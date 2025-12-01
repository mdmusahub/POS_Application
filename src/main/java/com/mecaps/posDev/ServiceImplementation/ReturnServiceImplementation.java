package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.*;
import com.mecaps.posDev.Enums.ReturnStatus;
import com.mecaps.posDev.Exception.ResourceNotFoundException;
import com.mecaps.posDev.Repository.*;
import com.mecaps.posDev.Request.ReturnOrderItemRequest;
import com.mecaps.posDev.Request.ReturnOrderRequest;
import com.mecaps.posDev.Response.ReturnOrderResponse;
import com.mecaps.posDev.Service.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


/**
 * Service implementation for handling product return orders,
 * including return creation, deletion, and retrieval of return details.
 */
@Service
@RequiredArgsConstructor
public class ReturnServiceImplementation implements ReturnService {

    private final ReturnOrderItemRepository returnOrderItemRepository;
    private final ReturnOrderRepository returnOrderRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductInventoryRepository productInventoryRepository;

    /**
     * Creates a return order, validates returned quantities,
     * updates inventory, calculates refund amount, and saves return items.
     *
     * @param req return order request containing items to be returned
     * @return response containing return order and its items
     * @throws ResourceNotFoundException when order or order items are not found
     */
    @Transactional
    @Override
    public ReturnOrderResponse createReturnOrder(ReturnOrderRequest req) {

        // 1. Fetch original order
        Order order = orderRepository.findById(req.getOrder_id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + req.getOrder_id()));

        Customer customer = order.getCustomer();

        // 2. Create new return order object
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setOrder(order);
        returnOrder.setRequested_by_customer_id(customer);
        returnOrder.setRefund_amount(0.0);
        returnOrder.setReturn_quantity(0L);

        returnOrder = returnOrderRepository.save(returnOrder);

        double totalRefund = 0.0;
        long totalReturnQty = 0L;

        List<ReturnOrderItem> returnOrderItems = new ArrayList<>();

        // ---------------------------------------------------------------------
        // Pre-calculate values needed to allocate order-level discount
        // We need:
        //  - subtotalBeforeOrderLevelDiscount: sum of each item's discounted price (i.e. price before applying order-level discount, which in createOrder was the 'discountedPrice' per item)
        //  - productLevelDiscountsTotal: sum of product-level discounts (we compute per item as baseTotalPrice - discountedPrice)
        // From these we can derive orderLevelDiscount = order.getDiscount() - productLevelDiscountsTotal
        // (createOrder accumulated product-level discounts first into order.discount and then added order-level discount)
        // ---------------------------------------------------------------------
        double subtotalBeforeOrderLevelDiscount = 0.0;   // sum of (discountedPrice) across items
        double productLevelDiscountsTotal = 0.0;        // sum of product-level discounts across items

        // Build these sums by iterating current order items (the original purchased items)
        for (OrderItem oldItem : order.getOrder_items()) {
            // base price (unit_price * qty) — unit_price was stored in orderItem
            double baseTotalPrice = oldItem.getUnit_price() * oldItem.getQuantity();

            // discountedPrice (the price before order-level discount but after product-level discount)
            // In createOrder you stored total_price = discountedPrice + gstAmount
            double gstAmount = oldItem.getGstAmount() != null ? oldItem.getGstAmount() : 0.0;
            double discountedPrice = oldItem.getTotal_price() - gstAmount; // total_price included GST, so subtract GST to get pre-tax discounted price

            // accumulate
            subtotalBeforeOrderLevelDiscount += discountedPrice;

            // product-level discount = baseTotalPrice - discountedPrice
            double productLevelDiscount = baseTotalPrice - discountedPrice;
            productLevelDiscountsTotal += productLevelDiscount;
        }

        // Compute order-level discount amount (if any)
        double totalRecordedDiscount = order.getDiscount() != null ? order.getDiscount() : 0.0;
        double orderLevelDiscount = totalRecordedDiscount - productLevelDiscountsTotal;
        if (orderLevelDiscount < 0) {
            // defensive: if negative due to rounding/mismatch, clamp to 0
            orderLevelDiscount = 0.0;
        }

        // Edge case: if subtotalBeforeOrderLevelDiscount is 0 (shouldn't normally happen), avoid division by zero.
        boolean noSubtotal = subtotalBeforeOrderLevelDiscount <= 0.0;

        // ---------------------------------------------------------------------
        // Now process return items (existing logic + improved refund calculation)
        // ---------------------------------------------------------------------
        for (ReturnOrderItemRequest itemReq : req.getReturnOrderItemRequests()) {

            OrderItem orderItem = orderItemRepository.findById(itemReq.getOrder_item_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Order Item not found with id: " + itemReq.getOrder_item_id()));

            // ---------- FIX #1 — Ensure the orderItem belongs to the SAME order ----------
            if (!orderItem.getOrder().getOrderId().equals(req.getOrder_id())) {
                throw new IllegalArgumentException("Order item " + itemReq.getOrder_item_id() +
                        " does not belong to order " + req.getOrder_id());
            }
            // ------------------------------------------------------------------------------

            // ---------- Check cumulative return quantity ----------
            Long alreadyReturnedQty = returnOrderItemRepository.sumReturnQty(orderItem.getOrder_item_id());
            if (alreadyReturnedQty == null) alreadyReturnedQty = 0L;

            long totalAfterNewReturn = alreadyReturnedQty + itemReq.getReturn_quantity();

            if (totalAfterNewReturn > orderItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Return quantity exceeds total purchased. Purchased: " + orderItem.getQuantity()
                                + ", Already Returned: " + alreadyReturnedQty
                                + ", Trying to return: " + itemReq.getReturn_quantity());
            }
            // -------------------------------------

            if (itemReq.getReturn_quantity() <= 0) {
                throw new IllegalArgumentException("Return quantity must be greater than 0");
            }

            Product product = orderItem.getProduct();
            ProductVariant variant = orderItem.getProductVariant();

            // ---------------------------------------------------------------------------------
            // REPLACED REFUND CALCULATION: proportionally remove order-level discount share

            double refundAmount = 0.0;
            Long returnQty = 0L;

            // -----------------------------------------------------------------------------
            // CASE 1: REFUNDED → calculate refund normally
            // -----------------------------------------------------------------------------
            if (itemReq.getReturn_status() == ReturnStatus.REFUNDED) {

                double itemTotalPrice = orderItem.getTotal_price();
                double gstAmount = orderItem.getGstAmount() != null ? orderItem.getGstAmount() : 0.0;
                double discountedPrice = itemTotalPrice - gstAmount;
                long purchasedQty = orderItem.getQuantity();
                returnQty = itemReq.getReturn_quantity();

                // compute this item's share of order-level discount (pre-tax)
                double itemOrderLevelShare = 0.0;
                if (!noSubtotal && orderLevelDiscount > 0.0) {
                    itemOrderLevelShare = (discountedPrice / subtotalBeforeOrderLevelDiscount) * orderLevelDiscount;
                }

                // per-unit price after removing allocated order-level discount
                double perUnitPreTaxAfterOrderDiscount = (discountedPrice - itemOrderLevelShare) / purchasedQty;

                // per-unit GST
                double perUnitGst = gstAmount / purchasedQty;

                // final per-unit amount customer paid
                double perUnitFinalPaid = perUnitPreTaxAfterOrderDiscount + perUnitGst;

                // refund amount
                refundAmount = round(perUnitFinalPaid * returnQty);
            }


            // -----------------------------------------------------------------------------
            // CASE 2: REPLACED → no refund (but inventory should be updated normally)
            // -----------------------------------------------------------------------------
            else if (itemReq.getReturn_status() == ReturnStatus.REPLACED) {

                refundAmount = 0.0;     // IMPORTANT: No refund for replacement
            }


            // Build ReturnOrderItem entity
            ReturnOrderItem returnItem = new ReturnOrderItem();
            returnItem.setOrderId(order);
            returnItem.setOrderItemId(orderItem);
            returnItem.setProductId(product);
            returnItem.setProductVariantId(variant);
            returnItem.setUnitPrice(orderItem.getUnit_price());
            returnItem.setReturnQuantity(returnQty);
            returnItem.setRefundAmount(refundAmount);
            returnItem.setReturnReason(itemReq.getReturn_reason());
            returnItem.setReturnStatus(itemReq.getReturn_status());

            returnOrderItems.add(returnItem);

            // Update inventory (add stock back)
            ProductInventory inventory = productInventoryRepository.findByproductVariant(variant)
                    .orElseThrow(() -> new RuntimeException("Inventory not found for variant"));
            inventory.setQuantity(inventory.getQuantity() + returnQty);
            productInventoryRepository.save(inventory);

            totalRefund += refundAmount;
            totalReturnQty += returnQty;
        }

        // Save all return order items
        returnOrderItemRepository.saveAll(returnOrderItems);

        // Update total refund + total returned qty on the returnOrder record
        returnOrder.setRefund_amount(round(totalRefund));
        returnOrder.setReturn_quantity(totalReturnQty);

        returnOrderRepository.save(returnOrder);

        return new ReturnOrderResponse(returnOrder, returnOrderItems);
    }


    /**
     * Deletes a return order along with all its associated items.
     *
     * @param id return order ID
     * @return success message
     * @throws ResourceNotFoundException when return order does not exist
     */
    @Override
    public String deleteReturnOrder(Long id) {
        ReturnOrder returnOrder = returnOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("This Product Id is not found " + id));

        Order order = returnOrder.getOrder();
        List<ReturnOrderItem> returnOrderItem = returnOrderItemRepository.findAllReturnOrderItemsByOrderId(order);

        returnOrderItemRepository.deleteAll(returnOrderItem);
        returnOrderRepository.delete(returnOrder);

        return "Deleted successfully";
    }

    /**
     * Finds a return order by ID.
     *
     * @param id return order ID
     * @return return order response
     * @throws ResourceNotFoundException if return order is not found
     */
    public ReturnOrderResponse getById(Long id) {
        ReturnOrder returnOrder = returnOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Return order id not found"));

        List<ReturnOrderItem> returnOrderItems =
                returnOrderItemRepository.findAllReturnOrderItemsByOrderId(returnOrder.getOrder());

        return null;
    }

    /**
     * Returns list of all return orders with their associated returned items.
     *
     * @return list of return order responses
     */
    public List<ReturnOrderResponse> getReturnOrder() {
        List<ReturnOrder> returnOrders = returnOrderRepository.findAll();

        return returnOrders.stream().map(returnOrder -> {
            List<ReturnOrderItem> returnOrderItems =
                    returnOrderItemRepository.findAllReturnOrderItemsByOrderId(returnOrder.getOrder());
            return new ReturnOrderResponse(returnOrder, returnOrderItems);
        }).toList();
    }

    private double round(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

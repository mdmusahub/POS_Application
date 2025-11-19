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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnServiceImplementation implements ReturnService {

    private final ReturnOrderItemRepository returnOrderItemRepository;
    private final ReturnOrderRepository returnOrderRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductInventoryRepository productInventoryRepository;

    @Transactional
    @Override
    public ReturnOrderResponse createReturnOrder(ReturnOrderRequest req) {

        // 1. Fetch the original order
        Order order = orderRepository.findById(req.getOrder_id())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + req.getOrder_id()));

        Customer customer = order.getCustomer();

        // 2. Create ReturnOrder object
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setOrder(order);
        returnOrder.setRequested_by_customer_id(customer);
        returnOrder.setRefund_amount(0.0);
        returnOrder.setReturn_quantity(0L);

        returnOrder = returnOrderRepository.save(returnOrder);

        double totalRefund = 0.0;
        long totalReturnQty = 0L;

        List<ReturnOrderItem> returnOrderItems = new ArrayList<>();

        // 3. Loop through return item requests
        for (ReturnOrderItemRequest itemReq : req.getReturnOrderItemRequests()) {

            OrderItem orderItem = orderItemRepository.findById(itemReq.getOrder_item_id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Order Item not found with id: " + itemReq.getOrder_item_id()));

            // quantity validations
            if (itemReq.getReturn_quantity() > orderItem.getQuantity()) {
                throw new IllegalArgumentException(
                        "Return quantity cannot be more than ordered quantity: " + orderItem.getQuantity());
            }

            // fetch product & variant
            Product product = orderItem.getProduct();
            ProductVariant variant = orderItem.getProductVariant();

            // refund calculation
            double unitPrice = orderItem.getUnit_price();
            double refundAmount = 0d;

            // create ReturnOrderItem
            ReturnOrderItem returnItem = new ReturnOrderItem();
            returnItem.setOrderId(order);
            returnItem.setOrderItemId(orderItem);
            returnItem.setProductId(product);
            returnItem.setProductVariantId(variant);
            returnItem.setUnitPrice(unitPrice);
            returnItem.setReturnQuantity(itemReq.getReturn_quantity());
            returnItem.setReturnReason(itemReq.getReturn_reason());
            returnItem.setReturnStatus(itemReq.getReturn_status());

            //condition if REPLACED then do not refund money
            if(itemReq.getReturn_status() == ReturnStatus.REFUNDED) {
                refundAmount = unitPrice * itemReq.getReturn_quantity();
                returnItem.setRefundAmount(refundAmount);
            }

            else {
                returnItem.setRefundAmount(0d);
            }

            returnOrderItems.add(returnItem);

            // inventory update (increase stock back)
            ProductInventory inventory = productInventoryRepository.findByproductVariant(variant)
                    .orElseThrow(() -> new RuntimeException("Inventory not found for variant: "));

            // in inventory database add the quantity of stocks return and update the database
            inventory.setQuantity(inventory.getQuantity() + itemReq.getReturn_quantity());
            productInventoryRepository.save(inventory);

            // update refund totals
            totalRefund += refundAmount;
            totalReturnQty += itemReq.getReturn_quantity();
        }

        // save all return items
        returnOrderItemRepository.saveAll(returnOrderItems);

        // update return order totals
        returnOrder.setRefund_amount(totalRefund);
        returnOrder.setReturn_quantity(totalReturnQty);

        returnOrderRepository.save(returnOrder);

        return new ReturnOrderResponse(returnOrder, returnOrderItems);
    }


    @Override
    public String deleteReturnOrder(Long id) {
        ReturnOrder returnOrder = returnOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("This Product Id is not found " + id));
        Order order = returnOrder.getOrder();
        List<ReturnOrderItem> returnOrderItem = returnOrderItemRepository.findAllReturnOrderItemsByOrderId(order);
        returnOrderItemRepository.deleteAll(returnOrderItem);
        returnOrderRepository.delete(returnOrder);
        return "Deleted successfully";
    }

    public ReturnOrderResponse getById(Long id) {
       ReturnOrder returnOrder = returnOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Return order id not found"));
       List<ReturnOrderItem> returnOrderItems =  returnOrderItemRepository.findAllReturnOrderItemsByOrderId(returnOrder.getOrder());

        return null;
    }

    public List<ReturnOrderResponse> getReturnOrder() {
       List<ReturnOrder> returnOrders = returnOrderRepository.findAll();
        return returnOrders.stream().map(returnOrder -> {
            List<ReturnOrderItem> returnOrderItems = returnOrderItemRepository.findAllReturnOrderItemsByOrderId(returnOrder.getOrder());
       return new ReturnOrderResponse(returnOrder, returnOrderItems);
        }).toList();
    }
}





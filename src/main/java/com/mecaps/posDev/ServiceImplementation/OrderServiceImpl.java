package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.*;
import com.mecaps.posDev.Enums.PaymentMode;
import com.mecaps.posDev.Enums.WaiverMode;
import com.mecaps.posDev.Exception.*;
import com.mecaps.posDev.Repository.*;
import com.mecaps.posDev.Request.OrderItemRequest;
import com.mecaps.posDev.Request.OrderRequest;
import com.mecaps.posDev.Response.OrderResponse;
import com.mecaps.posDev.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final DiscountRepository discountRepository;
    private final GstTaxRepository gstTaxRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final EmailService emailService;
    private final ReturnOrderItemRepository returnOrderItemRepository;
    private final OrderItemRepository orderItemRepository;



    /**
     * Creates a new order with full processing including:
     * <p>
     * - Customer lookup or creation<br>
     * - Order item validation (product, variant, stock)<br>
     * - Product-level and order-level discount calculation<br>
     * - GST calculation (CGST/SGST)<br>
     * - Inventory stock deduction<br>
     * - Payment validation (cash/UPI/mixed)<br>
     * - Sending HTML email confirmation<br>
     * <p>
     * All operations run inside a transaction to ensure consistency.
     *
     * @param orderRequest the request containing customer, items, and payment details
     * @return OrderResponse containing saved order data
     * @throws ProductNotFoundException         if product ID is invalid
     * @throws ProductVariantNotFoundException  if variant ID is invalid
     * @throws OutOfStockException              if ordered quantity exceeds stock
     * @throws OrderPaymentException            if paid amount does not match total amount
     * @throws RuntimeException                 for missing GST configuration
     */
    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number()).
                orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setPhoneNumber(orderRequest.getUser_phone_number());
                    newCustomer.setEmail(orderRequest.getUser_email());
                    return customerRepository.save(newCustomer);
                });

        Order order = new Order();
        order.setCustomer(customer);
        order.setUser_phone_number(orderRequest.getUser_phone_number());
        order.setUser_email(orderRequest.getUser_email());
        order.setOrder_status(orderRequest.getOrder_status());
        order.setPayment_mode(orderRequest.getPaymentMode());
        if (orderRequest.getPaymentMode() == PaymentMode.CASH) {
            //  Cash only
            order.setCash_amount(orderRequest.getCash_amount() != null ? orderRequest.getCash_amount() : "0.0");
            order.setOnline_amount("0.0");

        } else if (orderRequest.getPaymentMode() == PaymentMode.UPI) {
            // UPI only
            order.setOnline_amount(orderRequest.getOnline_amount() != null ? orderRequest.getOnline_amount() : "0.0");
            order.setCash_amount("0.0");

        } else if (orderRequest.getPaymentMode() == PaymentMode.MIXED) {
            // Cash + UPI
            order.setCash_amount(orderRequest.getCash_amount() != null ? orderRequest.getCash_amount() : "0.0");
            order.setOnline_amount(orderRequest.getOnline_amount() != null ? orderRequest.getOnline_amount() : "0.0");
        }

        order.setDiscount(0d);
        order.setTax(0.0);
        order.setTotal_amount(0.0);

        double subtotalBeforeTax = 0.0;
        double totalTaxAmount = 0.0;

        // we are creating and setting orderItem in order
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.getOrder_itemRequest()) {
            Product product = productRepository.findById(itemRequest.getProduct_id())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            ProductVariant productVariant = productVariantRepository.findById(itemRequest.getProduct_variant_id())
                    .orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Not Found"));

            Category category = product.getCategoryId();

            GstTax gstTax = gstTaxRepository.findByCategory(category)
                    .orElseGet(() -> {
                        Category parent = category.getParent_category();
                        if (parent != null) {
                            return gstTaxRepository.findByCategory(parent)
                                    .orElseThrow(() -> new RuntimeException(
                                            "GST not found for category or parent: " + category.getCategoryName()
                                    ));
                        }
                        throw new RuntimeException("No GST defined for category: " + category.getCategoryName());
                    });

            // set total base price in this variable so later we use in tax calculation
            double baseTotalPrice = productVariant.getProductVariantPrice() * itemRequest.getQuantity();
            double discountedPrice = baseTotalPrice;

            // Here we set Product level discount
            Discount discount = discountRepository
                    .findByproductVariant_productVariantId(productVariant.getProductVariantId())
                    .orElse(null);

            if (discount != null) {
                LocalDateTime now = LocalDateTime.now();

                // Condition 1: Must be active
                if (!discount.getIs_active()
                        || discount.getStart_date_time().isAfter(now)  // Discount has not started yet
                        || discount.getEnd_date_time().isBefore(now)// Discount already expired
                        || discount.getDiscount_value() == null
                        || discount.getDiscount_value() <= 0) {
                    // Do NOT apply discount
                    // BUT continue adding item normally
                } else {
                    // Apply discount
                    double discountAmount = 0.0;

                    if (discount.getWaiver_mode() == WaiverMode.PERCENTAGE) {
                        discountAmount = discountedPrice * discount.getDiscount_value() / 100;
                    } else if(discount.getWaiver_mode() == WaiverMode.FLAT_AMOUNT) {
                        discountAmount = discount.getDiscount_value() * itemRequest.getQuantity();
                    }

                    discountedPrice -= discountAmount;
                    order.setDiscount(order.getDiscount() + discountAmount);
                }
            }

            //  Price & GST Calculations
            double gstRate = gstTax.getGst_rate();
            double gstAmount = (discountedPrice * gstRate) / 100;
            double cGstAmount = gstAmount / 2;
            double sGstAmount = gstAmount / 2;

            //here we check inventory stock before set in orderItem entity. If order quantity is greater than inventory quantity then this will throw an exception.
            ProductInventory inventory = productInventoryRepository.findByproductVariant(productVariant)
                    .orElseThrow(() -> new ProductVariantNotFoundException(
                            "Product Variant Not Found" + productVariant.getProductId()));
            if (inventory.getQuantity() < itemRequest.getQuantity()) {
                throw new OutOfStockException("Requested " + itemRequest.getQuantity() + " but only " + inventory.getQuantity() + " available.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());

            // Set unit price and calculated amounts
            orderItem.setUnit_price(round(productVariant.getProductVariantPrice()));

            // Store GST details in OrderItem
            orderItem.setGstRate(gstRate);
            orderItem.setGstAmount(round(gstAmount));
            orderItem.setC_gstAmount(round(cGstAmount));
            orderItem.setS_gstAmount(round(sGstAmount));
            orderItem.setGstTax(gstTax);

            // Final total (discounted + GST)
            orderItem.setTotal_price(round(discountedPrice + gstAmount));


            subtotalBeforeTax += discountedPrice; // here we store total base price so later we calculate discount on order level discount
            totalTaxAmount += gstAmount;  // here we store total tax amount so later we can set this tax value in order entity
            orderItemList.add(orderItem);
        }

        order.setOrder_items(orderItemList);

        // here we are checking order level discount is present or not before set total price in order
        double orderLevelDiscount = 0.0;
        if (subtotalBeforeTax >= 1000 && subtotalBeforeTax < 2000) {
            orderLevelDiscount = subtotalBeforeTax * 0.02d; //  setting 2% discount if total amount is >= 1000 && < 2000
        } else if (subtotalBeforeTax >= 2000 && subtotalBeforeTax < 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.04d; //  setting 4% discount if total amount is >= 2000 && < 5000
        } else if (subtotalBeforeTax >= 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.06d;  // setting 6% discount if total amount is >= 5000
        }

        if (orderLevelDiscount > 0) {
            order.setDiscount(round(order.getDiscount() + orderLevelDiscount));
            subtotalBeforeTax -= orderLevelDiscount;
        }

        double calculatedOrderTotal = round(totalTaxAmount + subtotalBeforeTax);
        order.setTax(round(totalTaxAmount));
        order.setTotal_amount(round(calculatedOrderTotal));

        // here we check total amount or payment amount is equal to Total amount before saving
        double paidAmount = Double.parseDouble(order.getOnline_amount())
                + Double.parseDouble(order.getCash_amount());

        if (paidAmount > calculatedOrderTotal) {
            throw new OrderPaymentException("Payment exceeds total amount. Paid: "
                    + paidAmount + " Required: " + calculatedOrderTotal);
        }

        if (paidAmount < calculatedOrderTotal) {
            throw new OrderPaymentException("Payment is less than total amount. Paid: "
                    + paidAmount + " Required: " + calculatedOrderTotal);
        }

        // If equal, proceed
        order.setTotal_amount(round(calculatedOrderTotal));

        Order save = orderRepository.save(order);

        // managing inventory
        for (OrderItem orderItem : orderItemList) {
            ProductVariant productVariant = orderItem.getProductVariant();
            ProductInventory productInventory = productInventoryRepository.findByproductVariant(productVariant)
                    .orElseThrow(() -> new ProductVariantNotFoundException(
                            "Product Variant Not Found" + productVariant.getProductVariantId()));
            productInventory.setQuantity(productInventory.getQuantity() - orderItem.getQuantity());
            productInventoryRepository.save(productInventory);
        }

        //Send confirmation email
        String to = order.getUser_email();
        String subject = "Order Confirmed - #" + order.getOrderId();

        // Build HTML email
        String htmlContent = emailService.buildOrderConfirmationHtml(order);

        if (to != null && !to.isEmpty()) {
            try {
                emailService.sendHtmlEmail(to, subject, htmlContent);
                System.out.println("HTML order confirmation email sent to " + to);
            } catch (Exception e) {
                System.err.println("Failed to send HTML email: " + e.getMessage());
            }
        }
        return new OrderResponse(save);
    }

    /**
     * Updates an existing order with new customer details, payment mode,
     * order items, discounts, GST, and inventory adjustments.
     * <p>
     * This method performs the following:
     * <br>• Restores previous order item inventory
     * <br>• Processes updated product items with stock validation
     * <br>• Recalculates GST, discounts, and total amount
     * <br>• Updates customer details and payment mode
     * <br>• Saves updated order and deducts new inventory quantities
     * <p>
     * All operations run inside a transaction to maintain data consistency.
     *
     * @param id           the ID of the order to be updated
     * @param orderRequest the updated order details including items, payment, customer info
     * @return updated OrderResponse object
     * @throws OrderNotFound                if order does not exist
     * @throws ProductNotFoundException     if product ID in items is invalid
     * @throws ProductVariantNotFoundException if variant ID is invalid
     * @throws OutOfStockException          if requested quantity exceeds available stock
     * @throws RuntimeException             for missing GST configurations
     */
    @Transactional
    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFound("Order not found with ID : " + id));

        Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number())
                .orElseGet(() -> {
                    Customer updateCustomer = new Customer();
                    updateCustomer.setPhoneNumber(orderRequest.getUser_phone_number());
                    updateCustomer.setEmail(orderRequest.getUser_email());
                    return customerRepository.save(updateCustomer);
                });

        order.setCustomer(customer);
        order.setUser_phone_number(orderRequest.getUser_phone_number());
        order.setUser_email(orderRequest.getUser_email());
        order.setOrder_status(orderRequest.getOrder_status());
        order.setPayment_mode(orderRequest.getPaymentMode());

        if (orderRequest.getPaymentMode() == PaymentMode.CASH) {
            //  Cash only
            order.setCash_amount(orderRequest.getCash_amount() != null ? orderRequest.getCash_amount() : "0.0");
            order.setOnline_amount("0.0");
        } else if (orderRequest.getPaymentMode() == PaymentMode.UPI) {
            // UPI only
            order.setOnline_amount(orderRequest.getOnline_amount() != null ? orderRequest.getOnline_amount() : "0.0");
            order.setCash_amount("0.0");
        } else if (orderRequest.getPaymentMode() == PaymentMode.MIXED) {
            // Cash + UPI
            order.setCash_amount(orderRequest.getCash_amount() != null ? orderRequest.getCash_amount() : "0.0");
            order.setOnline_amount(orderRequest.getOnline_amount() != null ? orderRequest.getOnline_amount() : "0.0");
        }

        for (OrderItem oldItem : order.getOrder_items()) {

            //  Restore inventory first
            ProductVariant variant = oldItem.getProductVariant();
            ProductInventory inventory = productInventoryRepository.findByproductVariant(variant)
                    .orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Not Found"));

            inventory.setQuantity(inventory.getQuantity() + oldItem.getQuantity());
            productInventoryRepository.save(inventory);

            //  Check if this order item is referenced in return_order_item
            boolean isUsedInReturn = returnOrderItemRepository.existsByOrderItemId(oldItem);

            if (isUsedInReturn) {

                oldItem.setQuantity(0L);
                oldItem.setTotal_price(0.0);
                oldItem.setGstAmount(0.0);
                oldItem.setC_gstAmount(0.0);
                oldItem.setS_gstAmount(0.0);

                // update the item, do NOT delete
                orderItemRepository.save(oldItem);

            } else {

                oldItem.setQuantity(0L);
                oldItem.setTotal_price(0.0);
                oldItem.setGstAmount(0.0);
                oldItem.setC_gstAmount(0.0);
                oldItem.setS_gstAmount(0.0);

                orderItemRepository.save(oldItem);
            }
        }

// ---------------------------------------------------------

        double subtotalBeforeTax = 0.0;
        double totalTaxAmount = 0.0;

        order.setDiscount(0d);
        order.setTax(0.0);
        order.setTotal_amount(0.0);

        List<OrderItem> updatedOrderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.getOrder_itemRequest()) {

            Product product = productRepository.findById(itemRequest.getProduct_id())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            ProductVariant productVariant = productVariantRepository.findById(itemRequest.getProduct_variant_id())
                    .orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Not Found"));

            Category category = product.getCategoryId();

            GstTax gstTax = gstTaxRepository.findByCategory(category)
                    .orElseGet(() -> {
                        Category parent = category.getParent_category();
                        if (parent != null) {
                            return gstTaxRepository.findByCategory(parent)
                                    .orElseThrow(() -> new RuntimeException(
                                            "GST not found for category or parent: " + category.getCategoryName()));
                        }
                        throw new RuntimeException("No GST defined for category: " + category.getCategoryName());
                    });

            double baseTotalPrice = productVariant.getProductVariantPrice() * itemRequest.getQuantity();
            double discountedPrice = baseTotalPrice;

            Discount discount = discountRepository
                    .findByproductVariant_productVariantId(productVariant.getProductVariantId())
                    .orElse(null);

            if (discount != null) {
                LocalDateTime now = LocalDateTime.now();

                if (discount.getIs_active()
                        && !discount.getStart_date_time().isAfter(now)
                        && !discount.getEnd_date_time().isBefore(now)
                        && discount.getDiscount_value() != null
                        && discount.getDiscount_value() > 0) {
                }else{
                    double discountAmount = 0.0;

                    if (discount.getWaiver_mode() == WaiverMode.PERCENTAGE) {
                        discountAmount = discountedPrice * discount.getDiscount_value() / 100;
                    } else if(discount.getWaiver_mode() == WaiverMode.FLAT_AMOUNT) {
                        discountAmount = discount.getDiscount_value() * itemRequest.getQuantity();
                    }

                    discountedPrice -= discountAmount;

                    // Add to final order discount
                    order.setDiscount(order.getDiscount() + discountAmount);
                }
            }

            //  GST Calculations
            double gstRate = gstTax.getGst_rate();
            double gstAmount = (discountedPrice * gstRate) / 100;
            double cGstAmount = gstAmount / 2;
            double sGstAmount = gstAmount / 2;

            //  Validate stock BEFORE deducting
            ProductInventory inventory = productInventoryRepository.findByproductVariant(productVariant)
                    .orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Not Found"));
            if (inventory.getQuantity() < itemRequest.getQuantity()) {
                throw new OutOfStockException("Requested " + itemRequest.getQuantity()
                        + " but only " + inventory.getQuantity() + " available.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnit_price(round(productVariant.getProductVariantPrice()));
            orderItem.setGstRate(gstRate);
            orderItem.setGstAmount(round(gstAmount));
            orderItem.setC_gstAmount(round(cGstAmount));
            orderItem.setS_gstAmount(round(sGstAmount));
            orderItem.setGstTax(gstTax);
            orderItem.setTotal_price(round(discountedPrice + gstAmount));

            subtotalBeforeTax += discountedPrice;
            totalTaxAmount += gstAmount;
            updatedOrderItems.add(orderItem);
        }


// Create a map of old items using productVariantId (identity)
        Map<Long, OrderItem> oldItemMap = order.getOrder_items()
                .stream()
                .collect(Collectors.toMap(
                        oi -> oi.getProductVariant().getProductVariantId(),
                        oi -> oi,
                        (a, b) -> a
                ));

//  Create a new list that will become final updated list
        List<OrderItem> finalList = new ArrayList<>();

        for (OrderItem newItem : updatedOrderItems) {

            Long variantId = newItem.getProductVariant().getProductVariantId();

            if (oldItemMap.containsKey(variantId)) {
                // UPDATE EXISTING ROW (NO DELETE)
                OrderItem oldItem = oldItemMap.get(variantId);

                oldItem.setQuantity(newItem.getQuantity());
                oldItem.setUnit_price(newItem.getUnit_price());
                oldItem.setGstRate(newItem.getGstRate());
                oldItem.setGstAmount(newItem.getGstAmount());
                oldItem.setC_gstAmount(newItem.getC_gstAmount());
                oldItem.setS_gstAmount(newItem.getS_gstAmount());
                oldItem.setTotal_price(newItem.getTotal_price());
                oldItem.setGstTax(newItem.getGstTax());

                finalList.add(oldItem);

            } else {
                // ADD NEW ITEM
                finalList.add(newItem);
            }
        }

// Replace list content without clearing JPA list
        order.getOrder_items().retainAll(finalList);  // keeps only updated items
        order.getOrder_items().addAll(
                finalList.stream()
                        .filter(i -> !order.getOrder_items().contains(i))
                        .collect(Collectors.toList())
        );
// ========================================================================


        //  Order-level discount
        double orderLevelDiscount = 0.0;
        if (subtotalBeforeTax >= 1000 && subtotalBeforeTax < 2000) {
            orderLevelDiscount = subtotalBeforeTax * 0.02;
        } else if (subtotalBeforeTax >= 2000 && subtotalBeforeTax < 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.04;
        } else if (subtotalBeforeTax >= 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.06;
        }

        if (orderLevelDiscount > 0) {
            order.setDiscount(round(order.getDiscount() + orderLevelDiscount));
            subtotalBeforeTax -= orderLevelDiscount;
        }

        //  Set tax & total
        order.setTax(round(totalTaxAmount));
        double calculatedOrderTotal = round(subtotalBeforeTax + totalTaxAmount);
        order.setTotal_amount(round(calculatedOrderTotal));

        // Payment validation SAME as createOrder()
        double paidAmount = Double.parseDouble(order.getOnline_amount())
                + Double.parseDouble(order.getCash_amount());

        if (paidAmount > calculatedOrderTotal) {
            throw new OrderPaymentException("Payment exceeds total amount. Paid: "
                    + paidAmount + " Required: " + calculatedOrderTotal);
        }

        if (paidAmount < calculatedOrderTotal) {
            throw new OrderPaymentException("Payment is less than total amount. Paid: "
                    + paidAmount + " Required: " + calculatedOrderTotal);
        }

        //  Deduct inventory only after validation
        for (OrderItem orderItem : updatedOrderItems) {
            ProductVariant variant = orderItem.getProductVariant();
            ProductInventory inventory = productInventoryRepository.findByproductVariant(variant)
                    .orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Not Found"));
            inventory.setQuantity(inventory.getQuantity() - orderItem.getQuantity());
            productInventoryRepository.save(inventory);
        }

        Order updated = orderRepository.save(order);
        return new OrderResponse(updated);
    }
    /**
     * Retrieves all orders from the database and converts them
     * into a list of OrderResponse objects.
     *
     * @return list of all orders wrapped in OrderResponse
     */
    @Override
    public List<OrderResponse> getAll() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(OrderResponse::new).toList();
    }

    /**
     * Fetches a specific order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return OrderResponse containing the order details
     * @throws RuntimeException if the order ID does not exist
     */
    @Override
    public OrderResponse getById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("invalid order ID"));
        return new OrderResponse(order);
    }


    /**
     * Deletes an order by its ID.
     *
     * @param order_id the ID of the order to delete
     * @return success message after deletion
     * @throws RuntimeException if the order does not exist
     */
    @Override
    public String deleteOrder(Long order_id) {
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order deleted successfully";
    }

    /**
     * Rounds a decimal value to 2 decimal places using HALF_UP rounding.
     *
     * @param value the number to round
     * @return the rounded value as a double
     */
    private double round(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}

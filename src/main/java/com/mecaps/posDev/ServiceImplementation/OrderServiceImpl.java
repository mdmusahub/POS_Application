package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.*;
import com.mecaps.posDev.Enums.WaiverMode;
import com.mecaps.posDev.Exception.OutOfStockException;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Exception.ProductVariantNotFoundException;
import com.mecaps.posDev.Repository.*;
import com.mecaps.posDev.Request.OrderItemRequest;
import com.mecaps.posDev.Request.OrderRequest;
import com.mecaps.posDev.Response.OrderResponse;
import com.mecaps.posDev.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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


    // create order
    @Transactional
    public String createOrder(OrderRequest orderRequest) {
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
        order.setCash_amount(orderRequest.getCash_amount());
        order.setOnline_amount(orderRequest.getOnline_amount());
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

            Category category = product.getCategory_id();

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
            double baseTotalPrice = productVariant.getProduct_variant_price() * itemRequest.getQuantity();
            double discountedPrice = baseTotalPrice;
            // Here we set Product level discount
            Discount discount = discountRepository.findByproductVariant(productVariant.getProduct_variant_id())
                    .orElse(null);
            if (discount != null) {
                double discountAmount = 0.0;
                if (discount.getWaiver_mode() == WaiverMode.PERCENTAGE) {
                    discountAmount = discountedPrice * discount.getDiscount_value() / 100;
                } else if (discount.getWaiver_mode() == WaiverMode.FLAT_AMOUNT) {
                    discountAmount = discount.getDiscount_value() * itemRequest.getQuantity();
                }
                discountedPrice -= discountAmount;
                order.setDiscount(order.getDiscount() + discountAmount);
            }

            //  Price & GST Calculations
            double gstRate = gstTax.getGst_rate();
            double gstAmount = (discountedPrice * gstRate) / 100;
            double cGstAmount = gstAmount / 2;
            double sGstAmount = gstAmount / 2;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(productVariant);

            //here we check inventory stock before set in orderItem entity. If order quantity is greater than inventory quantity then this will throw an exception.
            ProductInventory inventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id())
                    .orElseThrow(() -> new ProductVariantNotFoundException(
                            "Product Variant Not Found" + productVariant.getProduct_variant_id()));
            if (inventory.getQuantity() < itemRequest.getQuantity()) {
                throw new OutOfStockException("Requested " + itemRequest.getQuantity() + " but only " + inventory.getQuantity() + " available.");
            }
            // Set unit price and calculated amounts
            orderItem.setUnit_price(productVariant.getProduct_variant_price());

            // Store GST details in OrderItem
            orderItem.setGstRate(gstRate);
            orderItem.setGstAmount(gstAmount);
            orderItem.setC_gstAmount(cGstAmount);
            orderItem.setS_gstAmount(sGstAmount);
            orderItem.setGstTax(gstTax);

            // Final total (discounted + GST)
            orderItem.setTotal_price(discountedPrice + gstAmount);


            subtotalBeforeTax += discountedPrice; // here we store total base price so later we calculate discount on order level discount
            totalTaxAmount += gstAmount;  // here we store total tax amount so later we can set this tax value in order entity
            orderItemList.add(orderItem);
        }

        order.setOrder_items(orderItemList);

        // here we are checking order level discount is present or not before set total price in order
        double orderLevelDiscount = 0.0;
        if (subtotalBeforeTax >= 1000 && subtotalBeforeTax < 2000) {
            orderLevelDiscount = subtotalBeforeTax * 0.05d; //  setting 5% discount if total amount is >= 1000 && < 2000
        } else if (subtotalBeforeTax >= 2000 && subtotalBeforeTax < 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.10d; //  setting 10% discount if total amount is >= 2000 && < 5000
        } else if (subtotalBeforeTax >= 5000) {
            orderLevelDiscount = subtotalBeforeTax * 0.15d;  // setting 15% discount if total amount is >= 5000
        }

        if (orderLevelDiscount > 0) {
            order.setDiscount(order.getDiscount() + orderLevelDiscount);
            subtotalBeforeTax -= orderLevelDiscount;
        }

        order.setTax(totalTaxAmount);
        order.setTotal_amount(subtotalBeforeTax + totalTaxAmount); // here we set total order price after checking order level discount is present or not
        orderRepository.save(order);

        // managing inventory
        for (OrderItem orderItem : orderItemList) {
            ProductVariant productVariant = orderItem.getProductVariant();
            ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id())
                    .orElseThrow(() -> new ProductVariantNotFoundException(
                            "Product Variant Not Found" + productVariant.getProduct_variant_id()));
            productInventory.setQuantity(productInventory.getQuantity() - orderItem.getQuantity());
            productInventoryRepository.save(productInventory);
        }
        return "order created successfully";
    }

    public List<OrderResponse> getAll() {
        List<Order> all = orderRepository.findAll();
        return all.stream().map(OrderResponse::new).toList();
    }

    public OrderResponse getById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("invalid order ID"));
        return new OrderResponse(order);
    }

    public String deleteOrder(Long order_id) {
        Order order = orderRepository.findById(order_id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return "order deleted successfully";
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with ID : " + id));

        order.setPayment_mode(orderRequest.getPaymentMode());
        order.setCash_amount(orderRequest.getCash_amount());
        order.setOnline_amount(orderRequest.getOnline_amount());
        order.setOrder_status(orderRequest.getOrder_status());
        order.setUser_phone_number(orderRequest.getUser_phone_number());
        order.setUser_email(orderRequest.getUser_email());


        Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number()).orElseGet(() -> {
            Customer customer1 = new Customer();
            customer1.setPhoneNumber(orderRequest.getUser_phone_number());
            customer1.setEmail(orderRequest.getUser_email());
            return customerRepository.save(customer1);
        });
        order.setCustomer(customer);


        for (OrderItem orderItem : order.getOrder_items()) {
            ProductVariant productVariant = orderItem.getProductVariant();
            ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id())
                    .orElseThrow(() -> new
                            ProductNotFoundException("Inventory not found for this Variant : " + productVariant.getProduct_variant_id()));
            productInventory.setQuantity(productInventory.getQuantity() + orderItem.getQuantity());
            productInventoryRepository.save(productInventory);

        }
        order.getOrder_items().clear();

        double subtotalBeforeTax = 0.0;
        double totalTaxAmount = 0.0;
        double totalDiscount = 0.0;

        // we are updating and setting orderItem in order
        List<OrderItem> newOrderItem = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrder_itemRequest()) {
            Product product = productRepository.findById(orderItemRequest.getProduct_id()).
                    orElseThrow(() -> new ProductNotFoundException("Product not found"));


            ProductVariant productVariant = productVariantRepository.findById(orderItemRequest.getProduct_variant_id()).
                    orElseThrow(() -> new ProductVariantNotFoundException("Product variant not found"));


            Category category = product.getCategory_id();

            GstTax gstTax = gstTaxRepository.findByCategory(category).orElseGet(() -> {
                Category category1 = category.getParent_category();
                if (category1 != null) {
                    return gstTaxRepository.findByCategory(category1).orElseThrow(() -> new RuntimeException("GST not found for category " + category.getCategoryName()));
                }
                throw new RuntimeException("no gst defined");
            });
// again we set total base price in this variable so later we use in tax calculation
            double baseTotalPrice = productVariant.getProduct_variant_price() * orderItemRequest.getQuantity();
            double discountedPrice = baseTotalPrice;

// Here we set Product level discount again
            Discount discount = discountRepository.findByproductVariant(productVariant.getProduct_variant_id()).orElse(null);
            if (discount != null) {
                double discountAmount = 0.0;
                if (discount.getWaiver_mode() == WaiverMode.PERCENTAGE) {
                    discountAmount = discountedPrice * discount.getDiscount_value() / 100;
                } else if (discount.getWaiver_mode() == WaiverMode.FLAT_AMOUNT) {
                    discountAmount = discount.getDiscount_value() * orderItemRequest.getQuantity();
                }
                discountedPrice = discountedPrice - discountAmount;
                totalDiscount = totalDiscount + discountAmount;
            }

//  Price & GST Calculations
            double gstRate = gstTax.getGst_rate();
            double gstAmount = (discountedPrice * gstRate) / 100;
            double cGstAmount = gstAmount / 2;
            double sGstAmount = gstAmount / 2;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(orderItemRequest.getQuantity());

//here we check inventory
            ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id()).
                    orElseThrow(() -> new ProductVariantNotFoundException("Product Variant Vot Found"));
            if (productInventory.getQuantity() < orderItemRequest.getQuantity()) {
                throw new OutOfStockException("Requested " + orderItemRequest.getQuantity() + " but only " + productInventory.getQuantity() + " available.");
            }

// Set unit price and calculated amounts
            orderItem.setUnit_price(productVariant.getProduct_variant_price());

            // Store GST details in OrderItem
            orderItem.setGstRate(gstRate);
            orderItem.setGstAmount(gstAmount);
            orderItem.setC_gstAmount(cGstAmount);
            orderItem.setS_gstAmount(sGstAmount);
            orderItem.setGstTax(gstTax);

// Final total (discounted + GST)
            orderItem.setTotal_price(discountedPrice + gstAmount);

            subtotalBeforeTax += discountedPrice;// here we store total base price so later we calculate discount on order level discount
            totalTaxAmount += gstAmount;// here we store total tax amount so later we can set this tax value in order entity
            newOrderItem.add(orderItem);


            productInventory.setQuantity(productInventory.getQuantity() - orderItemRequest.getQuantity());
            productInventoryRepository.save(productInventory);

// here we are checking order level discount is present or not before set total price in order
            double orderLevelDiscount = 0.0;
            if (subtotalBeforeTax >= 1000 && subtotalBeforeTax < 2000) {
                orderLevelDiscount = subtotalBeforeTax * 0.05d; //  setting 5% discount if total amount is >= 1000 && < 2000
            } else if (subtotalBeforeTax >= 2000 && subtotalBeforeTax < 5000) {
                orderLevelDiscount = subtotalBeforeTax * 0.10d; //  setting 10% discount if total amount is >= 2000 && < 5000
            } else if (subtotalBeforeTax >= 5000) {
                orderLevelDiscount = subtotalBeforeTax * 0.15d;  // setting 15% discount if total amount is >= 5000
            }

            if (orderLevelDiscount > 0) {
                order.setDiscount(order.getDiscount() + orderLevelDiscount);
                subtotalBeforeTax -= orderLevelDiscount;
            }

            order.setDiscount(totalDiscount);
            order.setTax(totalTaxAmount);
            order.setTotal_amount(subtotalBeforeTax + totalTaxAmount); // here we set total order price after checking order level discount is present or not
            order.setOrder_items(newOrderItem);
            Order order1 = orderRepository.save(order);


        }
      return new OrderResponse(order);
    }

    }

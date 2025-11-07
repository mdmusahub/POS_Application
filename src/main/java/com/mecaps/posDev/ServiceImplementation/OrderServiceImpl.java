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
private final OrderItemRepository orderItemRepository;
private final ProductInventoryRepository productInventoryRepository;


// create order
@Transactional
public String createOrder(OrderRequest orderRequest) {
    Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number()).
            orElseGet(()-> {
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


    double totalTaxAmount = 0.0;
    double totalAmount = 0.0;

    // we are creating and setting orderItem in order
    List<OrderItem> orderItemList = new ArrayList<>();

    for(OrderItemRequest itemRequest : orderRequest.getOrder_itemRequest()){
        Product product = productRepository.findById(itemRequest.getProduct_id())
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
        ProductVariant productVariant = productVariantRepository.findById(itemRequest.getProduct_variant_id())
                .orElseThrow(()-> new ProductVariantNotFoundException("Product Variant Not Found"));

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


        //  Step 5: Price & GST Calculations
        double baseTotalPrice = productVariant.getProduct_variant_price() * itemRequest.getQuantity();
        double gstRate = gstTax.getGst_rate();
        double gstAmount = (baseTotalPrice * gstRate) / 100;
        double cGstAmount = gstAmount / 2;
        double sGstAmount = gstAmount / 2;

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductVariant(productVariant);
        orderItem.setUnit_price(productVariant.getProduct_variant_price());

        // Store GST details in OrderItem
        orderItem.setGstRate(gstRate);
        orderItem.setGstAmount(gstAmount);
        orderItem.setC_gstAmount(cGstAmount);
        orderItem.setS_gstAmount(sGstAmount);
        orderItem.setGstTax(gstTax);
        orderItem.setTotal_price(baseTotalPrice);

        totalTaxAmount += orderItem.getGstAmount();  // here we set tax amount in this variable and update after every Iteration
        totalAmount += orderItem.getTotal_price(); // here we set total amount in this variable and update after every Iteration

        //if order quantity is greater than inventory quantity then this will throw an exception.
        ProductInventory inventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id())
                .orElseThrow(()-> new ProductVariantNotFoundException(
                        "Product Variant Not Found" + productVariant.getProduct_variant_id()));
        Long available = inventory.getQuantity();
        Long requested = itemRequest.getQuantity();
        if (available >= requested) {
            orderItem.setQuantity(requested);
        } else {
            throw new OutOfStockException("Requested " + itemRequest.getQuantity() + " but only "
                    + inventory.getQuantity() + " available.");
        }

        // Here we set Product level discount
        discountRepository.findByproductVariant(productVariant.getProduct_variant_id())
                .ifPresent(d -> {
                    double discountAmount = 0.0;
                    if (d.getWaiver_mode() == WaiverMode.PERCENTAGE) {
                        discountAmount = orderItem.getTotal_price() * d.getDiscount_value() / 100;
                    } else if (d.getWaiver_mode() == WaiverMode.FLAT_AMOUNT) {
                        discountAmount = d.getDiscount_value() * itemRequest.getQuantity();
                    }
                    orderItem.setTotal_price(orderItem.getTotal_price() - discountAmount);
                    order.setDiscount(order.getDiscount() + discountAmount);
                });
        orderItemList.add(orderItem);
    }

    order.setOrder_items(orderItemList);
    order.setTotal_amount(totalAmount);
    order.setTax(totalTaxAmount);

    //  setting 5% discount if total amount is >= 1000 && < 2000
    if(order.getTotal_amount() >= 1000 && order.getTotal_amount() < 2000){
        double orderLevelDiscount = order.getTotal_amount() * 0.05d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() - orderLevelDiscount);
    }

    //  setting 10% discount if total amount is >= 2000 && < 5000
    else if (order.getTotal_amount() >= 2000 && order.getTotal_amount() < 5000) {
        double orderLevelDiscount = order.getTotal_amount() * 0.10d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() - orderLevelDiscount);
    }

    // setting 15% discount if total amount is >= 5000
    else if (order.getTotal_amount() >= 5000) {
        double orderLevelDiscount = order.getTotal_amount() * 0.15d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() - orderLevelDiscount);
    }

    orderRepository.save(order);

    // managing inventory
    for(OrderItem orderItem : orderItemList){
      ProductVariant productVariant = orderItem.getProductVariant();
      ProductInventory productInventory = productInventoryRepository.findByProductVariant(productVariant.getProduct_variant_id())
              .orElseThrow(()-> new ProductVariantNotFoundException(
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
    Order order = orderRepository.findById(order_id).orElseThrow(()-> new RuntimeException("Order not found"));
    orderRepository.delete(order);
    return "order deleted successfully";
}
}

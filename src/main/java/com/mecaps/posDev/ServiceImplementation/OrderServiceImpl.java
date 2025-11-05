package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.*;
import com.mecaps.posDev.Enums.WaiverMode;
import com.mecaps.posDev.Exception.ProductNotFoundException;
import com.mecaps.posDev.Exception.ProductVariantNotFoundException;
import com.mecaps.posDev.Repository.*;
import com.mecaps.posDev.Request.OrderItemRequest;
import com.mecaps.posDev.Request.OrderRequest;
import com.mecaps.posDev.Response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {
private final OrderRepository orderRepository;
private final ProductRepository productRepository;
private final CustomerRepository customerRepository;
private final ProductVariantRepository productVariantRepository;
private final DiscountRepository discountRepository;
private final OrderItemRepository orderItemRepository;


// create order
public String createOrder(OrderRequest orderRequest) {
    Customer customer = customerRepository.findByPhoneNumber(orderRequest.getUser_phone_number()).
            orElseGet(()-> {
                Customer newCustomer = new Customer();
                newCustomer.setPhoneNumber(orderRequest.getUser_phone_number());
                return newCustomer;
            });

    Order order = new Order();
    order.setCustomer(customer);
    order.setUser_phone_number(orderRequest.getUser_phone_number());
    order.setOrder_status(orderRequest.getOrder_status());
    order.setPayment_mode(orderRequest.getPaymentMode());
    order.setCash_amount(orderRequest.getCash_amount());
    order.setOnline_amount(orderRequest.getOnline_amount());
    order.setTax(orderRequest.getTax());
    order.setDiscount(0d);
    order.setTotal_amount(0d);


    double totalWithoutTax = 0d;
    double totalTax = 0d;
    double taxPercent = (orderRequest.getTax() != null) ? orderRequest.getTax() : 0d;


    // we are creating and setting orderItem in order
    List<OrderItem> orderItemList = new ArrayList<>();
    for(OrderItemRequest itemRequest : orderRequest.getOrder_itemRequest()){
        Product product = productRepository.findById(itemRequest.getProduct_id())
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
        ProductVariant productVariant = productVariantRepository.findById(itemRequest.getProduct_variant_id())
                .orElseThrow(()-> new ProductVariantNotFoundException("Product Variant Not Found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setProductVariant(productVariant);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setUnit_price(itemRequest.getUnit_price());
        orderItem.setTotal_price(productVariant.getProduct_variant_price() * itemRequest.getQuantity());

        // Here we set Product level discount
        discountRepository.findByproductVariant(productVariant.getProduct_variant_id())
                .ifPresent(d -> {
                if(d.getWaiver_mode() == WaiverMode.PERCENTAGE){
                    double couponDiscount = orderItem.getTotal_price() * d.getDiscount_value()/100;
                    orderItem.setTotal_price(orderItem.getTotal_price() - couponDiscount);
                    order.setDiscount(order.getDiscount() + couponDiscount);
                }
                else if(d.getWaiver_mode() == WaiverMode.FLAT_AMOUNT){
                    double couponDiscount = d.getDiscount_value() * itemRequest.getQuantity();
                    orderItem.setTotal_price(orderItem.getTotal_price() - couponDiscount);
                    order.setDiscount(order.getDiscount() + couponDiscount);
                }
        });
        order.setTotal_amount(order.getTotal_amount() + orderItem.getTotal_price());
        orderItemRepository.save(orderItem);
        orderItemList.add(orderItem);
    }
    order.setOrder_items(orderItemList);

    //  setting 5% discount if total amount is >= 1000 && < 2000
    if(order.getTotal_amount() >= 1000 && order.getTotal_amount() < 2000){
        double orderLevelDiscount = order.getTotal_amount() * 0.05d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() + orderLevelDiscount);
    }

    //  setting 10% discount if total amount is >= 2000 && < 5000
    else if (order.getTotal_amount() >= 2000 && order.getTotal_amount() < 5000) {
        double orderLevelDiscount = order.getTotal_amount() * 0.10d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() + orderLevelDiscount);
    }

    // setting 15% discount if total amount is >= 5000
    else if (order.getTotal_amount() >= 5000) {
        double orderLevelDiscount = order.getTotal_amount() * 0.15d;
        order.setDiscount(order.getDiscount() + orderLevelDiscount);
        order.setTotal_amount(order.getTotal_amount() + orderLevelDiscount);
    }
    orderRepository.save(order);

    // managing inventory



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

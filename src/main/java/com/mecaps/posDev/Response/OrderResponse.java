package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Order;
import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Enums.OrderStatus;
import com.mecaps.posDev.Enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponse {

    private Long order_id;
    private PaymentMode payment_mode;
    private String cash_amount;
    private String online_amount;
    private OrderStatus order_status;
    // Order item list
    private List<OrderItemResponse> order_items;
    private Double discount;
    private Double tax;
    private Double total_amount;
    private String user_phone_number;
    private String customer_email;
    private String customer_phone;
    private LocalDateTime order_date;

    //  Default constructor
    public OrderResponse() {}

    // Constructor that takes Order entity and auto-fills data
    public OrderResponse(Order order) {
        if (order == null) return;

        this.order_id = order.getOrderId();
        this.payment_mode = order.getPayment_mode();
        this.cash_amount = order.getCash_amount();
        this.online_amount = order.getOnline_amount();
        this.order_status = order.getOrder_status();
        //  Convert each OrderItem to OrderItemResponse
        if (order.getOrder_items() != null) {
            this.order_items = order.getOrder_items().stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());
        }

        this.discount = order.getDiscount();
        this.total_amount = order.getTotal_amount();
        this.tax = order.getTax();
        this.user_phone_number = order.getUser_phone_number();
        this.order_date = order.getOrder_date();


        //  Customer details (safe null handling)
        Customer customer = order.getCustomer();
        if (customer != null) {
            this.customer_email = customer.getEmail();
            this.customer_phone = customer.getPhoneNumber();
        }

    }
}

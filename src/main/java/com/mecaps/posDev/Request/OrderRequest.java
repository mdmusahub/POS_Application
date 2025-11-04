package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.OrderItem;
import com.mecaps.posDev.Enums.OrderStatus;
import com.mecaps.posDev.Enums.PaymentMode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderRequest {
    private PaymentMode paymentMode;
    private String cash_amount;
    private String online_amount;
    private OrderStatus order_status;
    private Double tax;
    private String user_phone_number;
    private List<OrderItemRequest> order_itemRequest;
}

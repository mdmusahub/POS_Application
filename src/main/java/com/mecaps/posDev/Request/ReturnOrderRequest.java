package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Entity.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnOrderRequest {

    private Double refund_amount;
    private Long return_quantity;
    private Order order_id;
    private Customer requested_by_customer_id;

}

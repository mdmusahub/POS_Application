package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReturnOrderRequest {

    private Long order_id;
    private List<ReturnOrderItemRequest> returnOrderItemRequests;


}

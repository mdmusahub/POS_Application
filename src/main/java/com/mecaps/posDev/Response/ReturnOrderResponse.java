package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ReturnOrder;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReturnOrderResponse {

    private Double refund_amount;
    private Long return_quantity;
    //customer_name
    //order_name  //after entity is created by merging branch


    public ReturnOrderResponse(ReturnOrder returnOrder) {
        this.refund_amount = returnOrder.getRefund_amount();
        this.return_quantity = returnOrder.getReturn_quantity();
    }


}

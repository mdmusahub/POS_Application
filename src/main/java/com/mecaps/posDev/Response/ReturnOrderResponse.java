package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class ReturnOrderResponse {

    private Double refund_amount;
    private Long return_quantity;
    //customer_name
    //order_name  //after entity is created by merging branch


    public ReturnOrderResponse(ReturnOrder returnOrder, List<ReturnOrderItem> returnOrderItems) {
        this.refund_amount = returnOrder.getRefund_amount();
        this.return_quantity = returnOrder.getReturn_quantity();
    }


}

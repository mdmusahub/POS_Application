package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ReturnOrder;
import com.mecaps.posDev.Entity.ReturnOrderItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReturnOrderResponse {

    private Double refund_amount;
    private Long return_quantity;
    private List<ReturnOrderItemResponse> returnOrderItem;
    private LocalDateTime returnDate;


    public ReturnOrderResponse(ReturnOrder returnOrder, List<ReturnOrderItem> returnOrderItems) {
        this.refund_amount = returnOrder.getRefund_amount();
        this.return_quantity = returnOrder.getReturn_quantity();
        this.returnOrderItem = returnOrderItems.stream().map(ReturnOrderItemResponse::new).toList();
        this.returnDate = returnOrder.getReturn_date();
    }


}

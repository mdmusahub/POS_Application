package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ReturnOrderItem;
import com.mecaps.posDev.Enums.ReturnReason;
import com.mecaps.posDev.Enums.ReturnStatus;
import lombok.*;

@Getter
@Setter
public class ReturnOrderItemResponse {

    private Double refund_amount;
    private Long return_quantity;
    private ReturnReason return_reason;
    private ReturnStatus return_status;
    private Double unit_price;


    public ReturnOrderItemResponse(ReturnOrderItem returnOrderItem) {

        this.refund_amount = returnOrderItem.getRefund_amount();
        this.return_quantity = returnOrderItem.getReturn_quantity();
        this.return_reason = returnOrderItem.getReturn_reason();
        this.return_status = returnOrderItem.getReturn_status();
        this.unit_price = returnOrderItem.getUnit_price();
    }
}

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
    private Double unit_price;
    private Long orderId;
    private String product_name;
    private Long product_id;
    private String variantName;
    private Long variantId;
    private String product_variant_value;
    private ReturnReason return_reason;
    private ReturnStatus return_status;



    public ReturnOrderItemResponse(ReturnOrderItem returnOrderItem) {

        this.refund_amount = returnOrderItem.getRefundAmount();
        this.return_quantity = returnOrderItem.getReturnQuantity();
        this.unit_price = returnOrderItem.getUnitPrice();
        this.orderId = returnOrderItem.getOrderId().getOrderId();
        this.product_name = returnOrderItem.getProductId().getProductName();
        this.product_id = returnOrderItem.getProductId().getProductId();
        this.variantName = returnOrderItem.getProductVariantId().getVariantName();
        this.variantId = returnOrderItem.getProductVariantId().getProductVariantId();
        this.product_variant_value = returnOrderItem.getProductVariantId().getProductVariantValue();
        this.return_reason = returnOrderItem.getReturnReason();
        this.return_status = returnOrderItem.getReturnStatus();

    }
}

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

        this.refund_amount = returnOrderItem.getRefund_amount();
        this.return_quantity = returnOrderItem.getReturn_quantity();
        this.unit_price = returnOrderItem.getUnit_price();
        this.orderId = returnOrderItem.getOrder_id().getOrder_id();
        this.product_name = returnOrderItem.getProduct_id().getProductName();
        this.product_id = returnOrderItem.getProduct_id().getProduct_id();
        this.variantName = returnOrderItem.getProduct_variant_id().getVariantName();
        this.variantId = returnOrderItem.getProduct_variant_id().getProduct_variant_id();
        this.product_variant_value = returnOrderItem.getProduct_variant_id().getProduct_variant_value();
        this.return_reason = returnOrderItem.getReturn_reason();
        this.return_status = returnOrderItem.getReturn_status();

    }
}

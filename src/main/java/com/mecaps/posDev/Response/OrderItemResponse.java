package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long orderItemId;
    private Long variantId;
    private String product_name;
    private String variant_name;
    private Long quantity;
    private Double unit_price;
    private Double total_price;
    private Long order_id; // optional — to link with the parent order

    public OrderItemResponse() {
    }

    //  Constructor that takes OrderItem entity directly
    public OrderItemResponse(OrderItem item) {
        if (item == null) return;

        this.orderItemId = item.getOrder_item_id();
        this.variantId = item.getProductVariant().getProductVariantId();
        this.quantity = item.getQuantity();
        this.unit_price = item.getUnit_price();
        this.total_price = item.getTotal_price();

        // Get order info (if exists)
        if (item.getOrder() != null) {
            this.order_id = item.getOrder().getOrderId();
        }

        // Get product info (if exists)
        if (item.getProduct() != null) {
            this.product_name = item.getProduct().getProductName();
        }

        try {
            this.variant_name = item.getProductVariant().getVariantName();
        } catch (Exception e) {
            this.variant_name = null;
        }
    }
}


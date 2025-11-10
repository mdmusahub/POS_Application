package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long order_item_id;
    private Long product_id;
    private String product_name;
    private Long product_variant_id;
    private String variant_name;
    private Long quantity;
    private Double unit_price;
    private Double total_price;
    private Long order_id; // optional — to link with the parent order

    public OrderItemResponse() {}

    // ✅ Constructor that takes OrderItem entity directly
    public OrderItemResponse(OrderItem item) {
        if (item == null) return;

        this.order_item_id = item.getOrder_item_id();
        this.quantity = item.getQuantity();
        this.unit_price = item.getUnit_price();
        this.total_price = item.getTotal_price();

        // Get order info (if exists)
        if (item.getOrder() != null) {
            this.order_id = item.getOrder().getOrder_id();
        }

        // Get product info (if exists)
        if (item.getProduct() != null) {
            this.product_id = item.getProduct().getProduct_id();
            this.product_name = item.getProduct().getProductName();
        }

        // Get variant info (if exists)
        if (item.getProductVariant() != null) {
            this.product_variant_id = item.getProductVariant().getProduct_variant_id();

            // Optional: if your ProductVariant has attributes like color/size
            try {
                this.variant_name = item.getProductVariant().getVariantName();
            } catch (Exception e) {
                this.variant_name = null;
            }
        }
    }
}

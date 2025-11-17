package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Data;

@Data
public class ProductVariantRequest {

    private String product_variant_name;
    private Double product_variant_price;
    private Boolean refundable;
    private String product_variant_value;
    private Long product_variant;
    private ProductInventoryRequest productInventoryRequest;
}

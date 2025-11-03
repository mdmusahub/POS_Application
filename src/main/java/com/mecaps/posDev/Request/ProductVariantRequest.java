package com.mecaps.posDev.Request;

import com.mecaps.posDev.Entity.Product;
import lombok.Data;

@Data
public class ProductVariantRequest {

    private String variantName;
    private Double product_variant_price;
    private Boolean refundable;
    private String product_variant_value;
    private Long product_id;
}

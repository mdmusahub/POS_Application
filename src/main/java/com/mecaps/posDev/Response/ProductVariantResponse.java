package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
    private String variantName;
    private Double product_variant_price;
    private Boolean refundable;
    private String product_variant_value;
    private String product_name;

    public ProductVariantResponse(ProductVariant productVariant) {
        this.variantName = productVariant.getVariantName();
        this.product_variant_price = productVariant.getProductVariantPrice();
        this.product_variant_value = productVariant.getProductVariantValue();
        this.refundable = productVariant.getRefundable();
        this.product_name = productVariant.getProductId().getProductName();
    }
}

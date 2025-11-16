package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
    private String variantName;
    private Double product_variant_price;
    private Boolean refundable;
    private String product_variant_value;

    public ProductVariantResponse(ProductVariant productVariant) {
        this.variantName = productVariant.getVariantName();
        this.product_variant_price = productVariant.getProductVariantPrice();
        this.product_variant_value = productVariant.getProductVariantValue();
        this.refundable = productVariant.getRefundable();

    }
}

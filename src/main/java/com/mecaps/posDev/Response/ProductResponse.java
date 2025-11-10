package com.mecaps.posDev.Response;


import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private String product_name;
    private String product_description;
    private String product_category;
    private Boolean refundable;
    private Double min_variant_price;
    private Double max_variant_price;

    public ProductResponse(Product product) {
        this.product_name = product.getProductName();
        this.product_description = product.getProduct_description();
        this.product_category = product.getCategory_id().getCategoryName();
        this.refundable = product.getProduct_variant().isEmpty() ? null :
                product.getProduct_variant().getFirst().getRefundable();

        if (product.getProduct_variant() != null && !product.getProduct_variant().isEmpty()) {
            this.min_variant_price = product.getProduct_variant().stream()
                    .mapToDouble(ProductVariant::getProduct_variant_price)
                    .min()
                    .orElse(0.0);

            this.max_variant_price = product.getProduct_variant().stream()
                    .mapToDouble(ProductVariant::getProduct_variant_price)
                    .max()
                    .orElse(0.0);
        } else {
            this.min_variant_price = 0.0;
            this.max_variant_price = 0.0;
        }
    }
}


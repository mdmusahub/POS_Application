package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Product;
import com.mecaps.posDev.Entity.ProductInventory;
import com.mecaps.posDev.Entity.ProductVariant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProductResponse {

    private String product_name;
    private String product_description;
    private String product_category;
    private List<String> product_variant;
    private List<Double> product_variant_price;
    private Boolean refundable;
    private List<Long> product_quantity;

    public ProductResponse(Product product) {
        this.product_name = product.getProduct_name();
        this.product_description = product.getProduct_description();
        this.product_category = product.getCategory_id().getCategory_name();

        this.product_variant = product.getProduct_variants()
                .stream()
                .map(ProductVariant::getProduct_variant_name)
                .collect(Collectors.toList());

        this.product_variant_price = product.getProduct_variants()
                .stream()
                .map(ProductVariant::getProduct_variant_price)
                .collect(Collectors.toList());

        this.refundable = product.getProduct_variants().getFirst().getRefundable();

        this.product_quantity = product.getInventories()
                .stream()
                .map(ProductInventory::getQuantity)
                .collect(Collectors.toList());
    }


}

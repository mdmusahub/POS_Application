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
    private Boolean refundable;


    public ProductResponse(Product product) {
        this.product_name = product.getProductName();
        this.product_description = product.getProduct_description();
        this.product_category = product.getCategory_id().getCategoryName();
        this.refundable = product.getProduct_variant().getFirst().getRefundable();
    }


}

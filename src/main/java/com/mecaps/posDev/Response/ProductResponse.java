package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private String product_name;
    private String product_description;
    private String product_category;


    public ProductResponse(Product product) {
        this.product_name = product.getProductName();
        this.product_description = product.getProduct_description();
        this.product_category = product.getCategoryId().getCategoryName();


    }
}

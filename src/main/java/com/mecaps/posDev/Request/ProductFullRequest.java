package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFullRequest {

    private Long category_id;


    private String product_name;
    private String product_description;
    private String sku;

private List<ProductVariantRequest> productVariantRequests;
}

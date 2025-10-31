package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
        private String product_name;
        private String product_description;
        private String sku;
        private Long category_id;
}

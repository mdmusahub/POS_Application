package com.mecaps.posDev.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInventoryRequest {

    private String location;
    private Long quantity;

}

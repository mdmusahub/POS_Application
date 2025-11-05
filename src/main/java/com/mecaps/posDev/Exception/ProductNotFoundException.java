package com.mecaps.posDev.Exception;

public class ProductNotFoundException extends  RuntimeException{

    public ProductNotFoundException(String message) {

        super(message);
    }
}

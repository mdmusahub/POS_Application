package com.mecaps.posDev.exception;

public class ProductNotFoundExpection extends  RuntimeException{

    public ProductNotFoundExpection(String message) {

        super(message);
    }
}

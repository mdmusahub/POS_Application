package com.mecaps.posDev.Exception;

public class CustomerAlreadyExist extends RuntimeException{
    public CustomerAlreadyExist(String message){
        super(message);
    }
}

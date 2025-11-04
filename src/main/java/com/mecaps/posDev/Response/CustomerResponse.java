package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
    private String email ;
    private String phoneNumber ;
    public CustomerResponse(Customer request){
        this.email=request.getEmail() ;
        this.phoneNumber=request.getPhoneNumber() ;

    }
}

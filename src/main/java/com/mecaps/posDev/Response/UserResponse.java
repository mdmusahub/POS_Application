package com.mecaps.posDev.Response;

import com.mecaps.posDev.Entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String email ;
    private String password ;

public UserResponse(User request){
    this.email=request.getEmail();
    this.password=request.getPassword();
}

}

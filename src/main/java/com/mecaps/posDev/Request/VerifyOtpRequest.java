package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {
    private String email ;
    private String otp ;
}

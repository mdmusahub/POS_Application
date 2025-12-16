package com.mecaps.posDev.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String email;
    private String newPassword ;
    private String conformPassword ;


}
//OTP verify hone ke baad user naya password set karega.
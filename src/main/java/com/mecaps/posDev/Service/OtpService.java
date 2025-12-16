package com.mecaps.posDev.Service;

public interface OtpService {
    void genrateOtp(String email);
    void verifyOtp(String email , String otp) ;
    void resetPassword(String email , String newPass , String confirmPass) ;
}

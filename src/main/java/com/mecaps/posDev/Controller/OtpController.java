package com.mecaps.posDev.Controller;
import com.mecaps.posDev.Request.GenrateOtpRequest;
import com.mecaps.posDev.Request.ResetPasswordDTO;
import com.mecaps.posDev.Request.VerifyOtpRequest;
import com.mecaps.posDev.ServiceImplementation.OtpServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgetPassword")
@CrossOrigin
public class OtpController{

    private final OtpServiceImpl otpService ;

    public OtpController(OtpServiceImpl otpService) {
        this.otpService = otpService;

    }

    @PostMapping("/genrateOTP")
    public ResponseEntity<String> genrateOtp(@RequestBody GenrateOtpRequest request){
        otpService.genrateOtp(request.getEmail()) ;
        return ResponseEntity.ok("OTP send to your email") ;

    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.getEmail() , request.getOtp());
        return ResponseEntity.ok("OTP verified successfully") ;

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO request){
        otpService.resetPassword(request.getEmail() , request.getNewPassword(), request.getConformPassword());
        return ResponseEntity.ok("password Reset successfully") ;

    }


}

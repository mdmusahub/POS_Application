package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.User;
import com.mecaps.posDev.Repository.UserRepository;
import com.mecaps.posDev.Service.OtpService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpServiceImpl implements OtpService {

    private Map<String,String> otpMap = new HashMap<>() ;
    private Map<String, LocalDateTime> expiryDate = new HashMap<>() ;


private final UserRepository userRepository ;
private final EmailService emailService ;
private final PasswordEncoder passwordEncoder ;


    public OtpServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }
public void genrateOtp(String email){
  String otp = String.format("%06d" , new SecureRandom().nextInt(1000000));
  otpMap.put(email , otp);
  expiryDate.put(email , LocalDateTime.now().plusMinutes(60));
  emailService.sendHtmlEmail(email , "Your OTP" , "Your OTP is:" + otp);

}
    public void verifyOtp(String email , String otp) {
        String savedOtp = otpMap.get(email);
        LocalDateTime expiry = expiryDate.get(email);

        if (savedOtp == null) {
            throw new RuntimeException("OTP not generated");
        }

        if (!savedOtp.equals(otp)) {
            throw new RuntimeException("OTP incorrect");
        }

        if (expiry.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
    }

    public void resetPassword(String email , String newPass , String confirmPass){
        if(!newPass.equals(confirmPass)){
            throw new RuntimeException("Password do not match");
        }
        User user = userRepository.findByemail(email).orElseThrow(()->new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user) ;

        otpMap.remove(email);
        expiryDate.remove(email);



    }

}

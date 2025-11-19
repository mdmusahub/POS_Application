package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Entity.User;
import com.mecaps.posDev.Repository.UserRepository;
import com.mecaps.posDev.Request.AuthDTO;
import com.mecaps.posDev.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthDTO request) {
        User user = userRepository.findByemail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credential");
        }

        String token = jwtService.generateAccessToken(user.getEmail(), user.getRole());

        Map<String, String> map = new HashMap<>();
        map.put("Token :", token);
        return map;

    }
}

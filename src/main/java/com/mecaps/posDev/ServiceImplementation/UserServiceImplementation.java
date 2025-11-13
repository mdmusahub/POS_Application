package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.User;
import com.mecaps.posDev.Repository.UserRepository;
import com.mecaps.posDev.Request.UserRequest;
import com.mecaps.posDev.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public String createUser(UserRequest userRequest){
        User user= new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        userRepository.save(user) ;
        return "User is create successfully" ;
    }

}

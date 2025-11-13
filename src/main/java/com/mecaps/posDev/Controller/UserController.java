package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.UserRequest;
import com.mecaps.posDev.ServiceImplementation.UserServiceImplementation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userClass")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }


    @PostMapping("/createUser")
    public String createUser(@RequestBody UserRequest userRequest){
        return userServiceImplementation.createUser(userRequest);

    }



}

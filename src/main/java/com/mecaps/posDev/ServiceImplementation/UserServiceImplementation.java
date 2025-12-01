package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.User;
import com.mecaps.posDev.Repository.UserRepository;
import com.mecaps.posDev.Request.ChangePasswordDTO;
import com.mecaps.posDev.Request.UserRequest;
import com.mecaps.posDev.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing user operations such as creating a new user.
 */
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user by saving the email, encoded password,
     * and assigned role into the database.
     *
     * @param userRequest request containing user details
     * @return success message after user creation
     */
    @Override
    public String createUser(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setActive(userRequest.isActive());

        userRepository.save(user);
        return "User is create successfully";
    }



    public  String setPassword(long id , ChangePasswordDTO request){

        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("Uer not found"));

        if(!passwordEncoder.matches(request.getOldPassword(),user.getPassword())) {
            return "incorrect password" ;
        }

        if(!request.getNewPassword().equals(request.getConformPassword())){
            return "New Password and Confirm Password must match!" ;
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
        return "Change password";
    }
}

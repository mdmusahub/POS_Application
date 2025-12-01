package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.User;
import com.mecaps.posDev.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This service loads user details (email, password, role) from the database
 * for authentication during login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for injecting UserRepository.
     *
     * @param userRepository the repository used to fetch user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user’s authentication details using email as the username.
     * <p>
     * Used by Spring Security during login.
     *
     * @param email the email of the user attempting to log in
     * @return UserDetails containing username, encoded password, and roles
     * @throws UsernameNotFoundException if the email is not registered
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByemail(email)
                .orElseThrow(() -> new RuntimeException("User not found " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}

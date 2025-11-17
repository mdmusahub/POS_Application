package com.mecaps.posDev.Config;

import com.mecaps.posDev.Security.JwtAuthFilter;
import com.mecaps.posDev.ServiceImplementation.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter ;

    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth-> auth
        .requestMatchers("/auth/login").permitAll()
         .requestMatchers("/userClass/createUser").permitAll()
        .requestMatchers("/userClass/*").hasRole("ADMIN").anyRequest().authenticated());
         httpSecurity.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);
         return httpSecurity.build() ;

    }
}

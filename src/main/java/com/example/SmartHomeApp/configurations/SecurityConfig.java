package com.example.SmartHomeApp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // URL /device/** available only for role "device".
                        .requestMatchers("/device/**").hasRole("device")
                        // URL /user/** available only for role "user".
                        .requestMatchers("/user/**").hasRole("user")
                        // Any request to any URL needs authenticated user.
                        .anyRequest().authenticated()
                )
                // Login form on /login URL available for every user.
                .formLogin(form -> form
                        .permitAll()
                )
                // Every user is able to logout.
                .logout(logout -> logout.permitAll()
                );

        return http.build();
    }
    // Method for password encoding
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

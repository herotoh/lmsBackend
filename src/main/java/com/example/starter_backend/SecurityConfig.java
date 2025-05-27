package com.example.starter_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .csrf(csrf -> csrf.disable())  // Disable CSRF
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()   // Allow all requests without authentication
            )
            .httpBasic(httpBasic -> httpBasic.disable())  // Disable HTTP Basic Auth
            .formLogin(formLogin -> formLogin.disable()); // Disable form login

        return http.build();
    }
}

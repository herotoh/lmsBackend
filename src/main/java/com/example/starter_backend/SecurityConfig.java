package com.example.starter_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ use lambda DSL
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("/api/auth/**").permitAll() // Allow registration and login (public)
                // Remove or change this line:
                 .requestMatchers("/api/members/**").permitAll() // <--- REMOVE THIS LINE OR CHANGE TO .authenticated()
                //.anyRequest().authenticated() // ALL other requests (including /api/me and /api/members) must be authenticated
                .requestMatchers("/api/auth/**").permitAll() // Allow public endpoints like registration/login
                //.requestMatchers("/api/me").authenticated()  // Only logged-in users can access /api/me
                //.requestMatchers("/api/members/**").hasRole("ADMIN") // restrict members list to admins
                .anyRequest().authenticated() // everything else needs to be authenticated


            )
            .httpBasic(Customizer.withDefaults()); // Use HTTP Basic for authentication
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // ✅ your frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // needed for cookies or HTTP Basic with credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
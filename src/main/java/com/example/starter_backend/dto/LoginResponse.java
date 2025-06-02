// src/main/java/com/example/starter_backend/dto/LoginResponse.java
package com.example.starter_backend.dto;
import com.example.starter_backend.entity.Member;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List; // Assuming roles are a list of strings

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private List<String> roles; // Or whatever type your roles are
    private Member member; // Include the Member object

    // You can remove the explicit constructor, getters, and setters
    // as Lombok's @Data, @NoArgsConstructor, and @AllArgsConstructor handle these.

    // If you need a constructor that doesn't include 'member', you can add it explicitly:
    public LoginResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
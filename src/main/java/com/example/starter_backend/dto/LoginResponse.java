// src/main/java/com/example/starter_backend/dto/LoginResponse.java
package com.example.starter_backend.dto;

import java.util.List; // Assuming roles are a list of strings

public class LoginResponse {
    private String token;
    private String username;
    private List<String> roles; // Or whatever type your roles are

    // Constructor
    public LoginResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    // Setters (optional, but good practice if you need to modify fields after construction)
    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
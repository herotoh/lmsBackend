package com.example.starter_backend.dto;

import lombok.Data; // Import Lombok's @Data annotation
import lombok.NoArgsConstructor; // Import Lombok's @NoArgsConstructor annotation
import lombok.AllArgsConstructor; // Import Lombok's @AllArgsConstructor annotation

import java.time.LocalDate;

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class RegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String address;
    private String contactInfo;
    private boolean admin; // Added this field to control admin status during registration
    private String email;
    private String nric;
    private String mobile;
    private String remark;
    private String birthday; // Accepting as String for frontend compatibility
    private String sex;
    private LocalDate registrationDate;
    private LocalDate membershipExpiryDate;

    // If not using Lombok, keep your existing getters and setters.
    // Example for one field without Lombok:
    // public String getUsername() { return username; }
    // public void setUsername(String username) { this.username = username; }
}
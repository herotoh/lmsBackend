// RegistrationRequest.java
package com.example.starter_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String name;
    private String address;
    private String contactInfo;
    private boolean admin;
    private String email;
    private String nric;
    private String mobile;
    private String remark;
    private String birthday;
    private String sex;
    private LocalDate registrationDate;
    private LocalDate membershipExpiryDate;
}
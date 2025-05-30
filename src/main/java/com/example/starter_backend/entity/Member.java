package com.example.starter_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "email")
    private String email;

    private String nric;

    private String mobile;

    private String remark;

    private LocalDate birthday;

    private String sex;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "membership_expiry_date")
    private LocalDate membershipExpiryDate;
}
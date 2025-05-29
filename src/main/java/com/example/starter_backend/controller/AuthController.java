package com.example.starter_backend.controller;

import com.example.starter_backend.dto.RegistrationRequest;
import com.example.starter_backend.entity.Member;
import com.example.starter_backend.entity.User;
import com.example.starter_backend.service.MemberService;
import com.example.starter_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional; // <--- Ensure this import is present

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    // Your existing /api/auth/register method
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        if (registrationRequest.getUsername() == null || registrationRequest.getUsername().trim().isEmpty() ||
            registrationRequest.getPassword() == null || registrationRequest.getPassword().trim().isEmpty() ||
            registrationRequest.getName() == null || registrationRequest.getName().trim().isEmpty()) {
            return new ResponseEntity<>("Username, Password, and Full Name are required.", HttpStatus.BAD_REQUEST);
        }

        try {
            Member newMember = new Member();
            newMember.setName(registrationRequest.getName());
            newMember.setAddress(registrationRequest.getAddress());
            newMember.setContactInfo(registrationRequest.getContactInfo());
            newMember.setEmail(registrationRequest.getEmail());
            newMember.setNric(registrationRequest.getNric());
            newMember.setMobile(registrationRequest.getMobile());
            newMember.setRemark(registrationRequest.getRemark());

            if (registrationRequest.getBirthday() != null && !registrationRequest.getBirthday().trim().isEmpty()) {
                try {
                    newMember.setBirthday(LocalDate.parse(registrationRequest.getBirthday()));
                } catch (DateTimeParseException e) {
                    return new ResponseEntity<>("Invalid birthday format. Please use YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
                }
            } else {
                newMember.setBirthday(null);
            }

            newMember.setSex(registrationRequest.getSex());
            newMember.setRegistrationDate(LocalDate.now());
            newMember.setMembershipExpiryDate(LocalDate.now().plusYears(1));

            Member savedMember = memberService.addMember(newMember);

            User registeredUser = userService.registerUser(
                    registrationRequest.getUsername(),
                    registrationRequest.getPassword(),
                    savedMember,
                    registrationRequest.isAdmin()
            );

            return new ResponseEntity<>("User and Member registered successfully!", HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            // This case handles situations where there's no authenticated principal
            return new ResponseEntity<>("No authenticated user found.", HttpStatus.UNAUTHORIZED);
        }

        String username = userDetails.getUsername();

        // Use Optional to handle the possibility of no user found gracefully
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Member member = user.getMember(); // Get the associated Member

            if (member != null) {
                // Success: User found and has an associated Member. Return the Member object.
                return ResponseEntity.ok(member); // Returns ResponseEntity<Member>
            } else {
                // This scenario means a User exists but is not linked to a Member.
                // It indicates a potential data inconsistency if every User is supposed to have a Member.
                return new ResponseEntity<>("Authenticated user has no associated member details.", HttpStatus.NOT_FOUND); // Returns ResponseEntity<String>
            }
        } else {
            // This scenario means the user was authenticated by Spring Security,
            // but their details (username) could not be found in your UserRepository.
            // This suggests a critical data inconsistency (e.g., user deleted after login).
            return new ResponseEntity<>("Authenticated user's details not found in database.", HttpStatus.NOT_FOUND); // Returns ResponseEntity<String>
        }
    }
}
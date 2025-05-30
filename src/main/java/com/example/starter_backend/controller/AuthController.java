// AuthController.java
package com.example.starter_backend.controller;

import com.example.starter_backend.dto.LoginResponse;
import com.example.starter_backend.dto.RegistrationRequest;
import com.example.starter_backend.entity.Member;
import com.example.starter_backend.entity.User;
import com.example.starter_backend.service.MemberService;
import com.example.starter_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.starter_backend.security.JwtUtil; // Import JwtUtil

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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

            userService.registerUser(
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

@PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // --- NEW: Construct the LoginResponse DTO ---
            List<String> roles = userDetails.getAuthorities().stream()
                                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                                            .collect(Collectors.toList());

            LoginResponse loginResponse = new LoginResponse(token, userDetails.getUsername(), roles);
            // --- END NEW ---

            return ResponseEntity.ok(loginResponse); // Return the DTO
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred during login.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>("No authenticated user found.", HttpStatus.UNAUTHORIZED);
        }

        String username = userDetails.getUsername();

        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Member member = user.getMember();

            if (member != null) {
                return ResponseEntity.ok(member);
            } else {
                return new ResponseEntity<>("Authenticated user has no associated member details.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Authenticated user's details not found in database.", HttpStatus.NOT_FOUND);
        }
    }
}
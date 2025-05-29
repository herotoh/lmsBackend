package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.entity.User;
import com.example.starter_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional; // <--- Ensure this import is present

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, Member member, boolean isAdmin) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setMember(member);
        user.setRoles(new HashSet<>());
        if (isAdmin) {
            user.getRoles().add("ROLE_ADMIN");
        } else {
            user.getRoles().add("ROLE_MEMBER");
        }
        return userRepository.save(user);
    }

    /**
     * Finds a User by their username.
     * This is crucial for Spring Security to load user details and for fetching
     * the associated Member.
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty if not.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
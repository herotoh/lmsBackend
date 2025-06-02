package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.dto.MemberDTO;
import com.example.starter_backend.entity.User;
import com.example.starter_backend.entity.Role; // Import the Role enum
import com.example.starter_backend.repository.UserRepository;
import com.example.starter_backend.exception.UsernameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

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
            throw new UsernameTakenException("Username already taken: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setMember(member);
        user.setRoles(new HashSet<>());
        if (isAdmin) {
            user.getRoles().add(Role.ADMIN); // Use Role.ADMIN enum
        } else {
            user.getRoles().add(Role.MEMBER); // Use Role.MEMBER enum
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

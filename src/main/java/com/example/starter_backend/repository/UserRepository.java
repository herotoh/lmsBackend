package com.example.starter_backend.repository;

import com.example.starter_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find by username (used in login and authentication)
    Optional<User> findByUsername(String username);

    // Optionally find by associated member
    Optional<User> findByMemberId(Long memberId);

    // Optionally find all users by role
    // (e.g., list all admins or members)
    Optional<User> findByRolesContaining(String role);
    
}
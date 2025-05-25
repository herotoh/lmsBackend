package com.example.starter_backend.repository;

import com.example.starter_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Member entity.
 * Provides CRUD operations and custom queries for managing members in the database.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
package com.example.starter_backend.repository;

import com.example.starter_backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Custom query method to find members whose name contains the given string (case-insensitive).
     *
     * @param name Part or full name to search
     * @return List of matching members
     */
    List<Member> findByNameContainingIgnoreCase(String name);
}
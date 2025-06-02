// MemberService.java
package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing member-related operations.
 * Defines methods for CRUD operations and business logic for the Member entity.
 */
public interface MemberService {

    /**
     * Retrieves all members from the database.
     *
     * @return List of all members
     */
    List<Member> getAllMembers();

    /**
     * Retrieves a member by their ID.
     *
     * @param id The ID of the member to find
     * @return Optional containing the member if found, empty otherwise
     */
    Optional<Member> getMemberById(Long id);

    Optional<Member> getMemberByUsername(String username);

    /**
     * Adds a new member to the database.
     *
     * @param member The member to add
     * @return The saved member // Ensure this return type is consistent
     */
    Member addMember(Member member); // Returns the saved member, crucial for linking

    /**
     * Updates an existing member in the database.
     *
     * @param id            The ID of the member to update
     * @param memberDetails The updated member details
     * @return The updated member
     */
    Member updateMember(Long id, Member member);

    Member updateMember(Member member); // Keep this overload if used elsewhere

    /**
     * Deletes a member from the database by their ID.
     *
     * @param id The ID of the member to delete
     */
    void deleteMember(Long id);

    /**
     * Searches for members by name.
     *
     * @param name The name to search for (partial match)
     * @return List of members matching the name
     */
    List<Member> searchMembersByName(String name);
}
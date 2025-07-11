package com.example.starter_backend.controller;

import com.example.starter_backend.dto.MemberDTO;
import com.example.starter_backend.entity.Member;
import com.example.starter_backend.service.MemberService;
import com.example.starter_backend.exception.MemberNotFoundException; // Import custom exception

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication; // Keep if still used elsewhere, otherwise remove
import org.springframework.security.core.userdetails.User; //Changed to org.springframework.security.core.userdetails.User

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    // Get all members
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        logger.info("\n--- GET /api/members: Retrieving all members ---\n");
        List<Member> members = memberService.getAllMembers();
        logger.info("Retrieved {} members.", members.size());
        return ResponseEntity.ok(members);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMemberProfile(Authentication authentication) {
        logger.info("\n--- GET /api/members/me: Retrieving current member profile ---\n");

        try {
            String username = authentication.getName(); // Get username from Authentication
            logger.debug("Authenticated username: {}", username);

            // Fetch member by username (assuming you have such a method)
            Optional<Member> member = memberService.getMemberByUsername(username); // Implement this in your service

            if (member.isPresent()) {
                logger.info("Member profile found for user: {}", username);
                return ResponseEntity.ok(member.get());
            } else {
                logger.warn("Member profile not found for user: {}", username);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving member profile: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        logger.info("\n--- GET /api/members/{} : Retrieving member by ID ---\n", id);

        Optional<Member> member = memberService.getMemberById(id);
        if (member.isPresent()) {
            logger.info("Member found with ID: {}", id);
            return ResponseEntity.ok(member.get());
        } else {
            logger.warn("Member not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new member
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        logger.info("\n--- POST /api/members: Creating new member ---\n" +
                    "MemberDTO: {}\n", memberDTO);

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setAddress(memberDTO.getAddress());
        member.setContactInfo(memberDTO.getContactInfo());
        member.setEmail(memberDTO.getEmail());
        member.setNric(memberDTO.getNric());
        member.setMobile(memberDTO.getMobile());
        member.setRemark(memberDTO.getRemark());
        member.setBirthday(memberDTO.getBirthday());
        member.setSex(memberDTO.getSex());
        member.setRegistrationDate(memberDTO.getRegistrationDate());
        member.setMembershipExpiryDate(
                memberDTO.getMembershipExpiryDate() != null
                        ? memberDTO.getMembershipExpiryDate()
                        : memberDTO.getRegistrationDate().plusYears(3));

        Member savedMember = memberService.addMember(member);
        logger.info("Member created successfully with ID: {}", savedMember.getId());
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

    // Update member by ID
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody MemberDTO memberDTO) {
        logger.info("\n--- PUT /api/members/{}: Updating member ---\n" +
                    "Member ID: {}\n" +
                    "MemberDTO: {}\n", id, id, memberDTO);

        try {
            // Retrieve the existing member first
            Optional<Member> existingMemberOpt = memberService.getMemberById(id);
            if (!existingMemberOpt.isPresent()) {
                logger.warn("Member not found with ID: {}", id);
                throw new MemberNotFoundException(id); // Throw custom exception if not found
            }

            Member existingMember = existingMemberOpt.get();

            // Update the fields of the existing member with data from the DTO
            existingMember.setName(memberDTO.getName());
            existingMember.setAddress(memberDTO.getAddress());
            existingMember.setContactInfo(memberDTO.getContactInfo());
            existingMember.setEmail(memberDTO.getEmail());
            existingMember.setNric(memberDTO.getNric());
            existingMember.setMobile(memberDTO.getMobile());
            existingMember.setRemark(memberDTO.getRemark());
            existingMember.setBirthday(memberDTO.getBirthday());
            existingMember.setSex(memberDTO.getSex());
            existingMember.setRegistrationDate(memberDTO.getRegistrationDate());
            existingMember.setMembershipExpiryDate(memberDTO.getMembershipExpiryDate());

            // Pass the updated existing Member object to the service layer
            Member updatedMember = memberService.updateMember(existingMember);
            logger.info("Member updated successfully with ID: {}", id);
            return ResponseEntity.ok(updatedMember);
        } catch (MemberNotFoundException ex) {
            logger.warn("Member not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Handle not found specifically
        } catch (Exception ex) {
            logger.error("An unexpected error occurred while updating member with ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Catch any other unexpected exceptions
        }
    }

    // Delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        logger.info("\n--- DELETE /api/members/{}: Deleting member ---\n" +
                    "Member ID: {}\n", id, id);

        try {
            memberService.deleteMember(id);
            logger.info("Member deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (MemberNotFoundException ex) {
            logger.warn("Member not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            logger.error("An unexpected error occurred while deleting member with ID: {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search members by name
    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchMembersByName(@RequestParam String name) {
        logger.info("\n--- GET /api/members/search: Searching members by name ---\n" +
                    "Search Name: {}\n", name);

        List<Member> members = memberService.searchMembersByName(name);
        logger.info("Found {} members matching name: {}", members.size(), name);
        return ResponseEntity.ok(members);
    }
}
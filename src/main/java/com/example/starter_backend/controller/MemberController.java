package com.example.starter_backend.controller;

import com.example.starter_backend.dto.MemberDTO;
import com.example.starter_backend.entity.Member;
import com.example.starter_backend.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // Get all members
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Get member by ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new member
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberDTO memberDTO) {
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
        //member.setMembershipExpiryDate(memberDTO.getMembershipExpiryDate());
        member.setMembershipExpiryDate(
        memberDTO.getMembershipExpiryDate() != null
        ? memberDTO.getMembershipExpiryDate()
        : memberDTO.getRegistrationDate().plusYears(3) // or any default logic
);

        Member savedMember = memberService.addMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

    // Update member by ID
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody MemberDTO memberDTO) {
        Optional<Member> existingMemberOpt = memberService.getMemberById(id);
        if (!existingMemberOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Member existingMember = existingMemberOpt.get();
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

        Member updatedMember = memberService.updateMember(existingMember);
        return ResponseEntity.ok(updatedMember);
    }

// REMOVE OR COMMENT OUT THIS SECTION:
// @GetMapping("/me")
// public ResponseEntity<Member> getMyDetails(Authentication authentication) {
//     String username = authentication.getName();
//     Optional<Member> memberOpt = memberService.getMemberByUsername(username);
//     return memberOpt.map(ResponseEntity::ok)
//                     .orElseGet(() -> ResponseEntity.notFound().build());
// }

    // Delete member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // Search members by name
    @GetMapping("/search")
    public List<Member> searchMembersByName(@RequestParam String name) {
        return memberService.searchMembersByName(name);
    }
}

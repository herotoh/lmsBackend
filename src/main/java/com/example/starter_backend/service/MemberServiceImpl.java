package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.entity.User; // Import User
import com.example.starter_backend.repository.MemberRepository;
import com.example.starter_backend.repository.UserRepository; // Import UserRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository; // Keep this autowired for getMemberByUsername

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member addMember(Member member) {
        // Logging to verify field values before save
        System.out.println("Saving member: " + member.getName() + ", email: " + member.getEmail() + ", sex: " + member.getSex());
        return memberRepository.save(member); // This correctly returns the saved member
    }

    @Override
    public Member updateMember(Long id, Member memberDetails) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setName(memberDetails.getName());
            member.setAddress(memberDetails.getAddress());
            member.setContactInfo(memberDetails.getContactInfo());
            member.setRegistrationDate(memberDetails.getRegistrationDate());
            member.setMembershipExpiryDate(memberDetails.getMembershipExpiryDate());
            member.setEmail(memberDetails.getEmail());
            member.setNric(memberDetails.getNric());
            member.setMobile(memberDetails.getMobile());
            member.setRemark(memberDetails.getRemark());
            member.setBirthday(memberDetails.getBirthday());
            member.setSex(memberDetails.getSex());
            return memberRepository.save(member);
        } else {
            throw new RuntimeException("Member not found with ID: " + id);
        }
    }

    @Override
    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public List<Member> searchMembersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Member> getMemberByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getMember);
    }
}
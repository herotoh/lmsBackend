// MemberServiceImpl.java
package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.dto.MemberDTO;
import com.example.starter_backend.entity.User;
import com.example.starter_backend.repository.MemberRepository;
import com.example.starter_backend.repository.UserRepository;
import com.example.starter_backend.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

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
        logger.info("Saving member: {} with email: {} and sex: {}", member.getName(), member.getEmail(), member.getSex());
        return memberRepository.save(member);
    }

    @Override
    public Member updateMember(Long id, Member memberDetails) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        existingMember.setName(memberDetails.getName());
        existingMember.setAddress(memberDetails.getAddress());
        existingMember.setContactInfo(memberDetails.getContactInfo());
        existingMember.setRegistrationDate(memberDetails.getRegistrationDate());
        existingMember.setMembershipExpiryDate(memberDetails.getMembershipExpiryDate());
        existingMember.setEmail(memberDetails.getEmail());
        existingMember.setNric(memberDetails.getNric());
        existingMember.setMobile(memberDetails.getMobile());
        existingMember.setRemark(memberDetails.getRemark());
        existingMember.setBirthday(memberDetails.getBirthday());
        existingMember.setSex(memberDetails.getSex());

        return memberRepository.save(existingMember);
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

    public Member getMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElse(null); // Or throw a custom exception if you prefer
    }

}
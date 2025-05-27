package com.example.starter_backend.controller;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    void setup() {
        memberRepository.deleteAll();
        member = new Member();
        member.setName("Alice");
        member.setContactInfo("alice@example.com");
        member.setRegistrationDate(LocalDate.now());
        member.setMembershipExpiryDate(LocalDate.now().plusYears(1));
        member = memberRepository.save(member);
    }

    @Test
    void testGetMemberById() throws Exception {
        mockMvc.perform(get("/api/members/" + member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testAddMember() throws Exception {
        Member newMember = new Member();
        newMember.setName("Bob");
        newMember.setContactInfo("bob@example.com");

        mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMember)))
                .andExpect(status().i

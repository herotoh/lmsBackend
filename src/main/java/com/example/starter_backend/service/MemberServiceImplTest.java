package com.example.starter_backend.service;

import com.example.starter_backend.entity.Member;
import com.example.starter_backend.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member sampleMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleMember = new Member();
        sampleMember.setId(1L);
        sampleMember.setName("John Doe");
        sampleMember.setContactInfo("john@example.com");
        sampleMember.setRegistrationDate(LocalDate.now());
        sampleMember.setMembershipExpiryDate(LocalDate.now().plusYears(1));
    }

    @Test
    void testAddMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(sampleMember);
        Member saved = memberService.addMember(sampleMember);
        assertNotNull(saved);
        assertEquals("John Doe", saved.getName());
    }

    @Test
    void testGetMemberById() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(sampleMember));
        Optional<Member> result = memberService.getMemberById(1L);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testGetAllMembers() {
        when(memberRepository.findAll()).thenReturn(Arrays.asList(sampleMember));
        List<Member> members = memberService.getAllMembers();
        assertEquals(1, members.size());
    }

    @Test
    void testDeleteMember() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        memberService.deleteMember(1L);
        verify(memberRepository, times(1)).deleteById(1L);
    }
}

package com.example.starter_backend.dto;

import lombok.Data;

@Data
public class LoanRequestDTO {
    private Long bookId;
    private Long memberId;
}
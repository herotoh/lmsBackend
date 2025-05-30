// LoanService.java
package com.example.starter_backend.service;

import com.example.starter_backend.entity.Loan;

import java.util.List;

public interface LoanServiceImpl {
    Loan borrowBook(Long bookId, Long memberId);

    Loan returnBook(Long loanId);

    List<Loan> getAllLoans();
}
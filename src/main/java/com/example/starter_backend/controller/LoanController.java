package com.example.starter_backend.controller;

import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/borrow")
    public Loan borrowBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        return loanService.borrowBook(bookId, memberId);
    }

    @PostMapping("/return/{loanId}")
    public Loan returnBook(@PathVariable Long loanId) {
        return loanService.returnBook(loanId);
    }

    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
}

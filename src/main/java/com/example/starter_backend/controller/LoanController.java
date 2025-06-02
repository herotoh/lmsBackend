package com.example.starter_backend.controller;

import com.example.starter_backend.dto.LoanRequestDTO;
import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

/*  20250602 */
    // Borrow a book
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody LoanRequestDTO loanRequest) {
        try {
            System.out.println("Received loan request: " + loanRequest);

            
            Loan loan = loanService.borrowBook(loanRequest.getBookId(), loanRequest.getMemberId());
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
 
/* 
   @PostMapping("/borrow")
  public ResponseEntity<Loan> borrowBook(@RequestBody LoanRequestDTO loanRequestDTO) {
   try {
    //System.out.println("Received loan request: " + loanRequestDTO);

    Loan newLoan = loanService.borrowBook(loanRequestDTO.getBookId(), loanRequestDTO.getMemberId());
    return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
   } catch (Exception e) {
    e.printStackTrace(); // Log the error
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
   }
  }
  */
    // Return a book
    @PutMapping("/return/{loanId}")
    public ResponseEntity<?> returnBook(@PathVariable Long loanId) {
        try {
            Loan loan = loanService.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get loans for a specific member
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getLoansByMember(@PathVariable Long memberId) {
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        return ResponseEntity.ok(loans);
    }

    // Get all overdue loans
    @GetMapping("/overdue")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        List<Loan> overdueLoans = loanService.getOverdueLoans();
        return ResponseEntity.ok(overdueLoans);
    }
}
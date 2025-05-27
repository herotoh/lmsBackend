package com.example.starter_backend.service;

import com.example.starter_backend.entity.*;
import com.example.starter_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired private LoanRepository loanRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;

    @Override
    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (book.getAvailableCopies() <= 0)
            throw new RuntimeException("No available copies");

        boolean hasOverdue = loanRepository.findByMemberAndStatus(member, "OVERDUE").size() > 0;
        if (hasOverdue)
            throw new RuntimeException("Member has overdue books");

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus("BORROWED");

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Override
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!"BORROWED".equals(loan.getStatus())) {
            throw new RuntimeException("Book already returned");
        }

        loan.setReturnDate(LocalDate.now());
        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            loan.setStatus("OVERDUE");
        } else {
            loan.setStatus("RETURNED");
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}

package com.example.starter_backend.service;

import com.example.starter_backend.entity.*;
import com.example.starter_backend.repository.*;
import com.example.starter_backend.exception.BookNotFoundException;
import com.example.starter_backend.exception.BookUnavailableException;
import com.example.starter_backend.exception.MemberNotFoundException;
import com.example.starter_backend.exception.MemberHasOverdueBooksException;
import com.example.starter_backend.exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService implements LoanServiceImpl {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional // Ensures atomicity of operations
    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        User user = userRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("User not found for member id: " + memberId));

        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException(bookId);
        }

        // Optional: Check if user has too many borrowed books or overdue books
        // List<Loan> userBorrowedLoans = loanRepository.findByUserAndStatus(user, Loan.LoanStatus.BORROWED);
        // if (userBorrowedLoans.size() >= MAX_LOANS_PER_USER) { ... }
        // boolean hasOverdue = loanRepository.findByUserAndStatus(user, Loan.LoanStatus.OVERDUE).size() > 0;
        // if (hasOverdue) { throw new RuntimeException("User has overdue books and cannot borrow more."); }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book); // Update available copies

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user); // Set User entity
        loan.setMember(member);
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusWeeks(2)); // Example: Due in 2 weeks
        loan.setStatus(Loan.LoanStatus.BORROWED);

        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (loan.getStatus() != Loan.LoanStatus.BORROWED) {
            throw new IllegalStateException("Book is not currently borrowed"); // Or a custom exception
        }

        loan.setReturnDate(LocalDate.now());
        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            loan.setStatus(Loan.LoanStatus.OVERDUE);
        } else {
            loan.setStatus(Loan.LoanStatus.RETURNED);
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        loanRepository.save(loan);

        return loan;
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
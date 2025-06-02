package com.example.starter_backend.service;

import com.example.starter_backend.entity.Book;
import com.example.starter_backend.entity.Loan;
import com.example.starter_backend.entity.Member; // Import Member entity
import com.example.starter_backend.entity.User;
import com.example.starter_backend.repository.BookRepository;
import com.example.starter_backend.repository.LoanRepository;
import com.example.starter_backend.repository.MemberRepository; // Import MemberRepository
import com.example.starter_backend.repository.UserRepository;
import com.example.starter_backend.exception.BookNotFoundException;
import com.example.starter_backend.exception.BookUnavailableException;
import com.example.starter_backend.exception.MemberNotFoundException;
import com.example.starter_backend.exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService { // Implements the interface

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository; // Added MemberRepository

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository; // Injected
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }
    


    @Override
    @Transactional // Ensures atomicity of operations
    public Loan borrowBook(Long bookId, Long memberId) { // Uses memberId as per Loan entity
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        Member member = memberRepository.findById(memberId) // Get Member entity
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        User user = userRepository.findByMemberId(memberId) // Get associated User entity
                .orElseThrow(() -> new RuntimeException("User not found for member id: " + memberId));


        if (book.getAvailableCopies() <= 0) {
            throw new BookUnavailableException(bookId);
        }

        // Optional: Check if user has too many borrowed books or overdue books
        // Example:
        // List<Loan> userBorrowedLoans = loanRepository.findByUserAndStatus(user, Loan.LoanStatus.BORROWED);
        // if (userBorrowedLoans.size() >= MAX_LOANS_PER_USER) { throw new RuntimeException("User has reached max loans."); }
        // boolean hasOverdue = loanRepository.findByUserAndStatus(user, Loan.LoanStatus.OVERDUE).size() > 0;
        // if (hasOverdue) { throw new RuntimeException("User has overdue books and cannot borrow more."); }


        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book); // Update available copies

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user); // Set User entity
        loan.setMember(member); // Set Member entity (as per Loan entity design)
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

        if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
            throw new IllegalStateException("Book is already returned for loan ID: " + loanId);
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        loan.setReturnDate(LocalDate.now());
        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            loan.setStatus(Loan.LoanStatus.OVERDUE);
            // Optional: Integrate with FineService here to calculate and apply fine immediately
            // fineService.calculateFineForLoan(loan);
        } else {
            loan.setStatus(Loan.LoanStatus.RETURNED);
        }

        return loanRepository.save(loan);
    }

    @Override
    public List<Loan> getLoansByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return loanRepository.findByUser(user);
    }
  @Override
    public List<Loan> getLoansByMemberId(Long memberId) {
        // Replace with your actual repository call
        return loanRepository.findByMemberId(memberId);
    }
    @Override
    public List<Loan> getOverdueLoans() {
        return loanRepository.findByDueDateBeforeAndReturnDateIsNull(LocalDate.now());
    }
}
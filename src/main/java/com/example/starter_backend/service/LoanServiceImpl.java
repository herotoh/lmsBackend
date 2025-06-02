package com.example.starter_backend.service;

import com.example.starter_backend.entity.Loan;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Loan Service operations.
 * Defines the business logic contract for managing book loans.
 */
public interface LoanServiceImpl { // This is the interface

    /**
     * Retrieves all loan records.
     * @return A list of all loans.
     */
    List<Loan> getAllLoans();

    /**
     * Retrieves a specific loan by its ID.
     * @param id The ID of the loan to retrieve.
     * @return An Optional containing the Loan if found, or empty otherwise.
     */
    Optional<Loan> getLoanById(Long id);

    /**
     * Handles the borrowing of a book.
     * @param bookId The ID of the book to be borrowed.
     * @param memberId The ID of the member borrowing the book.
     * @return The created Loan object.
     * @throws RuntimeException if the book or member is not found, or if the book is unavailable.
     */
    Loan borrowBook(Long bookId, Long memberId);

    /**
     * Handles the returning of a book.
     * @param loanId The ID of the loan record to be returned.
     * @return The updated Loan object.
     * @throws RuntimeException if the loan is not found or the book is already returned.
     */
    Loan returnBook(Long loanId);

    /**
     * Retrieves all loan records for a specific user.
     * @param userId The ID of the user whose loans are to be retrieved.
     * @return A list of loans associated with the given user ID.
     */
    List<Loan> getLoansByUserId(Long userId);

    /**
     * Retrieves all overdue loan records.
     * @return A list of all overdue loans.
     */
    List<Loan> getOverdueLoans();
}
package com.example.starter_backend.exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long id) {
        super("Loan not found with id: " + id);
    }

    public LoanNotFoundException(String message) {
        super(message);
    }
}

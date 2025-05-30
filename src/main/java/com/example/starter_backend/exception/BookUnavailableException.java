package com.example.starter_backend.exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(Long bookId) {
        super("Book with ID " + bookId + " is currently unavailable.");
    }

    public BookUnavailableException(String message) {
        super(message);
    }
}

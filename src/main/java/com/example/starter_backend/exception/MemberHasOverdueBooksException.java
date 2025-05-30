package com.example.starter_backend.exception;

public class MemberHasOverdueBooksException extends RuntimeException {
    public MemberHasOverdueBooksException(Long memberId) {
        super("Member with ID " + memberId + " has overdue books and cannot borrow new ones.");
    }

    public MemberHasOverdueBooksException(String message) {
        super(message);
    }
}

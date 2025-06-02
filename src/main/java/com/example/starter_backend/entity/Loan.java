package com.example.starter_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan") // Ensure your table name is 'loan' or adjust
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) // EAGER fetch for book details in frontend
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) // EAGER fetch for user details in frontend
    @JoinColumn(name = "user_id") // Changed from member_id to user_id
    private User user; // The user who borrowed the book (not Member directly)

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "member_id")  // New field and join column for Member
    private Member member;
    
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // New field for actual return date

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(name = "fine")
    private Double fine = 0.0; // Default fine to 0.0

    public enum LoanStatus {
        BORROWED,
        RETURNED,
        OVERDUE
    }
}
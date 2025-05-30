package com.example.starter_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Column(nullable = false, unique = true)
    private String isbn;

    private String publisher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Min(value = 1450, message = "Year must be after 1450")
    @Max(value = 2100, message = "Year must be before 2100")
    private int yearPublished;

    @Lob
    private String description;

    @Min(value = 0, message = "Total copies must be zero or more")
    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Min(value = 0, message = "Available copies must be zero or more")
    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    private String shelfLocation;

    private String coverImageUrl;

    private LocalDate addedDate = LocalDate.now();
}
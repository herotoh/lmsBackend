// BookRepository.java
package com.example.starter_backend.repository;

import com.example.starter_backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByAvailableCopiesGreaterThan(int minCopies);
    List<Book> findByIsbnContainingIgnoreCase(String isbn); // Added this method
   Optional<Book> findById(Long id); // Ensure you have this
   Optional<Book> findByIsbn(String isbn);
}
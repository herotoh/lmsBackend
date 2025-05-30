package com.example.starter_backend.service;

import com.example.starter_backend.entity.Book;
import com.example.starter_backend.repository.BookRepository;
import com.example.starter_backend.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book) {
        // Ensure availableCopies is not greater than totalCopies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setCategory(bookDetails.getCategory());
        book.setLanguage(bookDetails.getLanguage());
        book.setYearPublished(bookDetails.getYearPublished());
        book.setDescription(bookDetails.getDescription());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());
        book.setShelfLocation(bookDetails.getShelfLocation());
        book.setCoverImageUrl(bookDetails.getCoverImageUrl());

        // Ensure availableCopies is not greater than totalCopies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Optional<Book> searchBooksByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> searchBooks(String title, String author, String isbn) {
        if (title != null && !title.isEmpty()) {
            return searchBooksByTitle(title);
        } else if (author != null && !author.isEmpty()) {
            return searchBooksByAuthor(author);
        } else if (isbn != null && !isbn.isEmpty()) {
            return bookRepository.findByIsbn(isbn).map(List::of).orElse(List.of());
        } else {
            return getAllBooks();
        }
    }
}
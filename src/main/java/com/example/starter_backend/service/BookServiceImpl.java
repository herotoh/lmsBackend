// BookServiceImpl.java
package com.example.starter_backend.service;

import com.example.starter_backend.entity.Book;
import com.example.starter_backend.repository.BookRepository;
import com.example.starter_backend.exception.BookNotFoundException; // Custom Exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String title, String author, String isbn) {
        // Example implementation: filter by non-null parameters
        List<Book> books = bookRepository.findAll();
        if (title != null && !title.isEmpty()) {
            books.removeIf(book -> !book.getTitle().toLowerCase().contains(title.toLowerCase()));
        }
        if (author != null && !author.isEmpty()) {
            books.removeIf(book -> !book.getAuthor().toLowerCase().contains(author.toLowerCase()));
        }
        if (isbn != null && !isbn.isEmpty()) {
            books.removeIf(book -> !book.getIsbn().toLowerCase().contains(isbn.toLowerCase()));
        }
        return books;
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id)); // Use custom exception

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setAvailableCopies(bookDetails.getAvailableCopies());

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id); // Use custom exception
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    //@Override
    //public List<Book> searchBooksByIsbn(String isbn) {
    //    return bookRepository.findByIsbnContainingIgnoreCase(isbn);
    //}
}
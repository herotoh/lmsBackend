package com.example.starter_backend.service;

import com.example.starter_backend.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    Book addBook(Book book);

    Book updateBook(Long id, Book bookDetails);

    void deleteBook(Long id);

    List<Book> getAvailableBooks();

    List<Book> searchBooksByTitle(String title);

    List<Book> searchBooksByAuthor(String author);

    //Optional<Book> searchBooksByIsbn(String isbn);

    List<Book> searchBooks(String title, String author, String isbn);
}
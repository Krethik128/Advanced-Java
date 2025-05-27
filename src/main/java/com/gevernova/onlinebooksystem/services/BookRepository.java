package com.gevernova.onlinebooksystem.services;

import com.gevernova.onlinebooksystem.models.Book;

import java.util.*;
import java.util.stream.Collectors;

public class BookRepository {
    private final Map<String, Book> booksByIsbn; // Stores books, ISBN as key

    public BookRepository() {
        this.booksByIsbn = new HashMap<>();
    }

    /**
     * Adds a book to the repository.
     */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (booksByIsbn.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists.");
        }
        booksByIsbn.put(book.getIsbn(), book);
        System.out.println("Added book: " + book.getTitle() + " by " + book.getAuthor());
    }

    /**
     * Removes a book from the repository by ISBN.
     */
    public Book removeBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        Book removedBook = booksByIsbn.remove(isbn);
        if (removedBook != null) {
            System.out.println("Removed book: " + removedBook.getTitle());
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
        return removedBook;
    }

    /**
     * Finds a book by its ISBN.
     */
    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        return booksByIsbn.get(isbn);
    }

    /**
     * Searches for books by title (case-insensitive, partial match).
     */
    public Collection<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty for search.");
        }
        final String lowerCaseTitle = title.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerCaseTitle))
                .collect(Collectors.toList());
    }

    /**
     * Searches for books by author (case-insensitive, partial match).
     */
    public Collection<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty for search.");
        }
        final String lowerCaseAuthor = author.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(lowerCaseAuthor))
                .collect(Collectors.toList());
    }

    /**
     * Returns all books in the repository.
     */
    public Collection<Book> getAllBooks() {
        return booksByIsbn.values();
    }
}

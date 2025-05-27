package com.gevernova.onlinebooksystem.services;

import com.gevernova.onlinebooksystem.models.Author;
import com.gevernova.onlinebooksystem.models.Book;
import com.gevernova.onlinebooksystem.models.Category;
import com.gevernova.onlinebooksystem.models.DiscountStrategy;


import java.util.Collection;
import java.util.List;

// This class acts as a facade/orchestrator for the online bookstore system.
public class OnlineBookstoreService {
    private final BookRepository bookRepository;
    private final ShoppingCart shoppingCart;

    public OnlineBookstoreService() {
        this.bookRepository = new BookRepository();
        this.shoppingCart = new ShoppingCart();
    }

    // --- Book Management (Delegates to BookRepository) ---

    public void addBook(String isbn, String title, Author author, double price, Category category) {
        try {
            Book book = new Book( title, author, price, category);
            bookRepository.addBook(book);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    public Book removeBook(String isbn) {
        return bookRepository.removeBook(isbn);
    }

    public Book findBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    public Collection<Book> searchBooksByTitle(String title) {
        try {
            return bookRepository.searchBooksByTitle(title);
        } catch (IllegalArgumentException e) {
            System.err.println("Error searching by title: " + e.getMessage());
            return List.of(); // Return empty list on error
        }
    }

    public Collection<Book> searchBooksByAuthor(String author) {
        try {
            return bookRepository.searchBooksByAuthor(author);
        } catch (IllegalArgumentException e) {
            System.err.println("Error searching by author: " + e.getMessage());
            return List.of(); // Return empty list on error
        }
    }

    public Collection<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    // --- Cart Management (Delegates to ShoppingCart) ---

    public void addItemToCart(String isbn, int quantity) {
        Book book = bookRepository.findBookByIsbn(isbn);
        if (book == null) {
            System.err.println("Error: Book with ISBN " + isbn + " not found to add to cart.");
            return;
        }
        try {
            shoppingCart.addItem(book, quantity);
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding item to cart: " + e.getMessage());
        }
    }

    public void removeItemFromCart(String isbn) {
        Book book = bookRepository.findBookByIsbn(isbn);
        if (book == null) {
            System.err.println("Error: Book with ISBN " + isbn + " not found to remove from cart.");
            return;
        }
        try {
            shoppingCart.removeItem(book);
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing item from cart: " + e.getMessage());
        }
    }

    public void updateCartItemQuantity(String isbn, int newQuantity) {
        Book book = bookRepository.findBookByIsbn(isbn);
        if (book == null) {
            System.err.println("Error: Book with ISBN " + isbn + " not found to update quantity in cart.");
            return;
        }
        try {
            shoppingCart.updateItemQuantity(book, newQuantity);
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating cart item quantity: " + e.getMessage());
        }
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        shoppingCart.setDiscountStrategy(strategy);
    }

    public double getCartSubtotal() {
        return shoppingCart.calculateSubtotal();
    }

    public double getCartFinalTotal() {
        return shoppingCart.calculateFinalTotal();
    }

    public void printCart() {
        shoppingCart.printCartContents();
    }

    public void clearCart() {
        shoppingCart.clearCart();
    }
}

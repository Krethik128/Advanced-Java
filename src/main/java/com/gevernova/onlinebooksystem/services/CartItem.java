package com.gevernova.onlinebooksystem.services;

import com.gevernova.onlinebooksystem.models.Book;

import java.util.Objects;

public class CartItem {
    private Book book;
    private int quantity;

    public CartItem(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null for a cart item.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.quantity = quantity;
    }

    public double getItemTotalPrice() {
        return book.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "book=" + book.getTitle() +
                ", quantity=" + quantity +
                ", totalPrice=" + getItemTotalPrice() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(book, cartItem.book); // Equality based on the book itself
    }

    @Override
    public int hashCode() {
        return Objects.hash(book);
    }
}

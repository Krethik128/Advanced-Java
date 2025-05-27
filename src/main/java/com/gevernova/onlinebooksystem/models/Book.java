package com.gevernova.onlinebooksystem.models;

import java.util.Objects;
import java.util.Random;

public class Book {
    private String isbn;
    private final String title;
    private final Author author;
    private double price;
    private Category category;

    public Book( String title, Author author, double price, Category category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null ) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        this.isbn = generateIsbn(title);
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
    }
    public String generateIsbn(String title) {
        Random random=new Random();
       return title+random.nextInt(100);
    }


    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthorName() { return author.getName(); }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
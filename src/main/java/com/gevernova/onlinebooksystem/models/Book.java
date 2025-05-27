package com.gevernova.onlinebooksystem.models;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private double price;
    private Category category;

    public Book(String isbn, String title, String author, double price, Category category) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
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
package com.gevernova.onlinebooksystem.model;

import com.gevernova.onlinebooksystem.exceptions.BookNotFoundException;
import com.gevernova.onlinebooksystem.exceptions.InvalidBookDetailsException;
import com.gevernova.onlinebooksystem.services.Category;

public class Book {
    private String title;
    private Author author;
    private double price;
    private Category category;

    public Book(String title, Author author, double price, Category category) throws InvalidBookDetailsException {
        if(title.isEmpty()){
            throw new InvalidBookDetailsException("Title cannot be empty");
        }
        if(author == null){
            throw new InvalidBookDetailsException("Author cannot be null");
        }
        if(price < 0){
            throw new InvalidBookDetailsException("Price cannot be negative");
        }
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
    }

    public String getTitle() { return title; }
    public Author getAuthor() { return author; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }
}

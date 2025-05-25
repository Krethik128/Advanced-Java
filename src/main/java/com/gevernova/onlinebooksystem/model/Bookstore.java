package com.gevernova.onlinebooksystem.model;

import com.gevernova.onlinebooksystem.exceptions.BookNotFoundException;
import com.gevernova.onlinebooksystem.services.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bookstore {
    private List<Book> catalog = new ArrayList<>();

    public void addBook(Book book) {
        catalog.add(book);
    }

    public List<Book> searchByTitle(String title) {
        return catalog.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String authorName) {
        return catalog.stream()
                .filter(b -> b.getAuthor().getName().equalsIgnoreCase(authorName))
                .collect(Collectors.toList());
    }

    public List<Book> searchByCategory(Category category) {
        return catalog.stream()
                .filter(b -> b.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Book> getCatalog() {
        return catalog;
    }

}

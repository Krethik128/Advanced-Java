package com.gevernova.onlinebooksystem.models;

import com.gevernova.onlinebooksystem.exceptionhandling.BookNotFoundException;

import java.util.*;

public class Cart {
    private List<Book> items = new ArrayList<>();

    public void addBook(Book book) {
        items.add(book);
    }

    public void removeBook(Book book) {
        if(!items.contains(book)) {
            throw new BookNotFoundException("Book not found");
        }
        items.remove(book);
    }

    public List<Book> getItems() {
        return items;
    }

    public double calculateTotal(DiscountStrategy discountStrategy) {
        double total = items.stream().mapToDouble(Book::getPrice).sum();
        return discountStrategy.applyDiscount(total);
    }

}

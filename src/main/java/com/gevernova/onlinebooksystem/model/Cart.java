package com.gevernova.onlinebooksystem.model;

import com.gevernova.onlinebooksystem.exceptions.BookNotFoundException;
import com.gevernova.onlinebooksystem.services.DiscountStrategy;

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

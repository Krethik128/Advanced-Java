package com.gevernova.onlinebooksystem.services;


import com.gevernova.onlinebooksystem.models.Book;
import com.gevernova.onlinebooksystem.models.NoDiscount;
import com.gevernova.onlinebooksystem.models.DiscountStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {
    private final List<CartItem> items;
    private DiscountCalculator discountCalculator;
    private DiscountStrategy currentDiscountStrategy;

    public ShoppingCart() {
        this.items = new ArrayList<>();
        // Default to NoDiscount if not explicitly set
        this.discountCalculator = new DiscountCalculator();
        this.currentDiscountStrategy = new NoDiscount();
    }

    public void setDiscountStrategy(DiscountStrategy strategy) {
        if (strategy == null) {
            this.currentDiscountStrategy = new NoDiscount(); // Default to no discount
        } else {
            this.currentDiscountStrategy = strategy;
        }
        System.out.println("Discount strategy set to: " + currentDiscountStrategy.getClass().getSimpleName());
    }

    /**
     * Adds a book to the cart or updates its quantity if already present.
     */
    public void addItem(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBook().equals(book))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            System.out.println("Updated quantity for '" + book.getTitle() + "' to " + existingItem.get().getQuantity());
        } else {
            items.add(new CartItem(book, quantity));
            System.out.println("Added '" + book.getTitle() + "' to cart (Quantity: " + quantity + ")");
        }
    }

    /**
     * Removes a book from the cart.
     */
    public boolean removeItem(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        boolean removed = items.removeIf(item -> item.getBook().equals(book));
        if (removed) {
            System.out.println("Removed '" + book.getTitle() + "' from cart.");
        } else {
            System.out.println("'" + book.getTitle() + "' not found in cart.");
        }
        return removed;
    }

    /**
     * Updates the quantity of a book in the cart.
     * If quantity is 0 or less, the item is removed.
     */
    public void updateItemQuantity(Book book, int newQuantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBook().equals(book))
                .findFirst();

        if (existingItem.isPresent()) {
            if (newQuantity <= 0) {
                removeItem(book); // Remove if quantity becomes zero or negative
            } else {
                existingItem.get().setQuantity(newQuantity);
                System.out.println("Updated quantity for '" + book.getTitle() + "' to " + newQuantity);
            }
        } else {
            System.out.println("'" + book.getTitle() + "' not found in cart to update quantity.");
        }
    }

    /**
     * Calculates the total price of all items in the cart before any discounts.
     */
    public double calculateSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getItemTotalPrice)
                .sum();
    }

    /**
     * Calculates the final total price of the cart after applying the current discount strategy.
     */
    public double calculateFinalTotal() {
        double subtotal = calculateSubtotal();
        return discountCalculator.calculateFinalAmount(subtotal, currentDiscountStrategy);
    }

    /**
     * Gets an unmodifiable list of items in the cart.
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Return a copy to prevent external modification
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }

    public void printCartContents() {
        if (items.isEmpty()) {
            System.out.println("\nYour shopping cart is empty.");
            return;
        }
        System.out.println("\n=== Your Shopping Cart ===");
        items.forEach(item -> System.out.printf("- %s (ISBN: %s) x %d @ %.2f = %.2f%n",
                item.getBook().getTitle(), item.getBook().getIsbn(), item.getQuantity(),
                item.getBook().getPrice(), item.getItemTotalPrice()));
        System.out.printf("Subtotal: %.2f%n", calculateSubtotal());
        System.out.printf("Final Total (with discount): %.2f%n", calculateFinalTotal());
        System.out.println("============================");
    }
}

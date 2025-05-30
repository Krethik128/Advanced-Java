package com.gevernova.ecommercesystem.model;

import com.gevernova.ecommercesystem.exceptions.InvalidProductException;

import java.util.UUID;

public class Product {
    private final String id;
    private String name;
    private ProductCategory productCategory;
    private double price;
    private int quantity;

    public Product(ProductCategory productCategory, String name, double price, int quantity) throws InvalidProductException {
        if (name == null || name.trim().isEmpty()) { // Use trim() to handle names with only spaces
            throw new InvalidProductException("Product name cannot be empty or null.");
        }
        if (price <= 0) {
            throw new InvalidProductException("Product price must be greater than zero.");
        }
        if (quantity < 0) { // Quantity cannot be negative
            throw new InvalidProductException("Product quantity cannot be negative.");
        }

        this.id = UUID.randomUUID().toString();
        this.productCategory = productCategory;
        this.name = name.trim(); // Store trimmed name
        this.price = price;
        this.quantity = quantity;
    }

    // Setters with basic validation where applicable
    public void setName(String name) throws InvalidProductException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be empty or null.");
        }
        this.name = name.trim();
    }

    public void setCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public void setPrice(double price) throws InvalidProductException {
        if (price <= 0) {
            throw new InvalidProductException("Product price must be greater than zero.");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) throws InvalidProductException {
        if (quantity < 0) {
            throw new InvalidProductException("Product quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductCategory getCategory() {
        return productCategory;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
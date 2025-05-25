package com.gevernova.feedbackaggregator.model;

public class Product {
    private final String productId;
    private final String name;

    public Product(String productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    // equals & hashCode for Map usage
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return productId.equals(other.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}


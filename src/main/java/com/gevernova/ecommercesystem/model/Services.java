package com.gevernova.ecommercesystem.model;

public interface Services {
    void addProduct(Product product, int quantity);
    void updateProduct(Product product,int quantity);
    void deleteProduct(Product product);
}

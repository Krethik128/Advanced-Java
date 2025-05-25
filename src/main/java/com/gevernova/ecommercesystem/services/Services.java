package com.gevernova.ecommerce.services;

import com.gevernova.ecommerce.model.Product;

public interface Services {
    void addProduct(Product product, int quantity);
    void updateProduct(Product product,int quantity);
    void deleteProduct(Product product);
}

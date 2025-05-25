package com.gevernova.ecommerce.exceptions;

public class ProductQuantityExceededException extends RuntimeException {
    public ProductQuantityExceededException(String message) {
        super(message);
    }
}

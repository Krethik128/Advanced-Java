package com.gevernova.ecommercesystem.exceptions;

public class ProductQuantityExceededException extends RuntimeException {
    public ProductQuantityExceededException(String message) {
        super(message);
    }
}

package com.gevernova.vehicelrentalsystem.exceptionhandling;

public class InvalidRatingException extends RuntimeException {
    public InvalidRatingException(String message) {
        super(message);
    }
}

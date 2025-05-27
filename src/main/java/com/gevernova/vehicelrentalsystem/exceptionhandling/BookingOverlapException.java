package com.gevernova.vehicelrentalsystem.exceptionhandling;

public class BookingOverlapException extends RuntimeException {
    public BookingOverlapException(String message) {
        super(message);
    }
}

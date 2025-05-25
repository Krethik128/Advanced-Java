package com.gevernova.vehicelrentalsystem.exceptions;

public class BookingOverlapException extends RuntimeException {
    public BookingOverlapException(String message) {
        super(message);
    }
}

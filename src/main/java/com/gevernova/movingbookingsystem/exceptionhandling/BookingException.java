package com.gevernova.movingbookingsystem.exceptionhandling;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}

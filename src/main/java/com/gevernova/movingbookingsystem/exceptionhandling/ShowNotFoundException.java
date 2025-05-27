package com.gevernova.movingbookingsystem.exceptionhandling;

public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(String message) {
        super(message);
    }
}

package com.gevernova.movingbookingsystem.exceptionhandling;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

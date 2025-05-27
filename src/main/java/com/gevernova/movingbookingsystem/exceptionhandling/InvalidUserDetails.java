package com.gevernova.movingbookingsystem.exceptionhandling;

public class InvalidUserDetails extends RuntimeException {
    public InvalidUserDetails(String message) {
        super(message);
    }
}

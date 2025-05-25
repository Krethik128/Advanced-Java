package com.gevernova.movingbookingsystem.exceptions;

public class InvalidUserDetails extends RuntimeException {
    public InvalidUserDetails(String message) {
        super(message);
    }
}

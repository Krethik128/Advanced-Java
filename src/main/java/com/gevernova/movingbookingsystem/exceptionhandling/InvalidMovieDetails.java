package com.gevernova.movingbookingsystem.exceptionhandling;

public class InvalidMovieDetails extends RuntimeException {
    public InvalidMovieDetails(String message) {
        super(message);
    }
}

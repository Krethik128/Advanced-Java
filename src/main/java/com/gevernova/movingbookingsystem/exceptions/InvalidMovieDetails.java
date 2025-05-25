package com.gevernova.movingbookingsystem.exceptions;

public class InvalidMovieDetails extends RuntimeException {
    public InvalidMovieDetails(String message) {
        super(message);
    }
}

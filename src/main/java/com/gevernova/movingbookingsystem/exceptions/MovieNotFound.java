package com.gevernova.movingbookingsystem.exceptions;

public class MovieNotFound extends RuntimeException {
    public MovieNotFound(String message) {
        super(message);
    }
}

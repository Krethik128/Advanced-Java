package com.gevernova.movingbookingsystem.exceptionhandling;

public class MovieNotFound extends RuntimeException {
    public MovieNotFound(String message) {
        super(message);
    }
}

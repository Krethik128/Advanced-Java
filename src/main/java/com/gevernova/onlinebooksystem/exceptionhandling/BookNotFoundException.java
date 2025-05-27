package com.gevernova.onlinebooksystem.exceptionhandling;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

package com.gevernova.onlinebooksystem.exceptionhandling;

public class InvalidBookDetailsException extends RuntimeException {
    public InvalidBookDetailsException(String message) {
        super(message);
    }
}

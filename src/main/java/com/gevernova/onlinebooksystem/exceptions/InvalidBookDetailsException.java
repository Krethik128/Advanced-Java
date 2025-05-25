package com.gevernova.onlinebooksystem.exceptions;

public class InvalidBookDetailsException extends RuntimeException {
    public InvalidBookDetailsException(String message) {
        super(message);
    }
}

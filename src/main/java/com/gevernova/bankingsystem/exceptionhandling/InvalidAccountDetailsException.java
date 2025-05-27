package com.gevernova.bankingsystem.exceptionhandling;

public class InvalidAccountDetailsException extends Exception {
    public InvalidAccountDetailsException(String message) {
        super(message);
    }
}

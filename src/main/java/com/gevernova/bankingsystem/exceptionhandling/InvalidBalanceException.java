package com.gevernova.bankingsystem.exceptionhandling;

public class InvalidBalanceException extends RuntimeException {
    public InvalidBalanceException(String message) {
        super(message);
    }
}

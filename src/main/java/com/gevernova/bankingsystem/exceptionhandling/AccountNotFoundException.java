package com.gevernova.bankingsystem.exceptionhandling;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

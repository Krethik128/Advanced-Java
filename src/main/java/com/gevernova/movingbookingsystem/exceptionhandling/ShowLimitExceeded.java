package com.gevernova.movingbookingsystem.exceptionhandling;

public class ShowLimitExceeded extends RuntimeException {
    public ShowLimitExceeded(String message) {
        super(message);
    }
}

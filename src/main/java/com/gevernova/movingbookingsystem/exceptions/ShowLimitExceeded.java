package com.gevernova.movingbookingsystem.exceptions;

public class ShowLimitExceeded extends RuntimeException {
    public ShowLimitExceeded(String message) {
        super(message);
    }
}

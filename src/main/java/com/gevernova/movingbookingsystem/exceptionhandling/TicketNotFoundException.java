package com.gevernova.movingbookingsystem.exceptionhandling;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}

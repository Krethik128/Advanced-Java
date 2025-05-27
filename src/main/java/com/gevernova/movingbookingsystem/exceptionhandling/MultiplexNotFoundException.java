package com.gevernova.movingbookingsystem.exceptionhandling;

public class MultiplexNotFoundException extends RuntimeException {
  public MultiplexNotFoundException(String message) {
    super(message);
  }
}

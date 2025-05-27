// src/main/java/com/gevernova/bankingsystem/service/ITransaction.java
package com.gevernova.bankingsystem.model;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;

// Interface for different transaction types (OCP)
public interface ITransaction {
    void execute() throws InvalidBalanceException, IllegalArgumentException;
    String getTransactionType();
    double getAmount();
    String getDescription(); // For printing transaction history
}
package com.gevernova.bankingsystem.model;


import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;

// This class is responsible for processing any ITransaction (OCP)
public class TransactionProcessor {
    public void process(ITransaction transaction) throws InvalidBalanceException, IllegalArgumentException {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction to process cannot be null.");
        }
        transaction.execute();
    }
}

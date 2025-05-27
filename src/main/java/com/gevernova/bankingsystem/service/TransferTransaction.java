// src/main/java/com/gevernova/bankingsystem/service/TransferTransaction.java
package com.gevernova.bankingsystem.service;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;
import com.gevernova.bankingsystem.model.Account;
import com.gevernova.bankingsystem.model.ITransaction;

public class TransferTransaction implements ITransaction {
    private final Account fromAccount;
    private final Account toAccount;
    private final double amount;

    public TransferTransaction(Account fromAccount, Account toAccount, double amount) {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Both sender and recipient accounts must not be null for transfer.");
        }
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @Override
    public void execute() throws InvalidBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }

        // Perform withdrawal from sender and deposit to recipient
        // These will throw InvalidBalanceException if rules are violated (e.g., insufficient funds)
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        System.out.println("Transferred " + amount + " from account " +
                fromAccount.getAccountNumber() + " to account " +
                toAccount.getAccountNumber());
    }

    @Override
    public String getTransactionType() {
        return "TRANSFER";
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getDescription() {
        return "Transfer of " + amount + " from " + fromAccount.getAccountNumber() +
                " to " + toAccount.getAccountNumber();
    }
}
// src/main/java/com/gevernova/bankingsystem/service/DepositTransaction.java
package com.gevernova.bankingsystem.service;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;
import com.gevernova.bankingsystem.model.Account;
import com.gevernova.bankingsystem.model.ITransaction;

public class DepositTransaction implements ITransaction {
    private final Account account;
    private final double amount;

    public DepositTransaction(Account account, double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null for deposit transaction.");
        }
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws InvalidBalanceException, IllegalArgumentException {
        // Delegate to the account's specific deposit method (LSP in action)
        account.deposit(amount);
    }

    @Override
    public String getTransactionType() {
        return "DEPOSIT";
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getDescription() {
        return "Deposit of " + amount + " to account " + account.getAccountNumber();
    }
}
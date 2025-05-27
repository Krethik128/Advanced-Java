// src/main/java/com/gevernova/bankingsystem/service/WithdrawalTransaction.java
package com.gevernova.bankingsystem.service;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;
import com.gevernova.bankingsystem.model.Account;
import com.gevernova.bankingsystem.model.ITransaction;

public class WithdrawalTransaction implements ITransaction {
    private final Account account;
    private final double amount;

    public WithdrawalTransaction(Account account, double amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null for withdrawal transaction.");
        }
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws InvalidBalanceException, IllegalArgumentException {
        // Delegate to the account's specific withdrawal method (LSP in action)
        account.withdraw(amount);
    }

    @Override
    public String getTransactionType() {
        return "WITHDRAWAL";
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getDescription() {
        return "Withdrawal of " + amount + " from account " + account.getAccountNumber();
    }
}
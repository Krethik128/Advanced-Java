package com.gevernova.bankingsystem.model;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;

public class CurrentAccount extends Account {
    private static final double MINIMUM_BALANCE_FOR_CURRENT = 2000.0;

    public CurrentAccount(String name, double initialBalance) throws IllegalArgumentException {
        super(name, AccountType.CURRENT, initialBalance);
    }


    @Override
    public void deposit(double amount) throws InvalidBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        setBalance(this.balance + amount);
        System.out.println("Deposited " + amount + " to Current Account " + getAccountNumber() + ". New balance: " + getBalance());
    }

    @Override
    public void withdraw(double amount) throws InvalidBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (!canWithdraw(amount)) { // Leverage canWithdraw logic
            throw new InvalidBalanceException("Insufficient balance for withdrawal from Current Account.");
        }
        setBalance(this.balance - amount);
        System.out.println("Withdrawn " + amount + " from Current Account " + getAccountNumber() + ". New balance: " + getBalance());
    }

    @Override
    public boolean canWithdraw(double amount) {
        // For current account, assume it cannot go below 2000
        return (this.balance - amount) >= MINIMUM_BALANCE_FOR_CURRENT;
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE_FOR_CURRENT;
    }
}
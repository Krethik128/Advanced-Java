package com.gevernova.bankingsystem.model;

import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 0;

    public SavingsAccount(String name, double initialBalance) throws IllegalArgumentException {
        super(name, AccountType.SAVINGS, initialBalance);
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Initial balance for Savings Account must be at least " + MINIMUM_BALANCE);
        }
    }

    @Override
    public void deposit(double amount) throws InvalidBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        setBalance(this.balance + amount);
        System.out.println("Deposited " + amount + " to Savings Account " + getAccountNumber() + ". New balance: " + getBalance());
    }

    @Override
    public void withdraw(double amount) throws InvalidBalanceException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (!canWithdraw(amount)) { // Leverage canWithdraw logic
            throw new InvalidBalanceException("Insufficient balance for withdrawal. Minimum balance of " + MINIMUM_BALANCE + " required.");
        }
        setBalance(this.balance - amount);
        System.out.println("Withdrawn " + amount + " from Savings Account " + getAccountNumber() + ". New balance: " + getBalance());
    }

    @Override
    public boolean canWithdraw(double amount) {
        return (this.balance - amount) >= MINIMUM_BALANCE;
    }

    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}
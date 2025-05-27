package com.gevernova.bankingsystem.model;

import com.gevernova.IDGenerator;
import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;

// Modified to be an abstract class, adhering to LSP
public abstract class Account {
    private static int nextAccountNumber = 1000;
    private final String accountNumber;
    private final String accountHolderName;
    protected double balance; // Protected for direct access/manipulation by subclasses
    protected  AccountType accountType;

    public Account(String name, AccountType accountType, double initialBalance) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        // Initial balance validation specific to abstract Account
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        this.accountHolderName = name;
        this.accountNumber = generateAccountNumber(); // Uses the shared IDGenerator
        this.accountType = accountType;
        this.balance = initialBalance;
    }

    private String generateAccountNumber() {
        return String.format("%06d", nextAccountNumber++);
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    // New common setBalance method for internal use by subclasses
    protected void setBalance(double newBalance) throws InvalidBalanceException {
        // Basic validation, more specific validation should be in deposit/withdraw of concrete classes
        if (newBalance < 0) {
            throw new InvalidBalanceException("Balance cannot be negative after operation.");
        }
        this.balance = newBalance;
    }
    public abstract void deposit(double amount) throws InvalidBalanceException, IllegalArgumentException;
    public abstract void withdraw(double amount) throws InvalidBalanceException, IllegalArgumentException;
    public abstract boolean canWithdraw(double amount);
    public abstract double getMinimumBalance();

    // Abstract methods to support withdrawal logic specific to each account type

    @Override
    public String toString() {
        return "Account [Number=" + accountNumber + ", Name=" + accountHolderName +
                ", Type=" + accountType + ", Balance=" + balance + "]";
    }



}
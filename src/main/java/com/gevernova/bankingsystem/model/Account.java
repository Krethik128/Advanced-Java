package com.gevernova.bankingsystem.model;

import com.gevernova.IDGenerator;
import com.gevernova.bankingsystem.exception.InvalidBalanceException;

public  class Account {
    private final String accountNumber;

    private final String name;
    private double balance;
    private final AccountType accountType;

    public Account(String name,AccountType accountType, double balance) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(accountType == null){
            throw new IllegalArgumentException("Account type cannot be null ");
        }
        if(balance < 0){
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.name = name;
        this.accountNumber = IDGenerator.generateID();
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getAccountHolderName() {
        return name;
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

    public void setBalance(double balance) throws InvalidBalanceException {
        if(balance < 0){
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }
}

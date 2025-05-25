package com.gevernova.bankingsystem.service;

import com.gevernova.bankingsystem.exception.InvalidBalanceException;
import com.gevernova.bankingsystem.model.Account;

public class Transaction {

    public void deposit(Account account, double amount) throws InvalidBalanceException { //
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null for deposit.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        account.setBalance(account.getBalance() + amount); //
        System.out.println("Deposited " + amount + " to account " + account.getAccountNumber() + ". New balance: " + account.getBalance());
    }

    public void withdraw(Account account,double amount) throws InvalidBalanceException{
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null for withdrawal.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (account.getBalance() < amount) {
            throw new InvalidBalanceException("Insufficient balance for withdrawal."); //
        }
        account.setBalance(account.getBalance() - amount); //
        System.out.println("Withdrawn " + amount + " from account " + account.getAccountNumber() + ". New balance: " + account.getBalance());
    }
    public void transfer(Account fromAccount, Account toAccount, double amount) throws InvalidBalanceException {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Both sender and recipient accounts must not be null for transfer.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        if (fromAccount.getBalance() < amount) {
            throw new InvalidBalanceException("Insufficient balance for transfer from account " + fromAccount.getAccountNumber());
        }

        withdraw(fromAccount, amount);
        deposit(toAccount, amount);
        System.out.println("Transferred " + amount + " from account " + fromAccount.getAccountNumber() + " to account " + toAccount.getAccountNumber());
    }
}

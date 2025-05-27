// src/main/java/com/gevernova/bankingsystem/model/Bank.java
package com.gevernova.bankingsystem.model;

import com.gevernova.bankingsystem.exceptionhandling.AccountNotFoundException;
import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;
import com.gevernova.bankingsystem.service.*; // Import all service classes
import java.util.*;
import java.util.stream.Collectors; // For stream operations if needed

public class Bank {
    private final Map<String, Account> accounts;
    private final List<ITransaction> transactionHistory; // To store history
    private final TransactionProcessor transactionProcessor; // The new processor

    public Bank() {
        this.accounts = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.transactionProcessor = new TransactionProcessor(); // Initialize the processor
    }

    public boolean isAccountPresent(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public void addAccount(Account account) throws IllegalArgumentException {
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account added: " + account.getAccountNumber() + " for " + account.getAccountHolderName() + " (Type: " + account.getAccountType() + ").");
    }

    public void removeAccount(String accountNumber) throws AccountNotFoundException {
        if (!accounts.containsKey(accountNumber)) {
            throw new AccountNotFoundException("Account does not exist in Database");
        }
        accounts.remove(accountNumber);
        System.out.println("Account " + accountNumber + " removed successfully.");
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        if (!accounts.containsKey(accountNumber)) {
            throw new AccountNotFoundException("Account " + accountNumber + " does not exist in Database");
        }
        return accounts.get(accountNumber);
    }

    public List<String> getAllAccountNumbers() {
        return accounts.keySet().stream().collect(Collectors.toList());
    }

    // New method to execute any ITransaction (OCP)
    public void executeTransaction(ITransaction transaction) throws InvalidBalanceException, IllegalArgumentException {
        transactionProcessor.process(transaction); // Use the processor
        transactionHistory.add(transaction); // Add to history
        System.out.println("Transaction completed: " + transaction.getTransactionType());
    }

    // Methods corresponding to the test cases for clarity and direct usage
    public void performDeposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidBalanceException, IllegalArgumentException {
        Account account = getAccount(accountNumber);
        ITransaction depositTransaction = new DepositTransaction(account, amount);
        executeTransaction(depositTransaction);
    }

    public void performWithdrawal(String accountNumber, double amount) throws AccountNotFoundException, InvalidBalanceException, IllegalArgumentException {
        Account account = getAccount(accountNumber);
        ITransaction withdrawalTransaction = new WithdrawalTransaction(account, amount);
        executeTransaction(withdrawalTransaction);
    }

    public void performTransfer(String fromAccountNumber, String toAccountNumber, double amount) throws AccountNotFoundException, InvalidBalanceException, IllegalArgumentException {
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);
        ITransaction transferTransaction = new TransferTransaction(fromAccount, toAccount, amount);
        executeTransaction(transferTransaction);
    }

    public void printTransactionHistory() {
        System.out.println("\n=== Transaction History ===");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (ITransaction transaction : transactionHistory) {
            System.out.println("- " + transaction.getDescription());
        }
    }

    public void printAllAccounts() {
        System.out.println("\n=== All Accounts ===");
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the bank.");
            return;
        }
        accounts.values().forEach(System.out::println);
    }
}
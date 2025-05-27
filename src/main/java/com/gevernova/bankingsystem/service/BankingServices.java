
package com.gevernova.bankingsystem.service;

import com.gevernova.bankingsystem.model.Account;
import com.gevernova.bankingsystem.exceptionhandling.InvalidBalanceException;
import com.gevernova.bankingsystem.model.ITransaction;
import com.gevernova.bankingsystem.model.TransactionProcessor;

import java.util.*;

public class BankingServices {
    private final List<ITransaction> transactionHistory;
    private final TransactionProcessor transactionProcessor;

    public BankingServices() {
        this.transactionHistory = new ArrayList<>();
        this.transactionProcessor = new TransactionProcessor();
    }

    public void deposit(Account account, double amount) throws InvalidBalanceException {
        try {
            DepositTransaction depositTransaction = new DepositTransaction(account, amount);
            // Delegate execution to TransactionProcessor, adhering to OCP
            transactionProcessor.process(depositTransaction);
            transactionHistory.add(depositTransaction);
        } catch (InvalidBalanceException e) {
            System.err.println("Deposit failed: " + e.getMessage());
            throw e; // Re-throw the original exception
        } catch (IllegalArgumentException e) {
            System.err.println("Deposit failed: " + e.getMessage());
            // Wrap IllegalArgumentException into InvalidBalanceException for consistent API
            throw new InvalidBalanceException("Deposit amount is invalid: " + e.getMessage());
        }
    }

    public void withdraw(Account account, double amount) throws InvalidBalanceException {
        try {
            WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(account, amount);
            // Delegate execution to TransactionProcessor, adhering to OCP
            transactionProcessor.process(withdrawalTransaction);
            transactionHistory.add(withdrawalTransaction);
        }  catch (InvalidBalanceException e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
            throw e; // Re-throw the original exception
        } catch (IllegalArgumentException e) {
            System.err.println("Withdrawal failed: " + e.getMessage());
            // Wrap IllegalArgumentException into InvalidBalanceException for consistent API
            throw new InvalidBalanceException("Withdrawal amount is invalid: " + e.getMessage());
        }
    }

    public void transfer(Account fromAccount, Account toAccount, double amount) throws InvalidBalanceException {
        try {
            ITransaction transferTransaction = new TransferTransaction(fromAccount, toAccount, amount);
            //transferTransaction.execute(); we can just delegate execution to TransactionProcessor
            transactionProcessor.process(transferTransaction);
            transactionHistory.add(transferTransaction);
        } catch (InvalidBalanceException e) {
            System.err.println("Transfer failed: " + e.getMessage());
            throw e;
        }
    }

    public List<ITransaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void printTransactionHistory() {
        System.out.println("\n=== Transaction History ===");
        for (ITransaction transaction : transactionHistory) {
            System.out.println(transaction.getDescription());
        }
    }
}

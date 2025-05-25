package com.gevernova.bankingsystem.model;

import com.gevernova.bankingsystem.exception.AccountNotFoundException;
import com.gevernova.bankingsystem.exception.InvalidBalanceException;
import com.gevernova.bankingsystem.service.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Bank {
    private final Map<String,Account> accounts;

    Transaction transaction;
    public Bank() {
        this.accounts = new HashMap<>();
        transaction = new Transaction();
    }

    public boolean isAccountPresent(String accountNumber) {
       return accounts.containsKey(accountNumber);
    }

    public void addAccount(Account account) throws IllegalArgumentException {
        if(!accounts.containsKey(account.getAccountNumber())){
            accounts.put(account.getAccountNumber(),account);
        }
        else{
            throw new IllegalArgumentException("Account number already exists");
        }
    }

    public void removeAccount(String accountNumber) throws IllegalArgumentException {
        if(accounts.containsKey(accountNumber)){
            accounts.remove(accountNumber);
        }else{
            throw new IllegalArgumentException("Account does not exist in Database");
        }
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        if(accounts.containsKey(accountNumber)){
            return accounts.get(accountNumber);
        }
        else{
            throw new AccountNotFoundException("Account does not exist in Database");
        }
    }

    public List<String> getAllAccountsNumbers() {
        return accounts.values()
                .stream()
                .map(Account::getAccountNumber)
                .toList();
    }

    public void makeTransaction() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your account number:");
        String accountNumber = scanner.nextLine();

        Account fromAccount = accounts.get(accountNumber); //
        if (fromAccount == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.println("What type of transaction would you like to make?");
        System.out.println("1. Deposit");
        System.out.println("2. Withdrawal");
        System.out.println("3. Transfer");
        int transactionType = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline after reading int

        try {
            switch (transactionType) {
                case 1:
                    System.out.println("Enter deposit amount:");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume leftover newline
                    transaction.deposit(fromAccount, depositAmount); // Use the modified deposit method
                    break;

                case 2:
                    System.out.println("Enter withdrawal amount:");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume leftover newline
                    transaction.withdraw(fromAccount, withdrawAmount); // Use the modified withdraw method
                    break;

                case 3:
                    System.out.println("Enter recipient account number:");
                    String toAccountNumber = scanner.nextLine();
                    Account toAccount = accounts.get(toAccountNumber); //

                    if (toAccount == null) {
                        System.out.println("Recipient account not found.");
                        break;
                    }

                    System.out.println("Enter transfer amount:");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // consume leftover newline
                    transaction.transfer(fromAccount, toAccount, transferAmount); // Use the new transfer method
                    break;

                default:
                    System.out.println("Invalid transaction type.");
                    break;
            }
        } catch (InvalidBalanceException | IllegalArgumentException e) { // Catch specific exceptions
            System.out.println("Transaction failed: " + e.getMessage()); //
        }
    }

}

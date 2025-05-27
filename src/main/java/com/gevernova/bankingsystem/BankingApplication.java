package com.gevernova.bankingsystem;
// BankingApplication.java - Proper usage example

import com.gevernova.bankingsystem.model.*;
import com.gevernova.bankingsystem.exceptionhandling.*;
import com.gevernova.bankingsystem.service.*;

import java.util.Scanner;

public class BankingApplication {
    //  main method for interactive testing, similar to your original Bank.java
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner mainScanner = new Scanner(System.in);

        try {
            Account savings = new SavingsAccount("John Doe", 1000.0);
            bank.addAccount(savings);

            Account current = new CurrentAccount("Jane Smith", 5000.0);
            bank.addAccount(current);

            System.out.println("\n--- Initial Account Balances ---");
            bank.printAllAccounts();

            while (true) {
                System.out.println("\n--- Banking System Menu ---");
                System.out.println("1. Perform Deposit");
                System.out.println("2. Perform Withdrawal");
                System.out.println("3. Perform Transfer");
                System.out.println("4. View All Accounts");
                System.out.println("5. View Transaction History");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                String choiceStr = mainScanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter account number: ");
                            String depAccNum = mainScanner.nextLine();
                            System.out.print("Enter deposit amount: ");
                            double depAmount = Double.parseDouble(mainScanner.nextLine());
                            bank.performDeposit(depAccNum, depAmount);
                            break;
                        case 2:
                            System.out.print("Enter account number: ");
                            String withAccNum = mainScanner.nextLine();
                            System.out.print("Enter withdrawal amount: ");
                            double withAmount = Double.parseDouble(mainScanner.nextLine());
                            bank.performWithdrawal(withAccNum, withAmount);
                            break;
                        case 3:
                            System.out.print("Enter sender account number: ");
                            String fromAccNum = mainScanner.nextLine();
                            System.out.print("Enter recipient account number: ");
                            String toAccNum = mainScanner.nextLine();
                            System.out.print("Enter transfer amount: ");
                            double transferAmount = Double.parseDouble(mainScanner.nextLine());
                            bank.performTransfer(fromAccNum, toAccNum, transferAmount);
                            break;
                        case 4:
                            bank.printAllAccounts();
                            break;
                        case 5:
                            bank.printTransactionHistory();
                            break;
                        case 6:
                            System.out.println("Exiting Banking System. Goodbye!");
                            mainScanner.close();
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (AccountNotFoundException | InvalidBalanceException | IllegalArgumentException e) {
                    System.out.println("Operation failed: ");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error during bank initialization: " + e.getMessage());
        } finally {
            if (mainScanner != null) {
                mainScanner.close();
            }
        }
    }
}


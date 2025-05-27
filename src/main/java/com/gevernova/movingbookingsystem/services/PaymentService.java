package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.PaymentDetails;

public class PaymentService {
        public void isAmountValid(double requiredAmount, double balance) {
            if (balance <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero");
            } else if (requiredAmount > balance ) {
                throw new IllegalArgumentException("Insufficient balance for payment. Minimum balance of " + balance + " required.");
            }
        }
        public boolean processPayment(PaymentDetails details, double requiredAmount, double balance) {
            return details != null && details.getAmount() >= requiredAmount && requiredAmount <= balance;
        }

}

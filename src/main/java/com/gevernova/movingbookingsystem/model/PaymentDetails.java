package com.gevernova.movingbookingsystem.model;

public class PaymentDetails {
    private String paymentMethod; // e.g., "Credit Card", "UPI", "Wallet"
    private double amount;
    private String transactionId;

    public PaymentDetails(String paymentMethod, double amount, String transactionId) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}

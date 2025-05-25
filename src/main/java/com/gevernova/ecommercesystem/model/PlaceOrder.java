package com.gevernova.ecommerce.model;

import com.gevernova.ecommerce.services.NotificationService;

import java.util.List;

public class PlaceOrder {

     boolean paymentStatus;

    private final List<NotificationService> notificationServices;

    // Constructor injection for notification services
    public PlaceOrder(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public boolean  isPaymentStatus(CustomerOrder order) {
        paymentStatus= order.getOrderSize() > 0;
        return paymentStatus;
    }

    public boolean placeOrder(CustomerOrder order) { // Accept an Order object directly
        if (!isPaymentStatus(order)) {
            System.out.println("Payment failed: No products in order.");
            return false;
        }
        order.getOrderDetails(); //printing order details
        // Notify all registered services
        for (NotificationService service : notificationServices) {
            service.notify(order); // Pass the order object to the notification service
        }
        System.out.println("Order placed successfully");
        return true;
    }

}

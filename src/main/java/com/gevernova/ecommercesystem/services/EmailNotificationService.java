package com.gevernova.ecommercesystem.services;

import com.gevernova.ecommercesystem.model.NotificationService;

public class EmailNotificationService implements NotificationService {

    @Override
    public void notify(CustomerOrder order) {
        System.out.println("Sending email to "+order.getName()+" at "+order.getEmail()+" for Order ID: "+order.getOrderId());
    }
}

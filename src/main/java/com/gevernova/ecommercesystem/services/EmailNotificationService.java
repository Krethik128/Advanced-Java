package com.gevernova.ecommerce.services;

import com.gevernova.ecommerce.model.CustomerOrder;

public class EmailNotificationService implements NotificationService {

    @Override
    public void notify(CustomerOrder order) {
        System.out.println("Sending email to "+order.getName()+" at "+order.getEmail()+" for Order ID: "+order.getOrderId());
    }
}

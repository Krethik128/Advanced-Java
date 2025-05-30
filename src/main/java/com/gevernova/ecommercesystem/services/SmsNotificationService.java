package com.gevernova.ecommercesystem.services;

import com.gevernova.ecommercesystem.model.NotificationService;

public class SmsNotificationService implements NotificationService {

    @Override
    public void notify(CustomerOrder order) {
        System.out.println("Sending SMS to "+order.getName()+" at "+order.getPhoneNumber()+" for Order ID: "+order.getOrderId());
    }
}

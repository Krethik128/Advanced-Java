package com.gevernova.ecommerce.services;

import com.gevernova.ecommerce.model.CustomerOrder;

public class SmsNotificationService implements NotificationService {

    @Override
    public void notify(CustomerOrder order) {
        System.out.println("Sending SMS to "+order.getName()+" at "+order.getPhoneNumber()+" for Order ID: "+order.getOrderId());
    }
}

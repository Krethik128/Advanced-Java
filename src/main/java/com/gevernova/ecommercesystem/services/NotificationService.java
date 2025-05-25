package com.gevernova.ecommerce.services;

import com.gevernova.ecommerce.model.CustomerOrder;

public interface NotificationService {
    void notify(CustomerOrder order);
}

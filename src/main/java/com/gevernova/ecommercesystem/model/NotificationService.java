package com.gevernova.ecommercesystem.model;

import com.gevernova.ecommercesystem.services.CustomerOrder;

public interface NotificationService {
    void notify(CustomerOrder order);
}

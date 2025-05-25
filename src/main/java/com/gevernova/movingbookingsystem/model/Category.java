package com.gevernova.movingbookingsystem.model;

public enum Category {
    GOLD(200),
    SILVER(150),
    PLATINUM(300);

    public final int price;

    Category(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

}

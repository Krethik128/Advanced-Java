package com.gevernova.movingbookingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Multiplex {
    private String id;
    private String name;
    private String address;
    private List<Screen> screens; // List of screens within this multiplex

    public Multiplex(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.screens = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }

    public Screen getScreenById(String screenId) {
        return screens.stream()
                .filter(s -> s.getId().equals(screenId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Multiplex{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", screens=" + screens.size() +
                '}';
    }
}
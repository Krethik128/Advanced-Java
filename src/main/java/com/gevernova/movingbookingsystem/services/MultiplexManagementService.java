package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.IDGenerator;
import com.gevernova.movingbookingsystem.model.Multiplex;
import com.gevernova.movingbookingsystem.model.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiplexManagementService {
    private Map<String, Multiplex> multiplexes = new HashMap<>(); // Multiplex ID -> Multiplex

    public MultiplexManagementService() {}

    public Multiplex addMultiplex(String name, String address) throws IllegalArgumentException {
        boolean alreadyExists = multiplexes.values().stream()
                .anyMatch(multiplex -> multiplex.getName().equals(name) ||
                        multiplex.getAddress().equalsIgnoreCase(address));
        if (alreadyExists) {
            throw new IllegalArgumentException("Multiplex with name '" + name + "' and address '" + address + "' already exists.");
        }
        String id = IDGenerator.generateUniqueId("MPX");
        Multiplex newMultiplex = new Multiplex(id, name, address);
        multiplexes.put(id, newMultiplex);
        System.out.println("Multiplex added: " + name);
        return newMultiplex;
    }

    public Multiplex getMultiplexById(String multiplexId) {
        return multiplexes.get(multiplexId);
    }

    public List<Multiplex> getAllMultiplexes() {
        return new ArrayList<>(multiplexes.values());
    }

    public void updateMultiplex(Multiplex multiplex) {
        if (multiplexes.containsKey(multiplex.getId())) {
            multiplexes.put(multiplex.getId(), multiplex);
            System.out.println("Multiplex updated: " + multiplex.getName());
        } else {
            throw new IllegalArgumentException("Multiplex not found for update.");
        }
    }

    public Screen addScreenToMultiplex(String multiplexId, String screenName, int rows, int cols) {
        Multiplex multiplex = getMultiplexById(multiplexId);
        if (multiplex == null) {
            throw new IllegalArgumentException("Multiplex with ID " + multiplexId + " not found.");
        } else if (multiplex.getScreens().stream().anyMatch(screen -> screen.getName().equals(screenName))) {
            throw new IllegalArgumentException("Screen with name '" + screenName + "' already exists in multiplex " + multiplex.getName() + ".");
        }

        String screenId = IDGenerator.generateUniqueId("SCR");
        Screen newScreen = new Screen(screenId, screenName, rows, cols);
        multiplex.addScreen(newScreen);
        System.out.println("Screen " + screenName + " added to multiplex " + multiplex.getName());
        return newScreen;
    }

    public Screen getScreenFromMultiplex(String multiplexId, String screenId) {
        Multiplex multiplex = getMultiplexById(multiplexId);
        if (multiplex == null) {
            return null;
        }
        return multiplex.getScreenById(screenId);
    }

    public boolean isMultiplexWithNameAndAddressPresent(String name, String address) {
        return multiplexes.values().stream()
                .anyMatch(multiplex -> multiplex.getName().equalsIgnoreCase(name) &&
                        multiplex.getAddress().equalsIgnoreCase(address));
    }
}
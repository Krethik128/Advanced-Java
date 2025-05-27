package com.gevernova.movingbookingsystem.services;

import com.gevernova.movingbookingsystem.model.IDGenerator;
import com.gevernova.movingbookingsystem.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, User> users; // In-memory storage for simplicity

    public UserService() {
        this.users = new HashMap<>();
    }

    public User registerUser(String username, String password, String email) {
        if(users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists: "+username);
        }
        else if (users.values().stream().anyMatch(user -> user.getUsername().equals(username) || user.getEmail().equals(email))) {
            throw new IllegalArgumentException("Email already exists: "+email);
        }
        User newUser = new User( username, password, email);
        users.put(username, newUser);
        System.out.println("User registered: " + newUser.getUsername());
        return newUser;
    }

    public User authenticateUser(String username, String password) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUserProfile(String userName) {
        return users.get(userName);
    }

    public void updateUserProfile(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            System.out.println("User profile updated for: " + user.getUsername());
        } else {
            throw new IllegalArgumentException("User not found for update.");
        }
    }

    public boolean deleteUser(String userName) {
        if (users.containsKey(userName)) {
            users.remove(userName);
            System.out.println("User " + userName + " deleted.");
            return true;
        }
        return false;
    }

    public boolean checkPassword(String userName, String password) {
        if (users.containsKey(userName)) {
            if (users.get(userName).getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserExists(String userName){
        return users.containsKey(userName);
    }
}
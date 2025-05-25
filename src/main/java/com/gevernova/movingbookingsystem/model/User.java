package com.gevernova.movingbookingsystem.model;

import com.gevernova.movingbookingsystem.exceptions.InvalidUserDetails;

import static com.gevernova.IDGenerator.generateID; // Assuming IDGenerator exists

public class User {
    private final String userId;
    private String name;
    private String email;
    private String phoneNumber;

    public User(String name, String email, String phoneNumber) throws InvalidUserDetails {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDetails("User name cannot be empty or null.");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) { // Basic email validation
            throw new InvalidUserDetails("Invalid email format.");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) { // Basic phone number validation
            throw new InvalidUserDetails("Phone number cannot be empty or null.");
        }

        this.userId = generateID();
        this.name = name.trim();
        this.email = email.trim();
        this.phoneNumber = phoneNumber.trim();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters (optional, depending on whether user details can be changed after creation)
    public void setName(String name) throws InvalidUserDetails {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidUserDetails("User name cannot be empty or null.");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) throws InvalidUserDetails {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new InvalidUserDetails("Invalid email format.");
        }
        this.email = email.trim();
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidUserDetails {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new InvalidUserDetails("Phone number cannot be empty or null.");
        }
        this.phoneNumber = phoneNumber.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}

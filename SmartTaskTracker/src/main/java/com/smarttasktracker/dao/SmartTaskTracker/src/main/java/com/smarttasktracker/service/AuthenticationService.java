package com.smarttasktracker.service;

import com.smarttasktracker.dao.UserDAO;
import com.smarttasktracker.model.User;

public class AuthenticationService {

    private UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Register new user
     */
    public boolean registerUser(User user) {
        try {
            // Check if email already exists
            if (userDAO.isEmailExists(user.getEmail())) {
                System.out.println("Email already exists");
                return false;
            }

            // Check if username already exists
            if (userDAO.isUsernameExists(user.getUsername())) {
                System.out.println("Username already exists");
                return false;
            }

            // Register user
            return userDAO.registerUser(user);

        } catch (Exception e) {
            System.err.println("Error in registerUser service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticate user with email and password
     */
    public User authenticateUser(String email, String password) {
        try {
            return userDAO.loginUser(email, password);
        } catch (Exception e) {
            System.err.println("Error in authenticateUser service: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if user exists by email
     * @param email - user's email
     * @return true if user exists, false otherwise
     */
    public boolean userExists(String email) {
        try {
            return userDAO.isEmailExists(email);
        } catch (Exception e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if email already exists
     */
    public boolean isEmailExists(String email) {
        return userDAO.isEmailExists(email);
    }

    /**
     * Check if username already exists
     */
    public boolean isUsernameExists(String username) {
        return userDAO.isUsernameExists(username);
    }
}

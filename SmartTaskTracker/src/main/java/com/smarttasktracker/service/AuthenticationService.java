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
     * @param user User object with all details
     * @return true if registration successful, false otherwise
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
     * @param email User's email
     * @param password User's password
     * @return User object if authentication successful, null otherwise
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
     * Check if email exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        return userDAO.isEmailExists(email);
    }

    /**
     * Check if username exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public boolean isUsernameExists(String username) {
        return userDAO.isUsernameExists(username);
    }
}

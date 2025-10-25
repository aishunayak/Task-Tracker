package com.smarttasktracker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    /**
     * Hash password using SHA-256 with random salt
     * @param password Plain text password
     * @return Base64 encoded salt + hash
     * @throws IllegalArgumentException if password is null or empty
     */
    public static String hashPassword(String password) {
        // Validation
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Combine salt + hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            // Return Base64 encoded string
            return Base64.getEncoder().encodeToString(combined);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verify password against stored hash
     * @param plainPassword Plain text password to verify
     * @param storedHash Stored Base64 encoded salt + hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        // Validation
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            return false;
        }
        if (storedHash == null || storedHash.trim().isEmpty()) {
            return false;
        }

        try {
            // Decode stored hash
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Check if hash is valid length (16 bytes salt + 32 bytes hash)
            if (combined.length != 48) {
                return false;
            }

            // Extract salt (first 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, 16);

            // Extract stored hash (remaining 32 bytes)
            byte[] storedPasswordHash = new byte[32];
            System.arraycopy(combined, 16, storedPasswordHash, 0, 32);

            // Hash input password with extracted salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] inputPasswordHash = md.digest(plainPassword.getBytes());

            // Compare hashes using constant-time comparison
            return MessageDigest.isEqual(storedPasswordHash, inputPasswordHash);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if password meets requirements
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // At least 8 characters
        if (password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check for at least one special character
        if (!password.matches(".*[@$!%*?&#].*")) {
            return false;
        }

        return true;
    }

    /**
     * Get password strength message
     * @param password Password to check
     * @return Error message if weak, null if strong
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }

        if (!password.matches(".*[@$!%*?&#].*")) {
            return "Password must contain at least one special character (@$!%*?&#)";
        }

        return null; // Password is strong
    }
}

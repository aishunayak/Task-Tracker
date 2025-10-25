package com.smarttasktracker.util;

public class ValidationUtil {

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate username format
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // 3-20 characters, alphanumeric and underscore only
        String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";
        return username.matches(usernameRegex);
    }

    /**
     * Validate name format
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // 2-50 characters, letters and spaces only
        String nameRegex = "^[A-Za-z\\s]{2,50}$";
        return name.matches(nameRegex);
    }

    /**
     * Validate phone number (Indian format)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // 10 digits starting with 6-9
        String phoneRegex = "^[6-9]\\d{9}$";
        return phone.matches(phoneRegex);
    }

    /**
     * Sanitize string input (remove leading/trailing spaces)
     */
    public static String sanitize(String input) {
        return input != null ? input.trim() : "";
    }

    /**
     * Check if string is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Escape HTML to prevent XSS
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}

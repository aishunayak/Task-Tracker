package com.smarttasktracker.util;

public class Constants {

    // Session Attributes
    public static final String SESSION_USER = "user";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USERNAME = "username";
    public static final String SESSION_USER_EMAIL = "userEmail";
    public static final String SESSION_USER_NAME = "userName";

    // User Roles
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    // Task Status
    public static final String TASK_STATUS_PENDING = "PENDING";
    public static final String TASK_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String TASK_STATUS_COMPLETED = "COMPLETED";

    // Task Priority
    public static final String PRIORITY_LOW = "LOW";
    public static final String PRIORITY_MEDIUM = "MEDIUM";
    public static final String PRIORITY_HIGH = "HIGH";

    // Issue Status
    public static final String ISSUE_STATUS_OPEN = "OPEN";
    public static final String ISSUE_STATUS_RESOLVED = "RESOLVED";
    public static final String ISSUE_STATUS_CLOSED = "CLOSED";

    // Issue Severity
    public static final String SEVERITY_LOW = "LOW";
    public static final String SEVERITY_MEDIUM = "MEDIUM";
    public static final String SEVERITY_HIGH = "HIGH";
    public static final String SEVERITY_CRITICAL = "CRITICAL";

    // Messages
    public static final String ERROR_LOGIN_FAILED = "Invalid username or password";
    public static final String ERROR_REGISTRATION_FAILED = "Registration failed. Please try again.";
    public static final String ERROR_USER_EXISTS = "Username or email already exists";
    public static final String SUCCESS_REGISTRATION = "Registration successful! Please login.";
    public static final String SUCCESS_LOGOUT = "You have been logged out successfully.";

    private Constants() {}
}

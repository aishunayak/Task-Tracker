package com.smarttasktracker.model;

import java.sql.Timestamp;

/**TaskAssignment Model*/
public class TaskAssignment {
    private int id;
    private int taskId;
    private int userId;
    private Timestamp assignedAt;
    private String status; // ASSIGNED, IN_PROGRESS, COMPLETED

    // User details (joined from users table)
    private String userName;
    private String userEmail;

    // Default constructor
    public TaskAssignment() {
    }

    // Constructor
    public TaskAssignment(int taskId, int userId) {
        this.taskId = taskId;
        this.userId = userId;
        this.status = "ASSIGNED";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

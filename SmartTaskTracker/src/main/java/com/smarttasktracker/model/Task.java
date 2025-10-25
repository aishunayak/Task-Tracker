package com.smarttasktracker.model;

import java.sql.Timestamp;

public class Task {
    private int id;
    private String title;
    private String description;
    private String filePath;
    private Timestamp startDate;
    private Timestamp endDate;
    private String assignmentType; // SELF, INDIVIDUAL, GROUP
    private Integer createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String status; // PENDING, ONGOING, COMPLETED, OVERDUE, SCHEDULED
    private String priority; // LOW, MEDIUM, HIGH
    private int points;
    private boolean isTrashed;

    // Creator details (joined from users table)
    private String creatorName;
    private String creatorEmail;

    public Task() {
    }

    // Constructor with essential fields
    public Task(String title, String description, String filePath, Timestamp startDate,
                Timestamp endDate, String assignmentType, int createdBy, String priority) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignmentType = assignmentType;
        this.createdBy = createdBy;
        this.priority = priority;
        this.status = "PENDING";
        this.points = 0;
        this.isTrashed = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isTrashed() {
        return isTrashed;
    }

    public void setTrashed(boolean trashed) {
        isTrashed = trashed;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
}

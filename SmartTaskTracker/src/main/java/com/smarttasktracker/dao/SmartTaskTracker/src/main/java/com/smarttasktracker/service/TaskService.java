package com.smarttasktracker.service;

import com.smarttasktracker.dao.TaskDAO;
import com.smarttasktracker.model.Task;

import java.sql.Timestamp;
import java.util.List;

public class TaskService {

    private TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAO();
    }

   //get today ,ongoing, sheduled,complete,with issue
    public List<Task> getTodaysTasks(int userId) {
        return taskDAO.getTodaysTasks(userId);
    }

    public List<Task> getOngoingTasks(int userId) {
        return taskDAO.getOngoingTasks(userId);
    }


    public List<Task> getOverdueTasks(int userId) {
        return taskDAO.getOverdueTasks(userId);
    }


    public List<Task> getScheduledTasks(int userId) {
        return taskDAO.getScheduledTasks(userId);
    }


    public List<Task> getCompletedTasks(int userId) {
        return taskDAO.getCompletedTasks(userId);
    }


    public List<Task> getOngoingTasksWithIssues(int userId) {
        return taskDAO.getOngoingTasksWithIssues(userId);
    }


    public List<Task> getUserTasks(int userId) {
        return taskDAO.getTasksByUserId(userId);
    }


    public List<Task> getTasksByStatus(int userId, String status) {
        return taskDAO.getTasksByStatus(userId, status);
    }


    public Task getTaskById(int taskId) {
        return taskDAO.getTaskById(taskId);
    }


    public boolean createTask(Task task) {
        // Validate task data
        if (!isValidTask(task)) {
            return false;
        }

        // Auto-set status based on dates
        task.setStatus(determineTaskStatus(task));

        return taskDAO.createTask(task);
    }

    /**
     * Update existing task with validation
     */
    public boolean updateTask(Task task) {
        // Validate task data
        if (!isValidTask(task)) {
            return false;
        }

        // Auto-update status based on dates
        task.setStatus(determineTaskStatus(task));

        return taskDAO.updateTask(task);
    }


     // Delete task

    public boolean deleteTask(int taskId) {
        return taskDAO.deleteTask(taskId);
    }

    //complete
    public boolean markTaskAsCompleted(int taskId) {
        return taskDAO.markTaskAsCompleted(taskId);
    }

   //update
    public boolean updateTaskStatus(int taskId, String status) {
        return taskDAO.updateTaskStatus(taskId, status);
    }


    public boolean isTaskCreator(int taskId, int userId) {
        Task task = taskDAO.getTaskById(taskId);
        if (task == null) {
            return false;
        }

        Integer creatorId = task.getCreatedBy();
        return creatorId != null && creatorId.equals(userId);
    }

    /**
     * Check if user has access to task (creator or assignee)
     */
    public boolean hasTaskAccess(int taskId, int userId) {
        Task task = taskDAO.getTaskById(taskId);
        if (task == null) {
            return false;
        }

        // Check if user is creator
        if (isTaskCreator(taskId, userId)) {
            return true;
        }


        return false;
    }

    /**
     * Validate task data
     */
    private boolean isValidTask(Task task) {
        if (task == null) {
            return false;
        }

        // Check required fields
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return false;
        }

        // Validate dates
        if (task.getStartDate() != null && task.getEndDate() != null) {
            if (task.getStartDate().after(task.getEndDate())) {
                return false; // Start date cannot be after end date
            }
        }

        return true;
    }

  //status of task
    private String determineTaskStatus(Task task) {
        if (task.getStatus() != null && "COMPLETED".equals(task.getStatus())) {
            return "COMPLETED";
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        if (task.getStartDate() == null || task.getEndDate() == null) {
            return task.getStatus() != null ? task.getStatus() : "PENDING";
        }

        if (task.getStartDate().after(now)) {
            return "SCHEDULED";
        }

        if (task.getEndDate().before(now)) {
            return "OVERDUE";
        }

        if (task.getStartDate().before(now) && task.getEndDate().after(now)) {
            return "ONGOING";
        }

        return task.getStatus() != null ? task.getStatus() : "PENDING";
    }


     // Get task statistics for dashboard

    public TaskStatistics getTaskStatistics(int userId) {
        TaskStatistics stats = new TaskStatistics();

        stats.setTotalTasks(taskDAO.getTasksByUserId(userId).size());
        stats.setOngoingTasks(taskDAO.getOngoingTasks(userId).size());
        stats.setOverdueTasks(taskDAO.getOverdueTasks(userId).size());
        stats.setCompletedTasks(taskDAO.getCompletedTasks(userId).size());
        stats.setScheduledTasks(taskDAO.getScheduledTasks(userId).size());
        stats.setTasksWithIssues(taskDAO.getOngoingTasksWithIssues(userId).size());

        return stats;
    }


    //  Inner class for task statistics

    public static class TaskStatistics {
        private int totalTasks;
        private int ongoingTasks;
        private int overdueTasks;
        private int completedTasks;
        private int scheduledTasks;
        private int tasksWithIssues;

        public int getTotalTasks() { return totalTasks; }
        public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

        public int getOngoingTasks() { return ongoingTasks; }
        public void setOngoingTasks(int ongoingTasks) { this.ongoingTasks = ongoingTasks; }

        public int getOverdueTasks() { return overdueTasks; }
        public void setOverdueTasks(int overdueTasks) { this.overdueTasks = overdueTasks; }

        public int getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }

        public int getScheduledTasks() { return scheduledTasks; }
        public void setScheduledTasks(int scheduledTasks) { this.scheduledTasks = scheduledTasks; }

        public int getTasksWithIssues() { return tasksWithIssues; }
        public void setTasksWithIssues(int tasksWithIssues) { this.tasksWithIssues = tasksWithIssues; }
    }
}

package com.smarttasktracker.dao;

import com.smarttasktracker.model.Task;
import com.smarttasktracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /**
     * Get Today's Tasks (end date is today OR currently running)
     */
    public List<Task> getTodaysTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND (DATE(t.end_date) = CURDATE() " +
                "OR (DATE(t.start_date) <= CURDATE() AND DATE(t.end_date) >= CURDATE())) " +
                "ORDER BY t.end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching today's tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get Ongoing Tasks (currently running between start and end date)
     */
    public List<Task> getOngoingTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND DATE(t.start_date) <= CURDATE() " +
                "AND DATE(t.end_date) >= CURDATE() " +
                "AND t.status != 'COMPLETED' " +
                "ORDER BY t.end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ongoing tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get Overdue Tasks (end date passed and not completed)
     */
    public List<Task> getOverdueTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND DATE(t.end_date) < CURDATE() " +
                "AND t.status != 'COMPLETED' " +
                "ORDER BY t.end_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                // Auto-update status to OVERDUE
                if (!"OVERDUE".equals(task.getStatus())) {
                    updateTaskStatus(task.getId(), "OVERDUE");
                    task.setStatus("OVERDUE");
                }
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching overdue tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get Scheduled Tasks (future tasks)
     */
    public List<Task> getScheduledTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND DATE(t.start_date) > CURDATE() " +
                "ORDER BY t.start_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scheduled tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get Completed Tasks
     */
    public List<Task> getCompletedTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND t.status = 'COMPLETED' " +
                "ORDER BY t.updated_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching completed tasks: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get Ongoing Tasks With Issues
     */
    public List<Task> getOngoingTasksWithIssues(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT DISTINCT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "INNER JOIN issues i ON t.id = i.task_id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND i.is_deleted = false " +
                "AND DATE(t.start_date) <= CURDATE() " +
                "AND DATE(t.end_date) >= CURDATE() " +
                "AND t.status != 'COMPLETED' " +
                "ORDER BY t.end_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ongoing tasks with issues: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Update Task Status
     */
    public boolean updateTaskStatus(int taskId, String status) {
        String sql = "UPDATE tasks SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, taskId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating task status: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Mark Task as Completed
     */
    public boolean markTaskAsCompleted(int taskId) {
        return updateTaskStatus(taskId, "COMPLETED");
    }

    /**
     * Delete Task (Soft Delete)
     */
    public boolean deleteTask(int taskId) {
        String sql = "UPDATE tasks SET is_trashed = true, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get Task by ID with details
     */
    public Task getTaskById(int taskId) {
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE t.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching task: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Update Task
     */
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, start_date = ?, " +
                "end_date = ?, priority = ?, status = ?, file_path = ?, " +
                "updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTimestamp(3, task.getStartDate());
            stmt.setTimestamp(4, task.getEndDate());
            stmt.setString(5, task.getPriority());
            stmt.setString(6, task.getStatus());
            stmt.setString(7, task.getFilePath());
            stmt.setInt(8, task.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Extract Task from ResultSet
     */
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setFilePath(rs.getString("file_path"));
        task.setStartDate(rs.getTimestamp("start_date"));
        task.setEndDate(rs.getTimestamp("end_date"));
        task.setAssignmentType(rs.getString("assignment_type"));
        task.setCreatedBy(rs.getInt("created_by"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setPoints(rs.getInt("points"));
        task.setTrashed(rs.getBoolean("is_trashed"));
        task.setCreatorName(rs.getString("creator_name"));
        task.setCreatorEmail(rs.getString("creator_email"));
        return task;
    }
    /**
     * Get all tasks for a user (used by calendar/dashboard)
     */
    public List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching tasks for user: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    /**
     * Get tasks by status
     */
    public List<Task> getTasksByStatus(int userId, String status) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.*, u.name as creator_name, u.email as creator_email " +
                "FROM tasks t " +
                "INNER JOIN users u ON t.created_by = u.id " +
                "WHERE (t.created_by = ? OR t.id IN " +
                "(SELECT task_id FROM task_assignments WHERE user_id = ?)) " +
                "AND t.is_trashed = false " +
                "AND t.status = ? " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setString(3, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching tasks by status: " + e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }
    /**
     * Create new task
     */
    /**
     * Create new task - COMPLETE WORKING VERSION
     */
    public boolean createTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, file_path, start_date, end_date, " +
                "assignment_type, created_by, status, priority) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getFilePath());
            stmt.setTimestamp(4, task.getStartDate());
            stmt.setTimestamp(5, task.getEndDate());
            stmt.setString(6, task.getAssignmentType());
            stmt.setInt(7, task.getCreatedBy());
            stmt.setString(8, task.getStatus());
            stmt.setString(9, task.getPriority());

            int result = stmt.executeUpdate();
            System.out.println("Task created: " + (result > 0));
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }



}

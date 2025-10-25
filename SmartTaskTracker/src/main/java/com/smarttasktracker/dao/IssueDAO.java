package com.smarttasktracker.dao;

import com.smarttasktracker.model.Issue;
import com.smarttasktracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    /**
     * Get all issues for a user
     */
    public List<Issue> getIssuesByUserId(int userId) {
        List<Issue> issues = new ArrayList<>();
        String sql = "SELECT i.*, u.name as creator_name, t.title as task_title " +
                "FROM issues i " +
                "LEFT JOIN users u ON i.created_by = u.id " +
                "LEFT JOIN tasks t ON i.task_id = t.id " +
                "WHERE i.created_by = ? AND i.is_deleted = false " +
                "ORDER BY i.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                issues.add(extractIssueFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching issues: " + e.getMessage());
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * Get issues by status
     */
    public List<Issue> getIssuesByStatus(int userId, String status) {
        List<Issue> issues = new ArrayList<>();
        String sql = "SELECT i.*, u.name as creator_name, t.title as task_title " +
                "FROM issues i " +
                "LEFT JOIN users u ON i.created_by = u.id " +
                "LEFT JOIN tasks t ON i.task_id = t.id " +
                "WHERE i.created_by = ? AND i.status = ? AND i.is_deleted = false " +
                "ORDER BY i.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                issues.add(extractIssueFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching issues by status: " + e.getMessage());
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * Get issue by ID
     */
    public Issue getIssueById(int issueId) {
        String sql = "SELECT i.*, u.name as creator_name, t.title as task_title " +
                "FROM issues i " +
                "LEFT JOIN users u ON i.created_by = u.id " +
                "LEFT JOIN tasks t ON i.task_id = t.id " +
                "WHERE i.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, issueId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractIssueFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching issue: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create new issue
     */
    public boolean createIssue(Issue issue) {
        String sql = "INSERT INTO issues (issue_name, task_id, department, priority, status, " +
                "description, assigned_to, created_by, start_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, issue.getIssueName());
            stmt.setObject(2, issue.getTaskId());
            stmt.setString(3, issue.getDepartment());
            stmt.setString(4, issue.getPriority());
            stmt.setString(5, issue.getStatus());
            stmt.setString(6, issue.getDescription());
            stmt.setObject(7, issue.getAssignedTo());
            stmt.setInt(8, issue.getCreatedBy());
            stmt.setTimestamp(9, issue.getStartDate());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creating issue: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update issue
     */
    public boolean updateIssue(Issue issue) {
        String sql = "UPDATE issues SET issue_name = ?, department = ?, priority = ?, " +
                "status = ?, description = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, issue.getIssueName());
            stmt.setString(2, issue.getDepartment());
            stmt.setString(3, issue.getPriority());
            stmt.setString(4, issue.getStatus());
            stmt.setString(5, issue.getDescription());
            stmt.setInt(6, issue.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating issue: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete issue (soft delete)
     */
    public boolean deleteIssue(int issueId) {
        String sql = "UPDATE issues SET is_deleted = true WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, issueId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting issue: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Extract Issue from ResultSet
     */
    private Issue extractIssueFromResultSet(ResultSet rs) throws SQLException {
        Issue issue = new Issue();
        issue.setId(rs.getInt("id"));
        issue.setIssueName(rs.getString("issue_name"));
        issue.setTaskId(rs.getObject("task_id") != null ? rs.getInt("task_id") : null);
        issue.setTaskTitle(rs.getString("task_title"));
        issue.setDepartment(rs.getString("department"));
        issue.setPriority(rs.getString("priority"));
        issue.setStatus(rs.getString("status"));
        issue.setDescription(rs.getString("description"));
        issue.setAssignedTo(rs.getObject("assigned_to") != null ? rs.getInt("assigned_to") : null);
        issue.setCreatedBy(rs.getInt("created_by"));
        issue.setCreatorName(rs.getString("creator_name"));
        issue.setCreatedAt(rs.getTimestamp("created_at"));
        issue.setUpdatedAt(rs.getTimestamp("updated_at"));
        issue.setStartDate(rs.getTimestamp("start_date"));
        issue.setDeleted(rs.getBoolean("is_deleted"));
        return issue;
    }
    /**
     * Close issue permanently - updates status to CLOSE
     */
    public boolean closeIssue(int issueId) {
        String sql = "UPDATE issues SET status = 'CLOSE' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, issueId);
            int rowsUpdated = stmt.executeUpdate();

            System.out.println("✅ Issue " + issueId + " closed successfully. Rows updated: " + rowsUpdated);
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error closing issue: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



}

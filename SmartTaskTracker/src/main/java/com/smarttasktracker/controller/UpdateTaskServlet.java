package com.smarttasktracker.controller;

import com.smarttasktracker.dao.TaskDAO;
import com.smarttasktracker.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/updateTask")
@MultipartConfig
public class UpdateTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void init() {
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }

        Integer currentUserId = (Integer) session.getAttribute("userId");

        try {
            // Get task ID
            String taskIdStr = request.getParameter("taskId");

            if (taskIdStr == null || taskIdStr.isEmpty()) {
                response.sendRedirect("tasks?error=Task ID is required");
                return;
            }

            int taskId = Integer.parseInt(taskIdStr);

            // Verify user is the creator
            Task existingTask = taskDAO.getTaskById(taskId);

            if (existingTask == null) {
                response.sendRedirect("tasks?error=Task not found");
                return;
            }

            // Check if task has creator ID
            Integer taskCreatorId = existingTask.getCreatedBy();

            if (taskCreatorId == null) {
                response.sendRedirect("tasks?error=Task creator not found");
                return;
            }

            // Compare creator ID with current user
            if (!taskCreatorId.equals(currentUserId)) {
                response.sendRedirect("tasks?error=Only task creator can edit this task");
                return;
            }

            // Get form data
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String priority = request.getParameter("priority");
            String status = request.getParameter("status");

            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                response.sendRedirect("tasks?error=Task title is required");
                return;
            }

            // Create updated task object
            Task task = new Task();
            task.setId(taskId);
            task.setTitle(title.trim());
            task.setDescription(description);
            task.setPriority(priority != null ? priority : "MEDIUM");
            task.setStatus(status != null ? status : "PENDING");

            // Parse dates
            try {
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    task.setStartDate(Timestamp.valueOf(startDateStr.replace("T", " ") + ":00"));
                }

                if (endDateStr != null && !endDateStr.isEmpty()) {
                    task.setEndDate(Timestamp.valueOf(endDateStr.replace("T", " ") + ":00"));
                }
            } catch (IllegalArgumentException e) {
                response.sendRedirect("tasks?error=Invalid date format");
                return;
            }

            // Handle file upload
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getFileName(filePart);

                if (fileName != null && !fileName.isEmpty()) {
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                    String filePath = UPLOAD_DIR + File.separator + uniqueFileName;

                    filePart.write(uploadPath + File.separator + uniqueFileName);
                    task.setFilePath(filePath);
                } else {
                    task.setFilePath(existingTask.getFilePath());
                }
            } else {
                // Keep existing file
                task.setFilePath(existingTask.getFilePath());
            }

            // Update task
            boolean success = taskDAO.updateTask(task);

            if (success) {
                response.sendRedirect("tasks?success=Task updated successfully");
            } else {
                response.sendRedirect("tasks?error=Failed to update task");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid task ID format: " + e.getMessage());
            response.sendRedirect("tasks?error=Invalid task ID");
        } catch (Exception e) {
            System.err.println("Error updating task: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("tasks?error=An error occurred while updating task");
        }
    }

    /**
     * Extract filename from Part header
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return null;

        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}

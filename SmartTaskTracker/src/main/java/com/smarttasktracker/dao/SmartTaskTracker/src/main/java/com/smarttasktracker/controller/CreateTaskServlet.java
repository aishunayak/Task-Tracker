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

@WebServlet("/createTask")
@MultipartConfig
public class CreateTaskServlet extends HttpServlet {

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

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            // Getting form data
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String priority = request.getParameter("priority");
            String assignmentType = request.getParameter("assignmentType");

            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                response.sendRedirect("tasks?error=Task title is required");
                return;
            }

            if (assignmentType == null || assignmentType.isEmpty()) {
                response.sendRedirect("tasks?error=Assignment type is required");
                return;
            }

            // Creating task object here
            Task task = new Task();
            task.setTitle(title.trim());
            task.setDescription(description);
            task.setPriority(priority != null ? priority : "MEDIUM");
            task.setAssignmentType(assignmentType);
            task.setCreatedBy(userId);
            task.setStatus("PENDING");

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

            // this is for file upload
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
                }
            }

            // Create task
            boolean success = taskDAO.createTask(task);

            if (success) {
                response.sendRedirect("tasks?success=Task created successfully");
            } else {
                response.sendRedirect("tasks?error=Failed to create task");
            }

        } catch (Exception e) {
            System.err.println("Error creating task: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("tasks?error=An error occurred while creating task");
        }
    }


     //Extract filename from Part header
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

package com.smarttasktracker.controller;

import com.smarttasktracker.dao.TaskDAO;
import com.smarttasktracker.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;  // ADD THIS IMPORT
import java.text.SimpleDateFormat;

@WebServlet("/getTaskDetails")
public class GetTaskDetailsServlet extends HttpServlet {

    private TaskDAO taskDAO;

    @Override
    public void init() {
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            out.print("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }

        String taskIdStr = request.getParameter("taskId");

        if (taskIdStr == null || taskIdStr.isEmpty()) {
            out.print("{\"success\": false, \"message\": \"Task ID is required\"}");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdStr);
            Task task = taskDAO.getTaskById(taskId);

            if (task == null) {
                out.print("{\"success\": false, \"message\": \"Task not found\"}");
                return;
            }

            // Format dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");

            // Get filename from filepath
            String fileName = "";
            if (task.getFilePath() != null && !task.getFilePath().isEmpty()) {
                File file = new File(task.getFilePath());
                fileName = file.getName();
            }

            // Build JSON manually
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"success\": true,");
            json.append("\"task\": {");
            json.append("\"id\": ").append(task.getId()).append(",");
            json.append("\"title\": \"").append(escapeJson(task.getTitle())).append("\",");
            json.append("\"description\": \"").append(escapeJson(task.getDescription() != null ? task.getDescription() : "")).append("\",");
            json.append("\"creatorName\": \"").append(escapeJson(task.getCreatorName())).append("\",");
            json.append("\"assignmentType\": \"").append(task.getAssignmentType()).append("\",");
            json.append("\"status\": \"").append(task.getStatus()).append("\",");
            json.append("\"priority\": \"").append(task.getPriority()).append("\",");
            json.append("\"startDate\": \"").append(task.getStartDate() != null ? sdf.format(task.getStartDate()) : "N/A").append("\",");
            json.append("\"endDate\": \"").append(task.getEndDate() != null ? sdf.format(task.getEndDate()) : "N/A").append("\",");
            json.append("\"filePath\": \"").append(escapeJson(task.getFilePath() != null ? task.getFilePath() : "")).append("\",");
            json.append("\"fileName\": \"").append(escapeJson(fileName)).append("\"");
            json.append("}");
            json.append("}");

            out.print(json.toString());

        } catch (Exception e) {
            System.err.println("Error fetching task details: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error loading task details\"}");
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

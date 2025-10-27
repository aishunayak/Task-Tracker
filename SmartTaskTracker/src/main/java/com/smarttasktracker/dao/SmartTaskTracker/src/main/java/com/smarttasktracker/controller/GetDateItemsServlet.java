package com.smarttasktracker.controller;

import com.smarttasktracker.dao.TaskDAO;
import com.smarttasktracker.dao.IssueDAO;
import com.smarttasktracker.model.Task;
import com.smarttasktracker.model.Issue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/getDateItems")
public class GetDateItemsServlet extends HttpServlet {

    private TaskDAO taskDAO;
    private IssueDAO issueDAO;

    @Override
    public void init() {
        taskDAO = new TaskDAO();
        issueDAO = new IssueDAO();
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

        Integer userId = (Integer) session.getAttribute("userId");
        String date = request.getParameter("date");

        try {
            List<Task> tasks = taskDAO.getTasksByUserId(userId);
            List<Issue> issues = issueDAO.getIssuesByUserId(userId);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Build JSON response
            StringBuilder json = new StringBuilder();
            json.append("{\"success\": true, \"tasks\": [");

            boolean firstTask = true;
            for (Task task : tasks) {
                if (task.getStartDate() != null) {
                    String taskDate = dateFormat.format(task.getStartDate());
                    if (date.equals(taskDate)) {
                        if (!firstTask) json.append(",");
                        json.append("{");
                        json.append("\"id\": ").append(task.getId()).append(",");
                        json.append("\"title\": \"").append(escapeJson(task.getTitle())).append("\",");
                        json.append("\"startTime\": \"").append(new SimpleDateFormat("HH:mm").format(task.getStartDate())).append("\",");
                        json.append("\"endTime\": \"").append(task.getEndDate() != null ? new SimpleDateFormat("HH:mm").format(task.getEndDate()) : "N/A").append("\"");
                        json.append("}");
                        firstTask = false;
                    }
                }
            }

            json.append("], \"issues\": [");

            boolean firstIssue = true;
            for (Issue issue : issues) {
                if (issue.getStartDate() != null) {
                    String issueDate = dateFormat.format(issue.getStartDate());
                    if (date.equals(issueDate)) {
                        if (!firstIssue) json.append(",");
                        json.append("{");
                        json.append("\"id\": ").append(issue.getId()).append(",");
                        json.append("\"issueName\": \"").append(escapeJson(issue.getIssueName())).append("\",");
                        json.append("\"department\": \"").append(escapeJson(issue.getDepartment() != null ? issue.getDepartment() : "N/A")).append("\"");
                        json.append("}");
                        firstIssue = false;
                    }
                }
            }

            json.append("]}");

            out.print(json.toString());

        } catch (Exception e) {
            System.err.println("Error fetching date items: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error loading items\"}");
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}

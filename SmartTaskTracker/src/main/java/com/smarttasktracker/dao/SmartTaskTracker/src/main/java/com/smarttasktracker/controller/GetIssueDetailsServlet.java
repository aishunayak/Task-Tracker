package com.smarttasktracker.controller;

import com.smarttasktracker.dao.IssueDAO;
import com.smarttasktracker.model.Issue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getIssueDetails")
public class GetIssueDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            out.print("{\"error\":\"User not logged in\"}");
            out.flush();
            return;
        }

        try {
            // Get issue ID from request
            String issueIdParam = request.getParameter("issueId");

            if (issueIdParam == null || issueIdParam.isEmpty()) {
                out.print("{\"error\":\"Issue ID is missing\"}");
                out.flush();
                return;
            }

            int issueId = Integer.parseInt(issueIdParam);

            // Fetch issue details from database
            IssueDAO issueDAO = new IssueDAO();
            Issue issue = issueDAO.getIssueById(issueId);

            if (issue != null) {
                // Build JSON response manually
                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"id\":\"").append(issue.getId()).append("\",");
                json.append("\"issueName\":\"").append(escapeJson(issue.getIssueName())).append("\",");
                json.append("\"description\":\"").append(escapeJson(issue.getDescription())).append("\",");
                json.append("\"status\":\"").append(escapeJson(issue.getStatus())).append("\",");
                json.append("\"priority\":\"").append(escapeJson(issue.getPriority())).append("\",");
                json.append("\"taskTitle\":\"").append(escapeJson(issue.getTaskTitle())).append("\",");
                json.append("\"creatorName\":\"").append(escapeJson(issue.getCreatorName())).append("\"");
                json.append("}");

                String jsonResponse = json.toString();
                out.print(jsonResponse);

                System.out.println(" Issue details sent: " + jsonResponse);
            } else {
                out.print("{\"error\":\"Issue not found\"}");
                System.out.println(" Issue not found with ID: " + issueId);
            }

            out.flush();

        } catch (NumberFormatException e) {
            out.print("{\"error\":\"Invalid issue ID format\"}");
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error fetching issue details\"}");
            out.flush();
        }
    }


     //Escape special characters for JSON

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

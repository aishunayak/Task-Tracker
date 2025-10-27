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
import java.sql.Timestamp;

@WebServlet("/createIssue")
public class CreateIssueServlet extends HttpServlet {

    private IssueDAO issueDAO;

    @Override
    public void init() {
        issueDAO = new IssueDAO();
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
            String issueName = request.getParameter("issueName");
            String taskIdStr = request.getParameter("taskId");
            String department = request.getParameter("department");
            String priority = request.getParameter("priority");
            String description = request.getParameter("description");

            // Validate required fields
            if (issueName == null || issueName.trim().isEmpty()) {
                response.sendRedirect("issues?error=Issue name is required");
                return;
            }

            // here i m Creating issue object
            Issue issue = new Issue();
            issue.setIssueName(issueName.trim());

            // Set task ID if provided
            if (taskIdStr != null && !taskIdStr.isEmpty() && !"".equals(taskIdStr)) {
                issue.setTaskId(Integer.parseInt(taskIdStr));
            }

            issue.setDepartment(department);
            issue.setPriority(priority != null ? priority : "MEDIUM");
            issue.setStatus("OPEN"); // Default status
            issue.setDescription(description);
            issue.setCreatedBy(userId);
            issue.setStartDate(new Timestamp(System.currentTimeMillis()));

            //for Save to database
            boolean isCreated = issueDAO.createIssue(issue);

            if (isCreated) {
                response.sendRedirect("issues?success=Issue created successfully");
            } else {
                response.sendRedirect("issues?error=Failed to create issue");
            }

        } catch (Exception e) {
            System.err.println("Error creating issue: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("issues?error=An error occurred while creating issue");
        }
    }
}

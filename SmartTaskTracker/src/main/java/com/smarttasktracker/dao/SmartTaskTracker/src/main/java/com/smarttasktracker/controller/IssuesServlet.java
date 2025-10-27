package com.smarttasktracker.controller;

import com.smarttasktracker.dao.IssueDAO;
import com.smarttasktracker.dao.TaskDAO;
import com.smarttasktracker.model.Issue;
import com.smarttasktracker.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/issues")
public class IssuesServlet extends HttpServlet {

    private IssueDAO issueDAO;
    private TaskDAO taskDAO;

    @Override
    public void init() {
        issueDAO = new IssueDAO();
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        // Get status filter (All, Open, Close, Ignore)
        String statusFilter = request.getParameter("status");

        List<Issue> issues = new ArrayList<>();

        try {
            if (statusFilter == null || "all".equalsIgnoreCase(statusFilter)) {
                // Get all issues
                issues = issueDAO.getIssuesByUserId(userId);
            } else {
                // Get issues by status
                issues = issueDAO.getIssuesByStatus(userId, statusFilter.toUpperCase());
            }

            // Get all tasks for the dropdown in create issue modal
            List<Task> userTasks = taskDAO.getTasksByUserId(userId);

            request.setAttribute("issues", issues);
            request.setAttribute("userTasks", userTasks);
            request.setAttribute("currentStatus", statusFilter);

            request.getRequestDispatcher("issues.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading issues: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("dashboard?error=Failed to load issues");
        }
    }
}

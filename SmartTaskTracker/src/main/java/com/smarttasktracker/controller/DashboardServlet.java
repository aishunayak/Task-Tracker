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
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            TaskDAO taskDAO = new TaskDAO();
            IssueDAO issueDAO = new IssueDAO();

            // Get all tasks and issues for the user
            List<Task> allTasks = taskDAO.getTasksByUserId(userId);
            List<Issue> allIssues = issueDAO.getIssuesByUserId(userId);

            // ============ CALCULATE TASK STATISTICS ============
            Map<String, Integer> taskStats = new HashMap<>();
            int totalTasks = allTasks != null ? allTasks.size() : 0;
            int ongoing = 0, overdue = 0, completed = 0, scheduled = 0;

            if (allTasks != null) {
                for (Task task : allTasks) {
                    String status = task.getStatus();
                    if ("ONGOING".equalsIgnoreCase(status)) {
                        ongoing++;
                    }else if ("COMPLETED".equalsIgnoreCase(status)) {
                        completed++;
                    } else if ("OVERDUE".equalsIgnoreCase(status)) {
                        overdue++;
                    } else if ("SCHEDULED".equalsIgnoreCase(status)) {
                        scheduled++;
                    }
                }
            }

            taskStats.put("total", totalTasks);
            taskStats.put("ongoing", ongoing);
            taskStats.put("overdue", overdue);
            taskStats.put("completed", completed);
            taskStats.put("scheduled", scheduled);

            // ============ CALCULATE ISSUE STATISTICS ============
            Map<String, Integer> issueStats = new HashMap<>();
            int totalIssues = allIssues != null ? allIssues.size() : 0;
            int open = 0, close = 0, ignore = 0;

            if (allIssues != null) {
                for (Issue issue : allIssues) {
                    String status = issue.getStatus();
                    if ("OPEN".equalsIgnoreCase(status)) {
                        open++;
                    } else if ("CLOSE".equalsIgnoreCase(status)) {
                        close++;
                    } else if ("IGNORE".equalsIgnoreCase(status)) {
                        ignore++;
                    }
                }
            }

            issueStats.put("total", totalIssues);
            issueStats.put("open", open);
            issueStats.put("close", close);
            issueStats.put("ignore", ignore);

            // Set attributes for JSP
            request.setAttribute("taskStats", taskStats);
            request.setAttribute("issueStats", issueStats);

            // Forward to dashboard.jsp
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        }
    }
}

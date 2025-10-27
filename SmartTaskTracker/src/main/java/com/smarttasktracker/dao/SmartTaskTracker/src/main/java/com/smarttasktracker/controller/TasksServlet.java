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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tasks")
public class TasksServlet extends HttpServlet {

    private TaskDAO taskDAO;

    @Override
    public void init() {
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
        String statusFilter = request.getParameter("status");

        List<Task> tasks = new ArrayList<>();

        try {
            // Route to appropriate method based on status
            if (statusFilter == null || statusFilter.isEmpty()) {
                tasks = taskDAO.getTodaysTasks(userId);
                statusFilter = "today";
            } else {
                switch (statusFilter.toUpperCase()) {
                    case "ONGOING":
                        tasks = taskDAO.getOngoingTasks(userId);
                        break;
                    case "OVERDUE":
                        tasks = taskDAO.getOverdueTasks(userId);
                        break;
                    case "SCHEDULED":
                        tasks = taskDAO.getScheduledTasks(userId);
                        break;
                    case "COMPLETED":
                        tasks = taskDAO.getCompletedTasks(userId);
                        break;
                    case "ONGOING_WITH_ISSUE":
                        tasks = taskDAO.getOngoingTasksWithIssues(userId);
                        break;
                    default:
                        tasks = taskDAO.getTodaysTasks(userId);
                        statusFilter = "today";
                }
            }

            request.setAttribute("tasks", tasks);
            request.setAttribute("currentStatus", statusFilter);
            request.getRequestDispatcher("tasks.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("dashboard?error=Failed to load tasks");
        }
    }
}

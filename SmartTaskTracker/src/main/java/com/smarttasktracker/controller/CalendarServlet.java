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
import java.util.List;
import java.util.Calendar;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        try {
            // Get view type (day, week, month) - default to day
            String viewType = request.getParameter("view");
            if (viewType == null || viewType.isEmpty()) {
                viewType = "day";
            }

            // Get date parameters or use current date
            Calendar selectedDate = Calendar.getInstance();
            String yearParam = request.getParameter("year");
            String monthParam = request.getParameter("month");
            String dayParam = request.getParameter("day");

            if (yearParam != null && monthParam != null && dayParam != null) {
                try {
                    selectedDate.set(Calendar.YEAR, Integer.parseInt(yearParam));
                    selectedDate.set(Calendar.MONTH, Integer.parseInt(monthParam) - 1); // Month is 0-indexed
                    selectedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayParam));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid date parameters: " + e.getMessage());
                    // Use current date if parsing fails
                }
            }

            // Get all tasks and issues for the user
            List<Task> allTasks = taskDAO.getTasksByUserId(userId);
            List<Issue> allIssues = issueDAO.getIssuesByUserId(userId);

            // Set attributes for JSP
            request.setAttribute("tasks", allTasks);
            request.setAttribute("issues", allIssues);
            request.setAttribute("currentView", viewType);
            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("selectedYear", selectedDate.get(Calendar.YEAR));
            request.setAttribute("selectedMonth", selectedDate.get(Calendar.MONTH) + 1);
            request.setAttribute("selectedDay", selectedDate.get(Calendar.DAY_OF_MONTH));

            // Forward to calendar.jsp
            request.getRequestDispatcher("calendar.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error loading calendar: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("dashboard?error=Failed to load calendar");
        }
    }
}

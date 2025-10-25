package com.smarttasktracker.controller;

import com.smarttasktracker.dao.TaskDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/completeTask")
public class CompleteTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;

    @Override
    public void init() {
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            boolean success = taskDAO.markTaskAsCompleted(taskId);

            if (success) {
                out.print("{\"success\": true, \"message\": \"Task marked as completed\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Failed to complete task\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Invalid task ID\"}");
        } catch (Exception e) {
            System.err.println("Error completing task: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error completing task\"}");
        }
    }
}

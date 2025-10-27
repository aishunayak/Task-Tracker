package com.smarttasktracker.controller;

import com.smarttasktracker.dao.TaskDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/toggleTaskStatus")
public class TaskStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Check if user is logged in
        if (request.getSession().getAttribute("userId") == null) {
            out.write("{\"success\": false, \"message\": \"Not logged in\"}");
            return;
        }

        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String currentStatus = request.getParameter("currentStatus");

            TaskDAO taskDAO = new TaskDAO();

            // Toggle between COMPLETED and ONGOING
            String newStatus = "COMPLETED".equals(currentStatus) ? "ONGOING" : "COMPLETED";

            boolean success = taskDAO.updateTaskStatus(taskId, newStatus);

            if (success) {
                out.write("{\"success\": true, \"newStatus\": \"" + newStatus + "\"}");
            } else {
                out.write("{\"success\": false, \"message\": \"Update failed\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}

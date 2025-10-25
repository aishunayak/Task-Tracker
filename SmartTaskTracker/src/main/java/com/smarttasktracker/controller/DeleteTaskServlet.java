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

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {

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

        Integer currentUserId = (Integer) session.getAttribute("userId");
        String taskIdStr = request.getParameter("taskId");

        if (taskIdStr == null || taskIdStr.isEmpty()) {
            out.print("{\"success\": false, \"message\": \"Task ID is required\"}");
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdStr);

            // Verify user is the creator
            Task task = taskDAO.getTaskById(taskId);

            if (task == null) {
                out.print("{\"success\": false, \"message\": \"Task not found\"}");
                return;
            }

            // Check if task creator matches current user
            Integer taskCreatorId = task.getCreatedBy();

            if (taskCreatorId == null) {
                out.print("{\"success\": false, \"message\": \"Task creator not found\"}");
                return;
            }

            if (!taskCreatorId.equals(currentUserId)) {
                out.print("{\"success\": false, \"message\": \"Only task creator can delete this task\"}");
                return;
            }

            // Delete task
            boolean success = taskDAO.deleteTask(taskId);

            if (success) {
                out.print("{\"success\": true, \"message\": \"Task deleted successfully\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Failed to delete task\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Invalid task ID format\"}");
        } catch (Exception e) {
            System.err.println("Error deleting task: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error deleting task: " + e.getMessage() + "\"}");
        }
    }
}

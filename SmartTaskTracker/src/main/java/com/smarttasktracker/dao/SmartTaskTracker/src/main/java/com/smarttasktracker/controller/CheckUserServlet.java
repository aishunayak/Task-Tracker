package com.smarttasktracker.controller;

import com.smarttasktracker.model.User;
import com.smarttasktracker.service.TaskService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/checkUser")
public class CheckUserServlet extends HttpServlet {

    private TaskService taskService;

    @Override
    public void init() {
        taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (email == null || email.trim().isEmpty()) {
            out.print("{\"exists\": false, \"message\": \"Email is required\"}");
            return;
        }

        User user = (User) taskService.getUserTasks(Integer.parseInt(email));

        if (user != null) {
            out.print("{\"exists\": true, \"name\": \"" + user.getName() + "\", \"email\": \"" + user.getEmail() + "\"}");
        } else {
            out.print("{\"exists\": false, \"message\": \"User not found\"}");
        }

        out.flush();
    }
}

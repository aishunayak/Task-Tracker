package com.smarttasktracker.controller;

import com.smarttasktracker.model.User;
import com.smarttasktracker.service.AuthenticationService;
import com.smarttasktracker.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() {
        authService = new AuthenticationService();
    }

    // for show ligin here by get req

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    //login submsion by post

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get user input
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Check if user exists first
        if (!authService.userExists(email)) {
            request.setAttribute("error", "No user found with this email. Please signup first .");
            request.setAttribute("email", email);
            request.setAttribute("showRegisterLink", true);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Authenticate user (user exists, now check password)
        User user = authService.authenticateUser(email, password);

        if (user != null) {
            // Login successful - create session
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER, user);
            session.setAttribute(Constants.SESSION_USER_ID, user.getId());
            session.setAttribute(Constants.SESSION_USERNAME, user.getUsername());
            session.setAttribute(Constants.SESSION_USER_EMAIL, user.getEmail());
            session.setAttribute(Constants.SESSION_USER_NAME, user.getName());

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // User exists but password is wrong
            request.setAttribute("error", "Incorrect password. Please try again.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}


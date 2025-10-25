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

/**
 * LoginServlet - Handles user login with email and password
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() {
        authService = new AuthenticationService();
    }

    /**
     * GET request - Show login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward to login.jsp
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * POST request - Handle login form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validation
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email is required");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Password is required");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = authService.authenticateUser(email, password);

        if (user != null) {
            // Login successful - Create session
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER, user);
            session.setAttribute(Constants.SESSION_USER_ID, user.getId());
            session.setAttribute(Constants.SESSION_USERNAME, user.getUsername());
            session.setAttribute(Constants.SESSION_USER_EMAIL, user.getEmail());
            session.setAttribute(Constants.SESSION_USER_NAME, user.getName());

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            // Login failed
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

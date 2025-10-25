package com.smarttasktracker.controller;

import com.smarttasktracker.service.AuthenticationService;
import com.smarttasktracker.util.PasswordUtil;
import com.smarttasktracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() {
        authService = new AuthenticationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Get and sanitize form data
        String name = ValidationUtil.sanitize(request.getParameter("name"));
        String username = ValidationUtil.sanitize(request.getParameter("username"));
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String password = request.getParameter("password"); // Don't trim password
        String profession = ValidationUtil.sanitize(request.getParameter("profession"));
        String companyName = ValidationUtil.sanitize(request.getParameter("companyName"));
        String designation = ValidationUtil.sanitize(request.getParameter("designation"));
        String city = ValidationUtil.sanitize(request.getParameter("city"));

        // ============================================
        // VALIDATION
        // ============================================

        // Check required fields
        if (ValidationUtil.isEmpty(name) || ValidationUtil.isEmpty(username) ||
                ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(password) ||
                ValidationUtil.isEmpty(profession) || ValidationUtil.isEmpty(companyName) ||
                ValidationUtil.isEmpty(city)) {

            request.setAttribute("error", "All required fields must be filled");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Validate name format
        if (!ValidationUtil.isValidName(name)) {
            request.setAttribute("error", "Name should contain only letters and spaces (2-50 characters)");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Validate username format
        if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("error", "Username must be 3-20 characters (letters, numbers, underscore only)");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Invalid email format");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Validate password strength
        String passwordError = PasswordUtil.getPasswordStrengthMessage(password);
        if (passwordError != null) {
            request.setAttribute("error", passwordError);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Check if email already exists
        if (authService.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Check if username already exists
        if (authService.isUsernameExists(username)) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // ============================================
        // HASH PASSWORD (SECURE WITH SALT)
        // ============================================
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Store in session
        session.setAttribute("reg_name", name);
        session.setAttribute("reg_username", username);
        session.setAttribute("reg_email", email);
        session.setAttribute("reg_password", hashedPassword);  // Hashed with salt
        session.setAttribute("reg_profession", profession);
        session.setAttribute("reg_companyName", companyName);
        session.setAttribute("reg_designation", designation);
        session.setAttribute("reg_city", city);

        // Redirect to confirmation
        response.sendRedirect("confirmation.jsp");
    }
}

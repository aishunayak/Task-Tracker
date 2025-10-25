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

@WebServlet("/completeRegistration")
public class CompleteRegistrationServlet extends HttpServlet {

    private AuthenticationService authService;

    @Override
    public void init() {
        authService = new AuthenticationService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Get data from session
        String name = (String) session.getAttribute("reg_name");
        String username = (String) session.getAttribute("reg_username");
        String email = (String) session.getAttribute("reg_email");
        String password = (String) session.getAttribute("reg_password");
        String profession = (String) session.getAttribute("reg_profession");
        String companyName = (String) session.getAttribute("reg_companyName");
        String designation = (String) session.getAttribute("reg_designation");
        String city = (String) session.getAttribute("reg_city");

        // Create User object
        User user = new User(email, name, username, password, profession, companyName, designation, city);

        // Register user in database
        boolean isRegistered = authService.registerUser(user);

        if (isRegistered) {
            // Get the registered user (to get user ID)
            User registeredUser = authService.authenticateUser(email, password);

            if (registeredUser != null) {
                // Clear registration data
                session.removeAttribute("reg_name");
                session.removeAttribute("reg_username");
                session.removeAttribute("reg_email");
                session.removeAttribute("reg_password");
                session.removeAttribute("reg_profession");
                session.removeAttribute("reg_companyName");
                session.removeAttribute("reg_designation");
                session.removeAttribute("reg_city");

                // Set user session (auto-login)
                session.setAttribute(Constants.SESSION_USER, registeredUser);
                session.setAttribute(Constants.SESSION_USER_ID, registeredUser.getId());
                session.setAttribute(Constants.SESSION_USERNAME, registeredUser.getUsername());
                session.setAttribute(Constants.SESSION_USER_EMAIL, registeredUser.getEmail());
                session.setAttribute(Constants.SESSION_USER_NAME, registeredUser.getName());

                // Redirect to dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("login.jsp?success=Registration successful! Please login.");
            }
        } else {
            response.sendRedirect("registration.jsp?error=Registration failed. Please try again.");
        }
    }
}

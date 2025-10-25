package com.smarttasktracker.controller;

import com.smarttasktracker.service.AuthenticationService;

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

        // Get all form data
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String profession = request.getParameter("profession");
        String companyName = request.getParameter("companyName");
        String designation = request.getParameter("designation");
        String city = request.getParameter("city");

        // Validate required fields
        if (name == null || name.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                profession == null || profession.trim().isEmpty() ||
                companyName == null || companyName.trim().isEmpty() ||
                city == null || city.trim().isEmpty()) {

            request.setAttribute("error", "All required fields must be filled");
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

        // Store data in session
        session.setAttribute("reg_name", name);
        session.setAttribute("reg_username", username);
        session.setAttribute("reg_email", email);
        session.setAttribute("reg_password", password);
        session.setAttribute("reg_profession", profession);
        session.setAttribute("reg_companyName", companyName);
        session.setAttribute("reg_designation", designation);
        session.setAttribute("reg_city", city);

        // Redirect to confirmation page
        response.sendRedirect("confirmation.jsp");
    }
}
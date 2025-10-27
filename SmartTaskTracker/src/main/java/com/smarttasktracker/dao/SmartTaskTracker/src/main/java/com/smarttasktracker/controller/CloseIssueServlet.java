package com.smarttasktracker.controller;

import com.smarttasktracker.dao.IssueDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/closeIssue")
public class CloseIssueServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("CloseIssueServlet called");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("❌ User not logged in, redirecting to login");
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String issueIdParam = request.getParameter("issueId");
            System.out.println("📋 Issue ID parameter: " + issueIdParam);

            if (issueIdParam == null || issueIdParam.isEmpty()) {
                System.out.println("❌ Issue ID is null or empty");
                response.sendRedirect("issues?error=Invalid issue ID");
                return;
            }

            int issueId = Integer.parseInt(issueIdParam);
            IssueDAO issueDAO = new IssueDAO();

            boolean success = issueDAO.closeIssue(issueId);

            if (success) {
                System.out.println(" Issue closed successfully, redirecting with success message");
                response.sendRedirect("issues?success=Issue closed successfully");
            } else {
                System.out.println("Failed to close issue");
                response.sendRedirect("issues?error=Failed to close issue");
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid issue ID format: " + e.getMessage());
            response.sendRedirect("issues?error=Invalid issue ID format");
        } catch (Exception e) {
            System.err.println(" Unexpected error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("issues?error=An error occurred while closing the issue");
        }
    }
}

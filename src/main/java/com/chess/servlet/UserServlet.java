package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.chess.dao.UserDAO;
import com.chess.model.User;
import com.chess.util.SecurityUtil;
import java.sql.SQLException;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            switch (action) {
                case "/register":
                    registerUser(request, response);
                    break;
                case "/login":
                    loginUser(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User(username, email, password);
        userDAO.createUser(user);
        
        // Log the registration
        userDAO.logSystemActivity("REGISTRATION", 
            "New user registered: " + username, 
            user.getId(), 
            request.getRemoteAddr());

        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = userDAO.getUserByUsername(username);
            
            if (user != null && SecurityUtil.verifyPassword(password, user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                // Log the login activity
                String activityType = "LOGIN";
                String description = "ADMIN".equals(user.getRole()) ? 
                    "Admin user logged in" : "User logged in: " + username;
                
                userDAO.logSystemActivity(activityType, description, user.getId(), request.getRemoteAddr());
                
                // Update last login timestamp
                userDAO.updateLastLogin(user.getId());
                
                // Redirect based on user role
                if ("ADMIN".equals(user.getRole())) {
                    System.out.println("Admin user detected, redirecting to admin dashboard");
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
                }
            } else {
                // Log failed login attempt
                userDAO.logSystemActivity("LOGIN_FAILED", 
                    "Failed login attempt for username: " + username, 
                    null, 
                    request.getRemoteAddr());
                    
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=true");
        }
    }
}

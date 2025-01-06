package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.MessagingException;
import com.chess.dao.UserDAO;
import com.chess.model.User;
import com.chess.util.SecurityUtil;
import com.chess.util.EmailUtil;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/user/reset-password/*")
public class PasswordResetServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/request".equals(pathInfo)) {
            request.getRequestDispatcher("/reset-password-request.jsp").forward(request, response);
        } else if (pathInfo != null && pathInfo.length() > 1) {
            String token = pathInfo.substring(1);
            try {
                User user = userDAO.getUserByResetToken(token);
                if (user != null) {
                    request.setAttribute("token", token);
                    request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }
            response.sendRedirect(request.getContextPath() + 
                "/reset-password-request.jsp?error=Invalid or expired token");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/request".equals(pathInfo)) {
                handleResetRequest(request, response);
            } else {
                handlePasswordReset(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void handleResetRequest(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String email = request.getParameter("email");
        String token = SecurityUtil.generateResetToken();
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);
        
        try {
            userDAO.setResetToken(email, token, expiry.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            EmailUtil.sendPasswordResetEmail(email, token);
            
            response.sendRedirect(request.getContextPath() + 
                "/reset-password-request.jsp?success=Password reset instructions have been sent to your email");
        } catch (MessagingException e) {
            getServletContext().log("Error sending password reset email", e);
            response.sendRedirect(request.getContextPath() + 
                "/reset-password-request.jsp?error=Error sending email. Please try again later.");
        }
    }

    private void handlePasswordReset(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        
        if (!SecurityUtil.isStrongPassword(password)) {
            response.sendRedirect(request.getContextPath() + "/reset-password.jsp?token=" + token + 
                "&error=Password does not meet security requirements");
            return;
        }

        User user = userDAO.getUserByResetToken(token);
        if (user != null) {
            userDAO.updatePassword(user.getId(), password);
            userDAO.setResetToken(user.getEmail(), null, null);
            
            try {
                // Send confirmation email
                EmailUtil.sendPasswordResetEmail(user.getEmail(), "Password changed successfully");
                response.sendRedirect(request.getContextPath() + "/reset-password.jsp?success=true");
            } catch (MessagingException e) {
                getServletContext().log("Error sending password change confirmation email", e);
                response.sendRedirect(request.getContextPath() + 
                    "/reset-password.jsp?success=true&warning=Password reset successful but confirmation email failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + 
                "/reset-password.jsp?error=Invalid or expired token");
        }
    }
}

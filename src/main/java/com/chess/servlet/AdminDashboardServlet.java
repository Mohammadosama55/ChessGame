package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.chess.model.User;
import com.chess.dao.UserDAO;
import com.chess.dao.GameDAO;
import com.chess.dao.TournamentDAO;
import com.chess.model.AdminDashboardData;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in and is an admin
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            AdminDashboardData dashboardData = getDashboardData();
            request.setAttribute("dashboardData", dashboardData);
            request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
    
    private AdminDashboardData getDashboardData() {
        AdminDashboardData data = new AdminDashboardData();
        
        try {
            UserDAO userDAO = new UserDAO();
            GameDAO gameDAO = new GameDAO();
            TournamentDAO tournamentDAO = new TournamentDAO();
            
            // Set basic statistics
            data.setTotalUsers(userDAO.getTotalUsers());
            data.setActiveUsers(userDAO.getActiveUsersCount());
            data.setTotalGames(gameDAO.getTotalGames());
            data.setActiveTournaments(tournamentDAO.getActiveTournamentsCount());
            
            // Set recent activities
            List<AdminDashboardData.SystemActivity> activities = userDAO.getRecentActivities(10);
            data.setRecentActivities(activities);
            
            // Set recent users
            data.setRecentUsers(userDAO.getRecentUsers(5));
            
            // Set recent games
            data.setRecentGames(gameDAO.getRecentGames(5));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
}

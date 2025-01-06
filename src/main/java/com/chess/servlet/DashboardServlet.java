package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.chess.dao.UserDAO;
import com.chess.dao.GameDAO;
import com.chess.dao.TournamentDAO;
import com.chess.model.User;
import com.chess.model.UserStats;
import com.chess.model.Game;
import com.chess.model.Tournament;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private TournamentDAO tournamentDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        tournamentDAO = new TournamentDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Get user statistics
            UserStats userStats = userDAO.getUserStats(user.getId());
            request.setAttribute("userStats", userStats);

            // Get recent games
            List<Game> recentGames = gameDAO.getGamesByPlayer(user.getId());
            request.setAttribute("recentGames", recentGames);

            // Get upcoming tournaments
            List<Tournament> upcomingTournaments = tournamentDAO.getActiveTournaments();
            request.setAttribute("upcomingTournaments", upcomingTournaments);

            // Get rating history for chart
            List<Integer> ratingHistory = getRatingHistory(user.getId());
            List<String> labels = new ArrayList<>();
            for (int i = 0; i < ratingHistory.size(); i++) {
                labels.add("Game " + (i + 1));
            }

            request.setAttribute("ratingHistoryLabels", gson.toJson(labels));
            request.setAttribute("ratingHistoryData", gson.toJson(ratingHistory));

            // Get leaderboard
            List<User> leaderboard = userDAO.getLeaderboard(10);
            request.setAttribute("leaderboard", leaderboard);

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading dashboard", e);
        }
    }

    private List<Integer> getRatingHistory(int userId) throws SQLException {
        // This is a placeholder implementation
        // In a real application, you would store rating history in the database
        List<Integer> history = new ArrayList<>();
        UserStats stats = userDAO.getUserStats(userId);
        int currentRating = stats.getRating();
        
        // Generate some sample rating history
        for (int i = 0; i < 10; i++) {
            history.add(currentRating - (int)(Math.random() * 200 - 100));
        }
        history.add(currentRating);
        
        return history;
    }
}

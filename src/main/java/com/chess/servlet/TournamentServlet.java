package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.chess.dao.TournamentDAO;
import com.chess.model.Tournament;
import com.chess.model.User;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/tournament/*")
public class TournamentServlet extends HttpServlet {
    private TournamentDAO tournamentDAO;

    @Override
    public void init() throws ServletException {
        tournamentDAO = new TournamentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                listTournaments(request, response);
            } else if ("/create".equals(pathInfo)) {
                showCreateForm(request, response);
            } else {
                viewTournament(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/create".equals(pathInfo)) {
                createTournament(request, response);
            } else if ("/register".equals(pathInfo)) {
                registerForTournament(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException | ParseException e) {
            throw new ServletException("Error processing request", e);
        }
    }

    private void listTournaments(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Tournament> tournaments = tournamentDAO.getActiveTournaments();
        request.setAttribute("tournaments", tournaments);
        request.getRequestDispatcher("/tournaments.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/tournaments.jsp");
            return;
        }
        
        request.getRequestDispatcher("/create-tournament.jsp").forward(request, response);
    }

    private void createTournament(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ParseException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/tournaments.jsp");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        
        Tournament tournament = new Tournament(
            request.getParameter("name"),
            request.getParameter("description"),
            new Timestamp(dateFormat.parse(request.getParameter("startDate")).getTime()),
            new Timestamp(dateFormat.parse(request.getParameter("endDate")).getTime())
        );
        
        tournament.setMaxPlayers(Integer.parseInt(request.getParameter("maxPlayers")));
        tournamentDAO.createTournament(tournament);
        
        response.sendRedirect(request.getContextPath() + "/tournament/");
    }

    private void viewTournament(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int tournamentId = Integer.parseInt(request.getPathInfo().substring(1));
        Tournament tournament = tournamentDAO.getTournamentById(tournamentId);
        
        if (tournament == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        request.setAttribute("tournament", tournament);
        request.getRequestDispatcher("/tournament-details.jsp").forward(request, response);
    }

    private void registerForTournament(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int tournamentId = Integer.parseInt(request.getParameter("tournamentId"));
        Tournament tournament = tournamentDAO.getTournamentById(tournamentId);
        
        if (tournament != null && !tournament.isFull()) {
            tournamentDAO.registerPlayer(tournamentId, user.getId());
        }
        
        response.sendRedirect(request.getContextPath() + "/tournament/" + tournamentId);
    }
}

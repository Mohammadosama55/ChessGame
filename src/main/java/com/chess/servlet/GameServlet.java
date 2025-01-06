package com.chess.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.chess.dao.GameDAO;
import com.chess.dao.UserDAO;
import com.chess.model.Game;
import com.chess.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/game/*")
public class GameServlet extends HttpServlet {
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if ("/new".equals(pathInfo)) {
                createNewGame(request, response);
            } else if (pathInfo != null && pathInfo.startsWith("/")) {
                viewGame(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            if ("/move".equals(pathInfo)) {
                handleMove(request, response);
            } else if ("/status".equals(pathInfo)) {
                updateGameStatus(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void createNewGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int opponentId = Integer.parseInt(request.getParameter("opponent"));
        User opponent = userDAO.getUserById(opponentId);

        if (opponent == null) {
            throw new ServletException("Opponent not found");
        }

        // Randomly assign colors
        boolean isWhite = Math.random() < 0.5;
        Game game = new Game(
            isWhite ? currentUser.getId() : opponent.getId(),
            isWhite ? opponent.getId() : currentUser.getId()
        );

        gameDAO.createGame(game);

        request.setAttribute("gameId", game.getId());
        request.setAttribute("whitePlayer", userDAO.getUserById(game.getWhitePlayerId()));
        request.setAttribute("blackPlayer", userDAO.getUserById(game.getBlackPlayerId()));
        
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    private void viewGame(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int gameId = Integer.parseInt(request.getPathInfo().substring(1));
        Game game = gameDAO.getGameById(gameId);

        if (game == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("gameId", game.getId());
        request.setAttribute("whitePlayer", userDAO.getUserById(game.getWhitePlayerId()));
        request.setAttribute("blackPlayer", userDAO.getUserById(game.getBlackPlayerId()));
        
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    private void handleMove(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        JsonObject jsonRequest = gson.fromJson(request.getReader(), JsonObject.class);
        int gameId = jsonRequest.get("gameId").getAsInt();
        String pgn = jsonRequest.get("pgn").getAsString();

        Game game = gameDAO.getGameById(gameId);
        game.setPgn(pgn);
        gameDAO.updateGame(game);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);
        
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }

    private void updateGameStatus(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        JsonObject jsonRequest = gson.fromJson(request.getReader(), JsonObject.class);
        int gameId = jsonRequest.get("gameId").getAsInt();
        String status = jsonRequest.get("status").getAsString();
        String pgn = jsonRequest.get("pgn").getAsString();

        Game game = gameDAO.getGameById(gameId);
        game.setStatus(status);
        game.setPgn(pgn);
        
        if (jsonRequest.has("winner")) {
            String winner = jsonRequest.get("winner").getAsString();
            game.setWinnerId(winner.equals("white") ? game.getWhitePlayerId() : game.getBlackPlayerId());
        }
        
        if (status.equals("COMPLETED") || status.equals("DRAWN")) {
            game.setEndedAt(new Timestamp(System.currentTimeMillis()));
        }
        
        gameDAO.updateGame(game);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);
        
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}

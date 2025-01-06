package com.chess.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.chess.model.Game;
import com.chess.model.AdminDashboardData;
import com.chess.util.DatabaseConfig;

public class GameDAO {
    
    public void createGame(Game game) throws SQLException {
        String sql = "INSERT INTO games (white_player_id, black_player_id, status, pgn) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, game.getWhitePlayerId());
            pstmt.setInt(2, game.getBlackPlayerId());
            pstmt.setString(3, game.getStatus());
            pstmt.setString(4, game.getPgn());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public Game getGameById(int id) throws SQLException {
        String sql = "SELECT * FROM games WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractGameFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<Game> getGamesByPlayer(int playerId) throws SQLException {
        String sql = "SELECT * FROM games WHERE white_player_id = ? OR black_player_id = ? ORDER BY started_at DESC";
        List<Game> games = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, playerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    games.add(extractGameFromResultSet(rs));
                }
            }
        }
        return games;
    }
    
    public void updateGame(Game game) throws SQLException {
        String sql = "UPDATE games SET status = ?, winner_id = ?, pgn = ?, ended_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, game.getStatus());
            if (game.getWinnerId() != null) {
                pstmt.setInt(2, game.getWinnerId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, game.getPgn());
            pstmt.setTimestamp(4, game.getEndedAt());
            pstmt.setInt(5, game.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    // Admin Dashboard Methods
    public int getTotalGames() throws SQLException {
        String sql = "SELECT COUNT(*) FROM games";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public List<Game> getRecentGames(int limit) throws SQLException {
        String sql = "SELECT g.*, w.username as white_username, b.username as black_username " +
                    "FROM games g " +
                    "JOIN users w ON g.white_player_id = w.id " +
                    "JOIN users b ON g.black_player_id = b.id " +
                    "ORDER BY g.started_at DESC LIMIT ?";
        List<Game> games = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Game game = extractGameFromResultSet(rs);
                    // Add additional information for admin view
                    game.setWhitePlayerUsername(rs.getString("white_username"));
                    game.setBlackPlayerUsername(rs.getString("black_username"));
                    games.add(game);
                }
            }
        }
        return games;
    }
    
    public int getActiveGamesCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM games WHERE status = 'IN_PROGRESS'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Game extractGameFromResultSet(ResultSet rs) throws SQLException {
        Game game = new Game(rs.getInt("white_player_id"), rs.getInt("black_player_id"));
        game.setId(rs.getInt("id"));
        game.setStatus(rs.getString("status"));
        game.setWinnerId(rs.getInt("winner_id"));
        if (rs.wasNull()) {
            game.setWinnerId(null);
        }
        game.setPgn(rs.getString("pgn"));
        game.setStartedAt(rs.getTimestamp("started_at"));
        game.setEndedAt(rs.getTimestamp("ended_at"));
        return game;
    }
}

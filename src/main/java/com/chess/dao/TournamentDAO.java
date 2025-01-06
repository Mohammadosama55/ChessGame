package com.chess.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.chess.model.Tournament;
import com.chess.model.User;
import com.chess.model.AdminDashboardData;
import com.chess.util.DatabaseConfig;

public class TournamentDAO {
    private UserDAO userDAO;

    public TournamentDAO() {
        this.userDAO = new UserDAO();
    }

    public void createTournament(Tournament tournament) throws SQLException {
        String sql = "INSERT INTO tournaments (name, description, start_date, end_date, status, max_players) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, tournament.getName());
            pstmt.setString(2, tournament.getDescription());
            pstmt.setTimestamp(3, tournament.getStartDate());
            pstmt.setTimestamp(4, tournament.getEndDate());
            pstmt.setString(5, tournament.getStatus());
            pstmt.setInt(6, tournament.getMaxPlayers());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tournament.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Tournament getTournamentById(int id) throws SQLException {
        String sql = "SELECT * FROM tournaments WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tournament tournament = extractTournamentFromResultSet(rs);
                    tournament.setParticipants(getParticipants(id));
                    return tournament;
                }
            }
        }
        return null;
    }

    public List<Tournament> getActiveTournaments() throws SQLException {
        String sql = "SELECT * FROM tournaments WHERE status IN ('UPCOMING', 'IN_PROGRESS') ORDER BY start_date";
        List<Tournament> tournaments = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Tournament tournament = extractTournamentFromResultSet(rs);
                tournament.setParticipants(getParticipants(tournament.getId()));
                tournaments.add(tournament);
            }
        }
        return tournaments;
    }

    public void registerPlayer(int tournamentId, int userId) throws SQLException {
        String sql = "INSERT INTO tournament_participants (tournament_id, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tournamentId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    public void updateTournamentStatus(int tournamentId, String status) throws SQLException {
        String sql = "UPDATE tournaments SET status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, tournamentId);
            pstmt.executeUpdate();
        }
    }

    private List<User> getParticipants(int tournamentId) throws SQLException {
        String sql = "SELECT u.* FROM users u " +
                    "JOIN tournament_participants tp ON u.id = tp.user_id " +
                    "WHERE tp.tournament_id = ?";
        List<User> participants = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tournamentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(userDAO.extractUserFromResultSet(rs));
                }
            }
        }
        return participants;
    }

    private Tournament extractTournamentFromResultSet(ResultSet rs) throws SQLException {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getInt("id"));
        tournament.setName(rs.getString("name"));
        tournament.setDescription(rs.getString("description"));
        tournament.setStartDate(rs.getTimestamp("start_date"));
        tournament.setEndDate(rs.getTimestamp("end_date"));
        tournament.setStatus(rs.getString("status"));
        tournament.setMaxPlayers(rs.getInt("max_players"));
        tournament.setCreatedAt(rs.getTimestamp("created_at"));
        return tournament;
    }

    // Admin Dashboard Methods
    public int getActiveTournamentsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tournaments WHERE status = 'IN_PROGRESS'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public List<Tournament> getRecentTournaments(int limit) throws SQLException {
        String sql = "SELECT t.*, u.username as creator_username " +
                    "FROM tournaments t " +
                    "LEFT JOIN users u ON t.created_by = u.id " +
                    "ORDER BY t.created_at DESC LIMIT ?";
        List<Tournament> tournaments = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tournament tournament = extractTournamentFromResultSet(rs);
                    tournament.setCreatorUsername(rs.getString("creator_username"));
                    tournaments.add(tournament);
                }
            }
        }
        return tournaments;
    }
    
    public int getTotalParticipantsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tournament_participants";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}

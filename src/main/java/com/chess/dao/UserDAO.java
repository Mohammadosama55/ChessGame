package com.chess.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;
import com.chess.model.User;
import com.chess.model.UserStats;
import com.chess.model.AdminDashboardData;
import com.chess.util.DatabaseConfig;
import com.chess.util.SecurityUtil;

public class UserDAO {
    
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, rating, role) VALUES (?, ?, ?, ?, ?)";
        
        // Hash the password
        String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setInt(4, user.getRating());
            pstmt.setString(5, user.getRole());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, rating = ?, role = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getRating());
            pstmt.setString(5, user.getRole());
            pstmt.setInt(6, user.getId());
            
            pstmt.executeUpdate();
        }
    }
    
    public UserStats getUserStats(int userId) throws SQLException {
        UserStats stats = new UserStats();
        
        // Get total games and outcomes
        String sql = "SELECT " +
                    "COUNT(*) as total_games, " +
                    "SUM(CASE WHEN winner_id = ? THEN 1 ELSE 0 END) as wins, " +
                    "SUM(CASE WHEN winner_id IS NULL THEN 1 ELSE 0 END) as draws, " +
                    "SUM(CASE WHEN winner_id IS NOT NULL AND winner_id != ? THEN 1 ELSE 0 END) as losses " +
                    "FROM games WHERE white_player_id = ? OR black_player_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.setTotalGames(rs.getInt("total_games"));
                    stats.setWins(rs.getInt("wins"));
                    stats.setDraws(rs.getInt("draws"));
                    stats.setLosses(rs.getInt("losses"));
                }
            }
        }
        
        // Get tournament statistics
        sql = "SELECT " +
              "COUNT(DISTINCT t.id) as tournaments_played, " +
              "COUNT(DISTINCT CASE WHEN t.winner_id = ? THEN t.id END) as tournaments_won " +
              "FROM tournaments t " +
              "JOIN tournament_participants tp ON t.id = tp.tournament_id " +
              "WHERE tp.user_id = ? AND t.status = 'COMPLETED'";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.setTournamentsPlayed(rs.getInt("tournaments_played"));
                    stats.setTournamentsWon(rs.getInt("tournaments_won"));
                }
            }
        }
        
        // Get current rating
        User user = getUserById(userId);
        if (user != null) {
            stats.setRating(user.getRating());
        }
        
        return stats;
    }

    public List<User> getLeaderboard(int limit) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY rating DESC LIMIT ?";
        List<User> leaderboard = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    leaderboard.add(extractUserFromResultSet(rs));
                }
            }
        }
        return leaderboard;
    }
    
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    if (SecurityUtil.verifyPassword(password, user.getPassword())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        String hashedPassword = SecurityUtil.hashPassword(newPassword);
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
        }
    }

    public void setResetToken(String email, String token, String expiry) throws SQLException {
        String sql = "UPDATE users SET reset_token = ?, reset_token_expiry = ? WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            pstmt.setString(2, expiry);
            pstmt.setString(3, email);
            
            pstmt.executeUpdate();
        }
    }

    public User getUserByResetToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE reset_token = ? AND reset_token_expiry > NOW()";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }

    User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRating(rs.getInt("rating"));
        user.setRole(rs.getString("role"));
        return user;
    }

    // Admin Dashboard Methods
    public int getTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public int getActiveUsersCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE last_login > DATE_SUB(NOW(), INTERVAL 24 HOUR)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public List<User> getRecentUsers(int limit) throws SQLException {
        String sql = "SELECT * FROM users ORDER BY created_at DESC LIMIT ?";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
        }
        return users;
    }
    
    public List<AdminDashboardData.SystemActivity> getRecentActivities(int limit) throws SQLException {
        String sql = "SELECT sa.*, u.username FROM system_activities sa " +
                    "LEFT JOIN users u ON sa.user_id = u.id " +
                    "ORDER BY sa.created_at DESC LIMIT ?";
        
        List<AdminDashboardData.SystemActivity> activities = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    if (username == null) {
                        username = "System";  // Default value for system activities
                    }
                    activities.add(new AdminDashboardData.SystemActivity(
                        rs.getString("activity_type"),
                        rs.getString("description"),
                        username,
                        rs.getTimestamp("created_at").toString()
                    ));
                }
            }
        }
        return activities;
    }
    
    public void logSystemActivity(String activityType, String description, Integer userId, String ipAddress) throws SQLException {
        String sql = "INSERT INTO system_activities (activity_type, description, user_id, ip_address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, activityType);
            pstmt.setString(2, description);
            if (userId != null) {
                pstmt.setInt(3, userId);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, ipAddress);
            
            pstmt.executeUpdate();
        }
    }
}

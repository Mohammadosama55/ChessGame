package com.chess.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chess.model.User;
import com.chess.model.UserStats;
import com.chess.util.DatabaseConfig;
import com.chess.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {
    
    @Mock
    private Connection connection;
    
    @Mock
    private PreparedStatement preparedStatement;
    
    @Mock
    private ResultSet resultSet;
    
    private UserDAO userDAO;
    
    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }
    
    @Test
    void testCreateUser() throws SQLException {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRating(1200);
        user.setRole("USER");
        
        String hashedPassword = "hashedPassword123";
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class);
             MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            securityUtil.when(() -> SecurityUtil.hashPassword(user.getPassword())).thenReturn(hashedPassword);
            
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt(1)).thenReturn(1);
            
            // Act
            userDAO.createUser(user);
            
            // Assert
            verify(preparedStatement).setString(1, user.getUsername());
            verify(preparedStatement).setString(2, user.getEmail());
            verify(preparedStatement).setString(3, hashedPassword);
            verify(preparedStatement).setInt(4, user.getRating());
            verify(preparedStatement).setString(5, user.getRole());
            verify(preparedStatement).executeUpdate();
            assertEquals(1, user.getId());
        }
    }
    
    @Test
    void testGetUserById() throws SQLException {
        // Arrange
        int userId = 1;
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class)) {
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(userId);
            when(resultSet.getString("username")).thenReturn("testUser");
            when(resultSet.getString("email")).thenReturn("test@example.com");
            when(resultSet.getString("password")).thenReturn("hashedPassword");
            when(resultSet.getInt("rating")).thenReturn(1200);
            when(resultSet.getString("role")).thenReturn("USER");
            
            // Act
            User user = userDAO.getUserById(userId);
            
            // Assert
            assertNotNull(user);
            assertEquals(userId, user.getId());
            assertEquals("testUser", user.getUsername());
            verify(preparedStatement).setInt(1, userId);
        }
    }
    
    @Test
    void testGetUserByUsername() throws SQLException {
        // Arrange
        String username = "testUser";
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class)) {
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("username")).thenReturn(username);
            when(resultSet.getString("email")).thenReturn("test@example.com");
            when(resultSet.getString("password")).thenReturn("hashedPassword");
            when(resultSet.getInt("rating")).thenReturn(1200);
            when(resultSet.getString("role")).thenReturn("USER");
            
            // Act
            User user = userDAO.getUserByUsername(username);
            
            // Assert
            assertNotNull(user);
            assertEquals(username, user.getUsername());
            verify(preparedStatement).setString(1, username);
        }
    }
    
    @Test
    void testUpdateUser() throws SQLException {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setUsername("updatedUser");
        user.setEmail("updated@example.com");
        user.setPassword("newPassword");
        user.setRating(1500);
        user.setRole("ADMIN");
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class)) {
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            
            // Act
            userDAO.updateUser(user);
            
            // Assert
            verify(preparedStatement).setString(1, user.getUsername());
            verify(preparedStatement).setString(2, user.getEmail());
            verify(preparedStatement).setString(3, user.getPassword());
            verify(preparedStatement).setInt(4, user.getRating());
            verify(preparedStatement).setString(5, user.getRole());
            verify(preparedStatement).setInt(6, user.getId());
            verify(preparedStatement).executeUpdate();
        }
    }
    
    @Test
    void testGetUserStats() throws SQLException {
        // Arrange
        int userId = 1;
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class)) {
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            
            // Mock first query for game stats
            when(connection.prepareStatement(contains("COUNT(*) as total_games")))
                .thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total_games")).thenReturn(10);
            when(resultSet.getInt("wins")).thenReturn(5);
            when(resultSet.getInt("draws")).thenReturn(2);
            when(resultSet.getInt("losses")).thenReturn(3);
            
            // Mock second query for tournament stats
            PreparedStatement tournamentStmt = mock(PreparedStatement.class);
            ResultSet tournamentRs = mock(ResultSet.class);
            when(connection.prepareStatement(contains("tournaments_played")))
                .thenReturn(tournamentStmt);
            when(tournamentStmt.executeQuery()).thenReturn(tournamentRs);
            when(tournamentRs.next()).thenReturn(true);
            when(tournamentRs.getInt("tournaments_played")).thenReturn(3);
            when(tournamentRs.getInt("tournaments_won")).thenReturn(1);
            
            // Mock user query for rating
            PreparedStatement userStmt = mock(PreparedStatement.class);
            ResultSet userRs = mock(ResultSet.class);
            when(connection.prepareStatement(contains("SELECT * FROM users WHERE id = ?")))
                .thenReturn(userStmt);
            when(userStmt.executeQuery()).thenReturn(userRs);
            when(userRs.next()).thenReturn(true);
            when(userRs.getInt("rating")).thenReturn(1500);
            
            // Act
            UserStats stats = userDAO.getUserStats(userId);
            
            // Assert
            assertNotNull(stats);
            assertEquals(10, stats.getTotalGames());
            assertEquals(5, stats.getWins());
            assertEquals(2, stats.getDraws());
            assertEquals(3, stats.getLosses());
            assertEquals(3, stats.getTournamentsPlayed());
            assertEquals(1, stats.getTournamentsWon());
            assertEquals(1500, stats.getRating());
        }
    }
    
    @Test
    void testAuthenticateUser_Success() throws SQLException {
        // Arrange
        String username = "testUser";
        String password = "password123";
        String hashedPassword = "hashedPassword123";
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class);
             MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            securityUtil.when(() -> SecurityUtil.verifyPassword(password, hashedPassword)).thenReturn(true);
            
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("username")).thenReturn(username);
            when(resultSet.getString("password")).thenReturn(hashedPassword);
            
            // Act
            User user = userDAO.authenticateUser(username, password);
            
            // Assert
            assertNotNull(user);
            assertEquals(username, user.getUsername());
            verify(preparedStatement).setString(1, username);
        }
    }
    
    @Test
    void testUpdatePassword() throws SQLException {
        // Arrange
        int userId = 1;
        String newPassword = "newPassword123";
        String hashedPassword = "hashedNewPassword123";
        
        try (MockedStatic<DatabaseConfig> dbConfig = mockStatic(DatabaseConfig.class);
             MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            
            dbConfig.when(DatabaseConfig::getConnection).thenReturn(connection);
            securityUtil.when(() -> SecurityUtil.hashPassword(newPassword)).thenReturn(hashedPassword);
            
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            
            // Act
            userDAO.updatePassword(userId, newPassword);
            
            // Assert
            verify(preparedStatement).setString(1, hashedPassword);
            verify(preparedStatement).setInt(2, userId);
            verify(preparedStatement).executeUpdate();
        }
    }
}

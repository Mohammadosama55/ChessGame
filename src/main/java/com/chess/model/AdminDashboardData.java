package com.chess.model;

import java.util.List;
import java.util.Map;

public class AdminDashboardData {
    private int totalUsers;
    private int activeUsers;
    private int totalGames;
    private int activeTournaments;
    private List<User> recentUsers;
    private List<Game> recentGames;
    private Map<String, Integer> userStatistics;
    private List<SystemActivity> recentActivities;

    // Nested class for system activities
    public static class SystemActivity {
        private String activityType;
        private String description;
        private String username;
        private String timestamp;

        // Constructor
        public SystemActivity(String activityType, String description, String username, String timestamp) {
            this.activityType = activityType;
            this.description = description;
            this.username = username;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getActivityType() { return activityType; }
        public void setActivityType(String activityType) { this.activityType = activityType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    // Getters and setters
    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
    public int getActiveUsers() { return activeUsers; }
    public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
    public int getTotalGames() { return totalGames; }
    public void setTotalGames(int totalGames) { this.totalGames = totalGames; }
    public int getActiveTournaments() { return activeTournaments; }
    public void setActiveTournaments(int activeTournaments) { this.activeTournaments = activeTournaments; }
    public List<User> getRecentUsers() { return recentUsers; }
    public void setRecentUsers(List<User> recentUsers) { this.recentUsers = recentUsers; }
    public List<Game> getRecentGames() { return recentGames; }
    public void setRecentGames(List<Game> recentGames) { this.recentGames = recentGames; }
    public Map<String, Integer> getUserStatistics() { return userStatistics; }
    public void setUserStatistics(Map<String, Integer> userStatistics) { this.userStatistics = userStatistics; }
    public List<SystemActivity> getRecentActivities() { return recentActivities; }
    public void setRecentActivities(List<SystemActivity> recentActivities) { this.recentActivities = recentActivities; }
}

package com.chess.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String resetToken;
    private String resetTokenExpiry;
    private int rating;
    private String role;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rating = 1200; // Default rating
        this.role = "USER";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(String resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }
}

package com.chess.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class Tournament {
    private int id;
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
    private int maxPlayers;
    private Timestamp createdAt;
    private String creatorUsername;
    private List<User> participants;

    public Tournament() {
        this.participants = new ArrayList<>();
        this.status = "UPCOMING";
        this.maxPlayers = 8;
    }

    public Tournament(String name, String description, Timestamp startDate, Timestamp endDate) {
        this();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public List<User> getParticipants() { return participants; }
    public void setParticipants(List<User> participants) { this.participants = participants; }

    public void addParticipant(User participant) {
        if (participants.size() < maxPlayers) {
            participants.add(participant);
        }
    }

    public boolean isFull() {
        return participants.size() >= maxPlayers;
    }
}

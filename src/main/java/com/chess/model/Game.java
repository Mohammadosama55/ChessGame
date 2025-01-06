package com.chess.model;

import java.sql.Timestamp;

public class Game {
    private int id;
    private int whitePlayerId;
    private int blackPlayerId;
    private String status;
    private Integer winnerId;
    private String pgn;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private String whitePlayerUsername;
    private String blackPlayerUsername;

    public Game(int whitePlayerId, int blackPlayerId) {
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.status = "IN_PROGRESS";
        this.startedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWhitePlayerId() { return whitePlayerId; }
    public void setWhitePlayerId(int whitePlayerId) { this.whitePlayerId = whitePlayerId; }

    public int getBlackPlayerId() { return blackPlayerId; }
    public void setBlackPlayerId(int blackPlayerId) { this.blackPlayerId = blackPlayerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getWinnerId() { return winnerId; }
    public void setWinnerId(Integer winnerId) { this.winnerId = winnerId; }

    public String getPgn() { return pgn; }
    public void setPgn(String pgn) { this.pgn = pgn; }

    public Timestamp getStartedAt() { return startedAt; }
    public void setStartedAt(Timestamp startedAt) { this.startedAt = startedAt; }

    public Timestamp getEndedAt() { return endedAt; }
    public void setEndedAt(Timestamp endedAt) { this.endedAt = endedAt; }

    public String getWhitePlayerUsername() {
        return whitePlayerUsername;
    }

    public void setWhitePlayerUsername(String whitePlayerUsername) {
        this.whitePlayerUsername = whitePlayerUsername;
    }

    public String getBlackPlayerUsername() {
        return blackPlayerUsername;
    }

    public void setBlackPlayerUsername(String blackPlayerUsername) {
        this.blackPlayerUsername = blackPlayerUsername;
    }
}

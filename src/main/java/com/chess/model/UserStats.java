package com.chess.model;

public class UserStats {
    private int totalGames;
    private int wins;
    private int losses;
    private int draws;
    private int rating;
    private int tournamentsPlayed;
    private int tournamentsWon;

    public UserStats() {
        this.rating = 1200; // Default rating
    }

    // Getters and Setters
    public int getTotalGames() { return totalGames; }
    public void setTotalGames(int totalGames) { this.totalGames = totalGames; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public int getTournamentsPlayed() { return tournamentsPlayed; }
    public void setTournamentsPlayed(int tournamentsPlayed) { this.tournamentsPlayed = tournamentsPlayed; }

    public int getTournamentsWon() { return tournamentsWon; }
    public void setTournamentsWon(int tournamentsWon) { this.tournamentsWon = tournamentsWon; }

    // Calculated statistics
    public double getWinRate() {
        return totalGames > 0 ? (double) wins / totalGames * 100 : 0;
    }

    public double getTournamentWinRate() {
        return tournamentsPlayed > 0 ? (double) tournamentsWon / tournamentsPlayed * 100 : 0;
    }
}

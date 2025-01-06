-- Drop database if exists and create new one
DROP DATABASE IF EXISTS chess_game;
CREATE DATABASE chess_game;
USE chess_game;

-- Users table with password reset and security fields
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    rating INT DEFAULT 1200,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Games table to store chess matches
CREATE TABLE games (
    id INT PRIMARY KEY AUTO_INCREMENT,
    white_player_id INT NOT NULL,
    black_player_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    winner_id INT,
    pgn TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    time_control VARCHAR(50),
    FOREIGN KEY (white_player_id) REFERENCES users(id),
    FOREIGN KEY (black_player_id) REFERENCES users(id),
    FOREIGN KEY (winner_id) REFERENCES users(id)
);

-- Tournaments table
CREATE TABLE tournaments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'UPCOMING',
    type VARCHAR(20) DEFAULT 'SINGLE_ELIMINATION',
    max_players INT DEFAULT 8,
    min_rating INT DEFAULT 0,
    max_rating INT DEFAULT 3000,
    time_control VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Tournament participants
CREATE TABLE tournament_participants (
    tournament_id INT,
    user_id INT,
    status VARCHAR(20) DEFAULT 'REGISTERED',
    seed_number INT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (tournament_id, user_id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tournament matches/brackets
CREATE TABLE tournament_matches (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tournament_id INT,
    game_id INT,
    round INT NOT NULL,
    match_order INT NOT NULL,
    player1_id INT,
    player2_id INT,
    winner_id INT,
    status VARCHAR(20) DEFAULT 'PENDING',
    scheduled_time TIMESTAMP,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (player1_id) REFERENCES users(id),
    FOREIGN KEY (player2_id) REFERENCES users(id),
    FOREIGN KEY (winner_id) REFERENCES users(id)
);

-- User statistics
CREATE TABLE user_stats (
    user_id INT PRIMARY KEY,
    total_games INT DEFAULT 0,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    draws INT DEFAULT 0,
    tournaments_played INT DEFAULT 0,
    tournaments_won INT DEFAULT 0,
    highest_rating INT DEFAULT 1200,
    lowest_rating INT DEFAULT 1200,
    current_streak INT DEFAULT 0,
    best_streak INT DEFAULT 0,
    last_game_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Rating history
CREATE TABLE rating_history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    rating INT NOT NULL,
    game_id INT,
    tournament_id INT,
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

-- Game moves
CREATE TABLE game_moves (
    id INT PRIMARY KEY AUTO_INCREMENT,
    game_id INT NOT NULL,
    move_number INT NOT NULL,
    move_notation VARCHAR(10) NOT NULL,
    piece_moved VARCHAR(10) NOT NULL,
    from_square VARCHAR(2) NOT NULL,
    to_square VARCHAR(2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- System activities table for admin monitoring
CREATE TABLE system_activities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    activity_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    user_id INT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert admin user (password: Admin@123#)
INSERT INTO users (username, email, password, role, rating) 
VALUES ('admin', 'admin@chessgame.com', 
'$2a$10$3jXwgZDvEFOKXImoHV90oeYz9wqgQkK2n8rB.jO9XtVEZPF.dkYfm', 
'ADMIN', 2000);

-- Insert some sample users (password: test123)
INSERT INTO users (username, email, password, rating) VALUES 
('player1', 'player1@example.com', 
'$2a$10$NZIqEy6VZbl1UfFO9PeA8.VqGIYVURx.V4gfWeuZHBcL0sX4eVYCO', 1500),
('player2', 'player2@example.com', 
'$2a$10$NZIqEy6VZbl1UfFO9PeA8.VqGIYVURx.V4gfWeuZHBcL0sX4eVYCO', 1600),
('player3', 'player3@example.com', 
'$2a$10$NZIqEy6VZbl1UfFO9PeA8.VqGIYVURx.V4gfWeuZHBcL0sX4eVYCO', 1400);

-- Initialize user_stats for all users
INSERT INTO user_stats (user_id, total_games, wins, losses, draws)
SELECT id, 0, 0, 0, 0 FROM users;

-- Create a sample tournament
INSERT INTO tournaments (name, description, start_date, end_date, max_players, created_by)
VALUES ('Welcome Tournament', 'First tournament of the platform', 
DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), 8, 1);

-- Create triggers for statistics updates
DELIMITER //

CREATE TRIGGER after_game_insert 
AFTER INSERT ON games
FOR EACH ROW
BEGIN
    -- Update total games for both players
    UPDATE user_stats 
    SET total_games = total_games + 1,
        last_game_date = NEW.started_at
    WHERE user_id IN (NEW.white_player_id, NEW.black_player_id);
END//

CREATE TRIGGER after_game_update
AFTER UPDATE ON games
FOR EACH ROW
BEGIN
    IF NEW.status = 'COMPLETED' AND NEW.winner_id IS NOT NULL THEN
        -- Update winner stats
        UPDATE user_stats 
        SET wins = wins + 1
        WHERE user_id = NEW.winner_id;
        
        -- Update loser stats
        UPDATE user_stats 
        SET losses = losses + 1
        WHERE user_id IN (NEW.white_player_id, NEW.black_player_id)
        AND user_id != NEW.winner_id;
    END IF;
END//

CREATE TRIGGER after_user_insert 
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO system_activities (activity_type, description, user_id)
    VALUES ('USER_CREATED', CONCAT('New user created: ', NEW.username), NEW.id);
END//

CREATE TRIGGER after_user_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    IF OLD.role != NEW.role THEN
        INSERT INTO system_activities (activity_type, description, user_id)
        VALUES ('ROLE_CHANGED', 
                CONCAT('User role changed from ', OLD.role, ' to ', NEW.role, ' for user: ', NEW.username),
                NEW.id);
    END IF;
END//

DELIMITER ;

-- Create indexes for better performance
CREATE INDEX idx_games_status ON games(status);
CREATE INDEX idx_tournaments_status ON tournaments(status);
CREATE INDEX idx_rating_history_user ON rating_history(user_id);
CREATE INDEX idx_game_moves_game ON game_moves(game_id);

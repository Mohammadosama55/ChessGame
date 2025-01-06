<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chess Game</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://unpkg.com/@chrisoakman/chessboardjs@1.0.0/dist/chessboard-1.0.0.min.css">
    <style>
        .game-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
        }
        .board-container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
        }
        #board {
            width: 100%;
        }
        .game-info {
            margin-top: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .player-info {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container game-container">
        <div class="player-info">
            <div class="white-player">
                <h4>White: ${whitePlayer.username}</h4>
                <p>Rating: ${whitePlayer.rating}</p>
            </div>
            <div class="black-player">
                <h4>Black: ${blackPlayer.username}</h4>
                <p>Rating: ${blackPlayer.rating}</p>
            </div>
        </div>

        <div class="board-container">
            <div id="board"></div>
        </div>

        <div class="game-info">
            <div id="status" class="alert alert-info"></div>
            <div class="move-history">
                <h5>Move History</h5>
                <pre id="pgn"></pre>
            </div>
        </div>
    </div>

    <!-- JavaScript Dependencies -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/@chrisoakman/chessboardjs@1.0.0/dist/chessboard-1.0.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/chess.js/0.10.3/chess.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/chess-game.js"></script>
    
    <script>
        // Initialize the game with the game ID from the server
        $(document).ready(function() {
            initializeGame(${gameId});
        });
    </script>
</body>
</html>

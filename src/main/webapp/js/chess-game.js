// Initialize the chess board
let board = null;
let game = new Chess();
let currentPlayer = 'white';
let gameId = null;

function initializeGame(initialGameId) {
    gameId = initialGameId;
    
    // Initialize the chess board
    board = Chessboard('board', {
        position: 'start',
        draggable: true,
        onDragStart: onDragStart,
        onDrop: onDrop,
        onSnapEnd: onSnapEnd
    });

    updateStatus();
}

function onDragStart(source, piece, position, orientation) {
    // Don't allow piece movement if game is over
    if (game.game_over()) return false;

    // Only allow the current player to move their pieces
    if ((currentPlayer === 'white' && piece.search(/^b/) !== -1) ||
        (currentPlayer === 'black' && piece.search(/^w/) !== -1)) {
        return false;
    }
}

function onDrop(source, target) {
    // Check if the move is legal
    let move = game.move({
        from: source,
        to: target,
        promotion: 'q' // Always promote to queen for simplicity
    });

    // If illegal move, snap back
    if (move === null) return 'snapback';

    // Make move on the server
    updateGameOnServer(source, target);
    updateStatus();
}

function onSnapEnd() {
    board.position(game.fen());
}

function updateStatus() {
    let status = '';
    let moveColor = game.turn() === 'w' ? 'White' : 'Black';

    // Checkmate?
    if (game.in_checkmate()) {
        status = 'Game over, ' + moveColor + ' is in checkmate.';
        updateGameStatus('COMPLETED', moveColor === 'White' ? 'black' : 'white');
    }
    // Draw?
    else if (game.in_draw()) {
        status = 'Game over, drawn position';
        updateGameStatus('DRAWN');
    }
    // Game still on
    else {
        status = moveColor + ' to move';
        // Check?
        if (game.in_check()) {
            status += ', ' + moveColor + ' is in check';
        }
    }

    document.getElementById('status').innerHTML = status;
    document.getElementById('pgn').innerHTML = game.pgn();
}

function updateGameOnServer(source, target) {
    fetch('/chess/game/move', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            gameId: gameId,
            source: source,
            target: target,
            pgn: game.pgn()
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            console.error('Error:', data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function updateGameStatus(status, winner = null) {
    fetch('/chess/game/status', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            gameId: gameId,
            status: status,
            winner: winner,
            pgn: game.pgn()
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            console.error('Error:', data.error);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

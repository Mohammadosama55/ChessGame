<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chess Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/chart.js@3.7.0/dist/chart.min.css" rel="stylesheet">
    <style>
        .stats-card {
            transition: transform 0.2s;
            margin-bottom: 20px;
        }
        .stats-card:hover {
            transform: translateY(-5px);
        }
        .game-history {
            max-height: 400px;
            overflow-y: auto;
        }
        .rating-chart {
            height: 300px;
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <div class="row mb-4">
            <div class="col-md-8">
                <h2>Welcome, ${sessionScope.user.username}!</h2>
            </div>
            <div class="col-md-4 text-end">
                <a href="${pageContext.request.contextPath}/game/new" class="btn btn-primary">New Game</a>
            </div>
        </div>

        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card stats-card bg-primary text-white">
                    <div class="card-body">
                        <h5 class="card-title">Rating</h5>
                        <h3 class="mb-0">${userStats.rating}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-success text-white">
                    <div class="card-body">
                        <h5 class="card-title">Win Rate</h5>
                        <h3 class="mb-0"><fmt:formatNumber value="${userStats.winRate}" pattern="#.#"/>%</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-info text-white">
                    <div class="card-body">
                        <h5 class="card-title">Total Games</h5>
                        <h3 class="mb-0">${userStats.totalGames}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-warning text-white">
                    <div class="card-body">
                        <h5 class="card-title">Tournaments Won</h5>
                        <h3 class="mb-0">${userStats.tournamentsWon}</h3>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <!-- Game History -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Recent Games</h5>
                    </div>
                    <div class="card-body game-history">
                        <c:forEach var="game" items="${recentGames}">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <span class="fw-bold">${game.whitePlayer.username}</span>
                                    vs
                                    <span class="fw-bold">${game.blackPlayer.username}</span>
                                </div>
                                <div>
                                    <span class="badge bg-${game.winnerId == sessionScope.user.id ? 'success' : 
                                        (game.winnerId == null ? 'secondary' : 'danger')}">
                                        ${game.winnerId == sessionScope.user.id ? 'Won' : 
                                          (game.winnerId == null ? 'Draw' : 'Lost')}
                                    </span>
                                    <small class="text-muted ms-2">
                                        <fmt:formatDate value="${game.startedAt}" pattern="MMM dd"/>
                                    </small>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Rating Chart -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Rating History</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="ratingChart" class="rating-chart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Upcoming Tournaments -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Upcoming Tournaments</h5>
                    </div>
                    <div class="card-body">
                        <c:forEach var="tournament" items="${upcomingTournaments}">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <h6 class="mb-0">${tournament.name}</h6>
                                    <small class="text-muted">
                                        Starts: <fmt:formatDate value="${tournament.startDate}" pattern="MMM dd, yyyy HH:mm"/>
                                    </small>
                                </div>
                                <a href="${pageContext.request.contextPath}/tournament/${tournament.id}" 
                                   class="btn btn-outline-primary btn-sm">View Details</a>
                            </div>
                        </c:forEach>
                        <c:if test="${empty upcomingTournaments}">
                            <p class="text-muted mb-0">No upcoming tournaments</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.7.0/dist/chart.min.js"></script>
    <script>
        // Initialize rating history chart
        const ctx = document.getElementById('ratingChart').getContext('2d');
        const ratingChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ${ratingHistoryLabels},
                datasets: [{
                    label: 'Rating',
                    data: ${ratingHistoryData},
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: false
                    }
                }
            }
        });
    </script>
</body>
</html>

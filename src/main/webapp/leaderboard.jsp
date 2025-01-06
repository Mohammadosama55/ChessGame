<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chess Leaderboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .leaderboard-table tr {
            transition: transform 0.2s;
        }
        .leaderboard-table tr:hover {
            transform: translateX(10px);
        }
        .rank-1 {
            background-color: #ffd700 !important;
        }
        .rank-2 {
            background-color: #c0c0c0 !important;
        }
        .rank-3 {
            background-color: #cd7f32 !important;
        }
        .player-stats {
            font-size: 0.9em;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <h2 class="mb-4">Chess Leaderboard</h2>

        <div class="row">
            <!-- Top Players -->
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Top Players</h5>
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-hover mb-0 leaderboard-table">
                            <thead>
                                <tr>
                                    <th>Rank</th>
                                    <th>Player</th>
                                    <th>Rating</th>
                                    <th>Win Rate</th>
                                    <th>Games</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="player" items="${leaderboard}" varStatus="status">
                                    <tr class="${status.index < 3 ? 'rank-'.concat(status.index + 1) : ''}">
                                        <td class="align-middle">#${status.index + 1}</td>
                                        <td>
                                            <div>${player.username}</div>
                                            <div class="player-stats">
                                                Tournaments Won: ${playerStats[player.id].tournamentsWon}
                                            </div>
                                        </td>
                                        <td class="align-middle">${player.rating}</td>
                                        <td class="align-middle">
                                            <fmt:formatNumber value="${playerStats[player.id].winRate}" pattern="#.#"/>%
                                        </td>
                                        <td class="align-middle">${playerStats[player.id].totalGames}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Statistics -->
            <div class="col-md-4">
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">Your Rankings</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <h6>Overall Rank</h6>
                            <h3>#${userRank}</h3>
                        </div>
                        <div class="mb-3">
                            <h6>Rating</h6>
                            <h3>${sessionScope.user.rating}</h3>
                        </div>
                        <div>
                            <h6>Win Rate</h6>
                            <h3><fmt:formatNumber value="${userStats.winRate}" pattern="#.#"/>%</h3>
                        </div>
               
                </div>

                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">Rating Distribution</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="ratingDistribution"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.7.0/dist/chart.min.js"></script>
    <script>
        // Initialize rating distribution chart
        const ctx = document.getElementById('ratingDistribution').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ${ratingRanges},
                datasets: [{
                    label: 'Players',
                    data: ${ratingDistribution},
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    </script>
</body>
</html>

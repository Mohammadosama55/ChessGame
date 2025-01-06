<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - GameChess</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Admin Dashboard CSS -->
    <style>
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            height: 100vh;
            width: 250px;
            background-color: #2c3e50;
            padding-top: 20px;
            color: white;
        }
        .main-content {
            margin-left: 250px;
            padding: 20px;
        }
        .sidebar-link {
            color: white;
            text-decoration: none;
            padding: 10px 20px;
            display: block;
            transition: background-color 0.3s;
        }
        .sidebar-link:hover {
            background-color: #34495e;
            color: white;
        }
        .sidebar-link.active {
            background-color: #3498db;
        }
        .stats-card {
            background-color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .activity-item {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        .activity-item:last-child {
            border-bottom: none;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="text-center mb-4">
            <i class="fas fa-chess fa-3x mb-2"></i>
            <h4>Admin Panel</h4>
        </div>
        <nav>
            <a href="#" class="sidebar-link active"><i class="fas fa-tachometer-alt me-2"></i> Dashboard</a>
            <a href="users" class="sidebar-link"><i class="fas fa-users me-2"></i> Users</a>
            <a href="games" class="sidebar-link"><i class="fas fa-chess-board me-2"></i> Games</a>
            <a href="tournaments" class="sidebar-link"><i class="fas fa-trophy me-2"></i> Tournaments</a>
            <a href="reports" class="sidebar-link"><i class="fas fa-chart-bar me-2"></i> Reports</a>
            <a href="settings" class="sidebar-link"><i class="fas fa-cog me-2"></i> Settings</a>
            <a href="../logout" class="sidebar-link"><i class="fas fa-sign-out-alt me-2"></i> Logout</a>
        </nav>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="container-fluid">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Dashboard Overview</h2>
                <div class="text-muted">Last updated: <fmt:formatDate value="${dashboardData.lastUpdate}" pattern="MMM dd, yyyy HH:mm"/></div>
            </div>

            <!-- Stats Cards -->
            <div class="row">
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>${dashboardData.totalUsers}</h3>
                        <p class="text-muted mb-0">Total Users</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>${dashboardData.activeUsers}</h3>
                        <p class="text-muted mb-0">Active Users</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>${dashboardData.totalGames}</h3>
                        <p class="text-muted mb-0">Total Games</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h3>${dashboardData.activeTournaments}</h3>
                        <p class="text-muted mb-0">Active Tournaments</p>
                    </div>
                </div>
            </div>

            <!-- Recent Activities -->
            <div class="row mt-4">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Recent Activities</h5>
                        </div>
                        <div class="card-body">
                            <c:forEach items="${dashboardData.recentActivities}" var="activity">
                                <div class="activity-item">
                                    <div class="d-flex justify-content-between">
                                        <span class="text-primary">${activity.username}</span>
                                        <small class="text-muted">${activity.timestamp}</small>
                                    </div>
                                    <p class="mb-0">${activity.description}</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Recent Users</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Username</th>
                                            <th>Email</th>
                                            <th>Joined</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${dashboardData.recentUsers}" var="user">
                                            <tr>
                                                <td>${user.username}</td>
                                                <td>${user.email}</td>
                                                <td><fmt:formatDate value="${user.createdAt}" pattern="MMM dd, yyyy"/></td>
                                                <td>
                                                    <button class="btn btn-sm btn-primary">View</button>
                                                    <button class="btn btn-sm btn-danger">Block</button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

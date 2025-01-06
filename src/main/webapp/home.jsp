<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GameChess - Your Ultimate Chess Platform</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .navbar-custom {
            background-color: #2c3e50;
            padding: 1rem;
        }
        .navbar-custom .navbar-brand {
            color: #ecf0f1;
            font-size: 1.5rem;
            font-weight: bold;
        }
        .navbar-custom .nav-link {
            color: #ecf0f1 !important;
            margin: 0 10px;
            transition: color 0.3s;
        }
        .navbar-custom .nav-link:hover {
            color: #3498db !important;
        }
        .hero-section {
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('assets/images/chess-bg.jpg');
            background-size: cover;
            background-position: center;
            color: white;
            padding: 100px 0;
            text-align: center;
        }
        .feature-card {
            border: none;
            border-radius: 10px;
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .feature-icon {
            font-size: 2.5rem;
            color: #3498db;
            margin-bottom: 1rem;
        }
        footer {
            background-color: #2c3e50;
            color: white;
            padding: 2rem 0;
        }
        .profile-indicator {
            display: flex;
            align-items: center;
        }
        .profile-pic {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-custom">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-chess"></i> GameChess
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="#"><i class="fas fa-home"></i> Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="play.jsp"><i class="fas fa-gamepad"></i> Play Now</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="tournaments.jsp"><i class="fas fa-trophy"></i> Tournaments</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="leaderboard.jsp"><i class="fas fa-crown"></i> Leaderboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="learn.jsp"><i class="fas fa-book"></i> Learn</a>
                    </li>
                </ul>
                <div class="d-flex">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <div class="profile-indicator">
                                <img src="${sessionScope.user.profilePic}" alt="Profile" class="profile-pic">
                                <div class="dropdown">
                                    <button class="btn btn-outline-light dropdown-toggle" type="button" id="profileDropdown" data-bs-toggle="dropdown">
                                        ${sessionScope.user.username}
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="profile.jsp">My Profile</a></li>
                                        <li><a class="dropdown-item" href="settings.jsp">Settings</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="logout">Logout</a></li>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="login.jsp" class="btn btn-outline-light me-2">Login</a>
                            <a href="register.jsp" class="btn btn-primary">Sign Up</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <h1 class="display-4">Welcome to GameChess</h1>
            <p class="lead">Play chess online, improve your skills, and join tournaments</p>
            <a href="play.jsp" class="btn btn-primary btn-lg mt-3">Play Now</a>
        </div>
    </section>

    <!-- Features Section -->
    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">Features</h2>
            <div class="row">
                <div class="col-md-4">
                    <div class="card feature-card">
                        <div class="card-body text-center">
                            <i class="fas fa-chess-knight feature-icon"></i>
                            <h5 class="card-title">Play Online</h5>
                            <p class="card-text">Play chess against players from around the world in real-time matches.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card feature-card">
                        <div class="card-body text-center">
                            <i class="fas fa-trophy feature-icon"></i>
                            <h5 class="card-title">Tournaments</h5>
                            <p class="card-text">Participate in daily and weekly tournaments to win prizes and improve your ranking.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card feature-card">
                        <div class="card-body text-center">
                            <i class="fas fa-graduation-cap feature-icon"></i>
                            <h5 class="card-title">Learn & Improve</h5>
                            <p class="card-text">Access tutorials, puzzles, and strategies to enhance your chess skills.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="bg-light py-5">
        <div class="container">
            <div class="row text-center">
                <div class="col-md-4">
                    <h3 class="display-4">1000+</h3>
                    <p class="text-muted">Active Players</p>
                </div>
                <div class="col-md-4">
                    <h3 class="display-4">50+</h3>
                    <p class="text-muted">Daily Tournaments</p>
                </div>
                <div class="col-md-4">
                    <h3 class="display-4">10K+</h3>
                    <p class="text-muted">Games Played</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-md-4">
                    <h5>About GameChess</h5>
                    <p>Your ultimate platform for playing chess online, improving your skills, and competing in tournaments.</p>
                </div>
                <div class="col-md-4">
                    <h5>Quick Links</h5>
                    <ul class="list-unstyled">
                        <li><a href="about.jsp" class="text-light">About Us</a></li>
                        <li><a href="contact.jsp" class="text-light">Contact</a></li>
                        <li><a href="privacy.jsp" class="text-light">Privacy Policy</a></li>
                        <li><a href="terms.jsp" class="text-light">Terms of Service</a></li>
                    </ul>
                </div>
                <div class="col-md-4">
                    <h5>Connect With Us</h5>
                    <div class="social-links">
                        <a href="#" class="text-light me-3"><i class="fab fa-facebook"></i></a>
                        <a href="#" class="text-light me-3"><i class="fab fa-twitter"></i></a>
                        <a href="#" class="text-light me-3"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="text-light"><i class="fab fa-discord"></i></a>
                    </div>
                </div>
            </div>
            <hr class="mt-4 mb-4" style="background-color: white;">
            <div class="text-center">
                <p class="mb-0">&copy; 2025 GameChess. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

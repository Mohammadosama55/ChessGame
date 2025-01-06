<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chess Tournaments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .tournament-card {
            margin-bottom: 20px;
            transition: transform 0.2s;
        }
        .tournament-card:hover {
            transform: translateY(-5px);
        }
        .status-badge {
            position: absolute;
            top: 10px;
            right: 10px;
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Chess Tournaments</h2>
            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/tournament/create" class="btn btn-primary">Create Tournament</a>
            </c:if>
        </div>

        <div class="row">
            <c:forEach var="tournament" items="${tournaments}">
                <div class="col-md-6 col-lg-4">
                    <div class="card tournament-card">
                        <div class="card-body">
                            <span class="badge bg-${tournament.status == 'UPCOMING' ? 'primary' : 'success'} status-badge">
                                ${tournament.status}
                            </span>
                            <h5 class="card-title">${tournament.name}</h5>
                            <p class="card-text">${tournament.description}</p>
                            <div class="mb-3">
                                <small class="text-muted">
                                    Start: <fmt:formatDate value="${tournament.startDate}" pattern="MMM dd, yyyy HH:mm"/>
                                </small><br>
                                <small class="text-muted">
                                    End: <fmt:formatDate value="${tournament.endDate}" pattern="MMM dd, yyyy HH:mm"/>
                                </small>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="text-muted">
                                    ${tournament.participants.size()}/${tournament.maxPlayers} players
                                </span>
                                <a href="${pageContext.request.contextPath}/tournament/${tournament.id}" 
                                   class="btn btn-outline-primary">View Details</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty tournaments}">
            <div class="text-center py-5">
                <h4>No active tournaments available</h4>
                <p class="text-muted">Check back later for upcoming tournaments!</p>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

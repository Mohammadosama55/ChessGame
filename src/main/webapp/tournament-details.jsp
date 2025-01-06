<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${tournament.name} - Tournament Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .participant-card {
            transition: transform 0.2s;
        }
        .participant-card:hover {
            transform: translateY(-2px);
        }
        .tournament-info {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/tournament/">Tournaments</a></li>
                <li class="breadcrumb-item active">${tournament.name}</li>
            </ol>
        </nav>

        <div class="tournament-info">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2>${tournament.name}</h2>
                <span class="badge bg-${tournament.status == 'UPCOMING' ? 'primary' : 'success'} fs-6">
                    ${tournament.status}
                </span>
            </div>
            
            <p class="lead">${tournament.description}</p>
            
            <div class="row mt-4">
                <div class="col-md-6">
                    <h5>Tournament Details</h5>
                    <ul class="list-unstyled">
                        <li><strong>Start Date:</strong> <fmt:formatDate value="${tournament.startDate}" pattern="MMM dd, yyyy HH:mm"/></li>
                        <li><strong>End Date:</strong> <fmt:formatDate value="${tournament.endDate}" pattern="MMM dd, yyyy HH:mm"/></li>
                        <li><strong>Players:</strong> ${tournament.participants.size()}/${tournament.maxPlayers}</li>
                    </ul>
                </div>
                
                <div class="col-md-6 text-md-end">
                    <c:if test="${tournament.status == 'UPCOMING' && !tournament.full && sessionScope.user != null}">
                        <form action="${pageContext.request.contextPath}/tournament/register" method="post" class="d-inline">
                            <input type="hidden" name="tournamentId" value="${tournament.id}">
                            <button type="submit" class="btn btn-primary">Register for Tournament</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>

        <h3 class="mb-4">Participants</h3>
        <div class="row">
            <c:forEach var="participant" items="${tournament.participants}">
                <div class="col-md-4 mb-3">
                    <div class="card participant-card">
                        <div class="card-body">
                            <h5 class="card-title">${participant.username}</h5>
                            <p class="card-text">Rating: ${participant.rating}</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty tournament.participants}">
            <div class="text-center py-4">
                <p class="text-muted">No participants have registered yet.</p>
            </div>
        </c:if>

        <c:if test="${tournament.status == 'IN_PROGRESS'}">
            <div class="mt-5">
                <h3 class="mb-4">Tournament Bracket</h3>
                <!-- Tournament bracket will be implemented in the next phase -->
                <div class="alert alert-info">
                    Tournament matches will be displayed here once the tournament begins.
                </div>
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

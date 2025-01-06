<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        h1 { color: #333; }
        .error-message {
            font-size: 18px;
            color: #666;
            margin: 20px 0;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .home-link {
            display: inline-block;
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <h1>Oops! Something went wrong</h1>
    <div class="error-message">
        <% if (exception != null) { %>
            <%= exception.getMessage() %>
        <% } else { %>
            An unexpected error occurred.
        <% } %>
    </div>
    <a href="${pageContext.request.contextPath}/" class="home-link">Return to Home</a>
</body>
</html>

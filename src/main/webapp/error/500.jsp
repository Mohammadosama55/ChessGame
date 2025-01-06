<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>500 - Internal Server Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        h1 { color: #333; }
        .error-code {
            font-size: 72px;
            color: #e74c3c;
            margin: 20px 0;
        }
        .message {
            font-size: 18px;
            color: #666;
            margin-bottom: 30px;
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
    <div class="error-code">500</div>
    <h1>Internal Server Error</h1>
    <p class="message">Sorry, something went wrong on our end. Please try again later.</p>
    <a href="${pageContext.request.contextPath}/" class="home-link">Return to Home</a>
</body>
</html>

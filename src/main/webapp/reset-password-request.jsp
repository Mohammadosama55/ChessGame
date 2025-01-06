<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password Request</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .reset-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 20px;
            background-color: white;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .info-text {
            font-size: 0.9em;
            color: #666;
            margin-top: 15px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="reset-container">
            <h2 class="text-center mb-4">Reset Password</h2>
            
            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    ${param.error}
                </div>
            </c:if>
            
            <c:if test="${param.success != null}">
                <div class="alert alert-success">
                    ${param.success}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/user/reset-password/request" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email Address</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>

                <button type="submit" class="btn btn-primary w-100">Send Reset Link</button>
            </form>

            <div class="info-text">
                <p>Enter your email address and we'll send you a link to reset your password.</p>
                <p>Remember to check your spam folder if you don't receive the email within a few minutes.</p>
            </div>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

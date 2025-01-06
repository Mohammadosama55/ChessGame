<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
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
        .password-requirements {
            font-size: 0.9em;
            color: #666;
            margin-top: 10px;
        }
        .requirement {
            margin-bottom: 5px;
        }
        .requirement.met {
            color: #198754;
        }
        .requirement.met::before {
            content: "✓ ";
        }
        .requirement.unmet {
            color: #dc3545;
        }
        .requirement.unmet::before {
            content: "× ";
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
                    Password has been reset successfully. You can now <a href="${pageContext.request.contextPath}/login.jsp">login</a>.
                </div>
            </c:if>

            <form id="resetForm" action="${pageContext.request.contextPath}/user/reset-password" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="token" value="${param.token}">
                
                <div class="mb-3">
                    <label for="password" class="form-label">New Password</label>
                    <input type="password" class="form-control" id="password" name="password" required 
                           onkeyup="checkPasswordStrength()">
                    <div class="password-requirements">
                        <div id="lengthReq" class="requirement unmet">At least 8 characters</div>
                        <div id="upperReq" class="requirement unmet">At least one uppercase letter</div>
                        <div id="lowerReq" class="requirement unmet">At least one lowercase letter</div>
                        <div id="digitReq" class="requirement unmet">At least one number</div>
                        <div id="specialReq" class="requirement unmet">At least one special character</div>
                    </div>
                </div>
                
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>

                <button type="submit" class="btn btn-primary w-100" id="submitBtn" disabled>Reset Password</button>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function checkPasswordStrength() {
            const password = document.getElementById('password').value;
            const submitBtn = document.getElementById('submitBtn');
            
            // Check requirements
            const hasLength = password.length >= 8;
            const hasUpper = /[A-Z]/.test(password);
            const hasLower = /[a-z]/.test(password);
            const hasDigit = /\d/.test(password);
            const hasSpecial = /[^A-Za-z0-9]/.test(password);
            
            // Update requirement indicators
            document.getElementById('lengthReq').className = 'requirement ' + (hasLength ? 'met' : 'unmet');
            document.getElementById('upperReq').className = 'requirement ' + (hasUpper ? 'met' : 'unmet');
            document.getElementById('lowerReq').className = 'requirement ' + (hasLower ? 'met' : 'unmet');
            document.getElementById('digitReq').className = 'requirement ' + (hasDigit ? 'met' : 'unmet');
            document.getElementById('specialReq').className = 'requirement ' + (hasSpecial ? 'met' : 'unmet');
            
            // Enable submit button if all requirements are met
            submitBtn.disabled = !(hasLength && hasUpper && hasLower && hasDigit && hasSpecial);
        }

        function validateForm() {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password !== confirmPassword) {
                alert('Passwords do not match!');
                return false;
            }
            return true;
        }
    </script>
</body>
</html>

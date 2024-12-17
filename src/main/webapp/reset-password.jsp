<%--
  Created by IntelliJ IDEA.
  User: AFish
  Date: 2024/12/9
  Time: 00:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>重置密码</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f9;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .form-container {
      background: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      width: 300px;
    }
    h2 {
      text-align: center;
      color: #333;
    }
    input[type="password"], button {
      width: 90%;
      padding: 10px;
      margin: 10px 0;
      border: 1px solid #ddd;
      border-radius: 5px;
    }
    button {
      background-color: #007BFF;
      color: white;
      border: none;
      cursor: pointer;
    }
    button:hover {
      background-color: #0056b3;
    }
    .message {
      text-align: center;
      color: #28a745;
    }
  </style>
</head>
<body>
<div class="form-container">
  <h2>重置密码</h2>
  <input type="password" id="password" placeholder="请输入新密码" required>
  <input type="password" id="confirmPassword" placeholder="确认新密码" required>
  <button onclick="resetPassword()">提交</button>
  <p class="message" id="message"></p>
</div>

<script src="js/reset-password.js"></script>

</body>
</html>

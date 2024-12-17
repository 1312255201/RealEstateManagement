<%--
  Created by IntelliJ IDEA.
  User: AFish
  Date: 2024/12/3
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改个人信息</title>
    <link rel="stylesheet" type="text/css" href="css/profile.css">

</head>
<body>
<div class="form-container">
    <h1>个人信息</h1>
    <!-- 个人证件照 -->
    <div class="profile-pic">
        <% String avatarUrl = (String) request.getSession(false).getAttribute("avatar"); %>
        <% if (avatarUrl != null && !avatarUrl.isEmpty() ) { %>
        <img src="<%= avatarUrl %>" alt="证件照">
        <% } else { %>
        <img src="../img/default-avatar.png" alt="默认头像">
        <% } %>
    </div>

    <!-- 信息展示 -->
    <div class="info-grid">
        <div class="info-item">
            <label>姓名</label>
            <span id="display-name"><%= request.getSession(false).getAttribute("username") %></span>
        </div>
        <div class="info-item">
            <label>身份证号</label>
            <span id="display-idnumber"><%= request.getSession(false).getAttribute("useridnumber") %></span>
        </div>
        <div class="info-item">
            <label>手机号</label>
            <span id="display-phonenumber"><%= request.getSession(false).getAttribute("userphonenumber") %></span>
            <button onclick="openModal('phonenumber', '<%= request.getSession(false).getAttribute("userphonenumber") %>')">修改</button>
        </div>
        <div class="info-item">
            <label>邮箱</label>
            <span id="display-email"><%= request.getSession(false).getAttribute("useremail") %></span>
            <button onclick="openModal('email', '<%= request.getSession(false).getAttribute("useremail") %>')">修改</button>
        </div>
        <div class="info-item">
            <label>密码</label>
            <span id="display-password">*******</span>
            <button onclick="openModal('password', '*******')">修改</button>
        </div>
        <div class="info-item">
            <label>权限组</label>
            <span id="display-role"><%= request.getSession(false).getAttribute("userrole") %></span>
        </div>
    </div>
</div>

<!-- 模态框 -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h2 id="modalTitle">编辑字段</h2>
        <form id="editForm">
            <input type="hidden" id="field" name="field">
            <label for="newValue">新值:</label>
            <input type="text" id="newValue" name="newValue" required>

            <!-- 二次确认框 -->
            <div id="confirmationSection" style="display: none; margin-top: 20px;">
                <label for="confirmValue">确认信息:</label>
                <input type="password" id="confirmValue" name="confirmValue" placeholder="请输入密码进行确认" required>
            </div>

            <div class="form-actions">
                <button type="button" onclick="submitEdit()">保存修改</button>
                <button type="button" class="cancel-btn" onclick="closeModal()">取消</button>
            </div>
        </form>
    </div>
</div>

<script src="js/profile.js"></script>

</body>
</html>

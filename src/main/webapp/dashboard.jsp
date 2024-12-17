<%--
  Created by IntelliJ IDEA.
  User: AFish
  Date: 2024/12/3
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>房地产管理系统 - 控制台</title>
    <link rel="stylesheet" type="text/css" href="css/dashboard.css">
</head>
<body>
<div class="dashboard">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
        <div>
            <h2>房地产管理系统</h2>
            <%
                session = request.getSession(false);
                String userRole = (String) session.getAttribute("userrole"); // 获取用户身份
                if ("admin".equals(userRole)) {
            %>
            <a href="dashboard.jsp?section=home" class="<%= "home".equals(request.getParameter("section")) || request.getParameter("section") == null ? "active" : "" %>">概览</a>
            <a href="dashboard.jsp?section=profile" class="<%= "profile".equals(request.getParameter("section")) || request.getParameter("section") == null ? "active" : "" %>">个人资料</a>
            <a href="dashboard.jsp?section=usermanage" class="<%= "usermanage".equals(request.getParameter("section")) ? "active" : "" %>">用户管理</a>
            <%
            } else if ("user".equals(userRole)) {
            %>
            <a href="dashboard.jsp?section=profile" class="<%= "profile".equals(request.getParameter("section")) || request.getParameter("section") == null ? "active" : "" %>">个人资料</a>
            <%
            } else if ("merchant".equals(userRole)) {
            %>
            <a href="dashboard.jsp?section=profile" class="<%= "profile".equals(request.getParameter("section")) || request.getParameter("section") == null ? "active" : "" %>">个人资料</a>
            <%
                } else {
                    response.sendRedirect("login.jsp"); // 未登录或角色不明确时跳转到登录页面
                }
            %>
        </div>
        <!-- 退出登录 -->
        <a href="LogoutServlet" class="logout">退出登录</a>
    </div>
    <!-- 右侧内容区域 -->
    <div class="content">
        <%
            String section = request.getParameter("section");
    if ("profile".equals(section)) {
    %><jsp:include page="UserManageView/profile.jsp" /> <%
    }else if ("usermanage".equals(section)) {
    %><jsp:include page="UserManageView/user-management.jsp" /> <%
    }else {
        %><jsp:include page="home.jsp" /> <%
        }
    %>
    </div>
</div>
</body>
</html>

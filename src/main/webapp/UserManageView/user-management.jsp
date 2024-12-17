<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="cn.bugfish.drivingschoolmanagementsystem.DataBase.DBUtil" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>用户管理</title>
  <link rel="stylesheet" type="text/css" href="css/usermanagement.css">

</head>
<body>
<h1>用户管理</h1>

<div class="filter-bar">
  <input type="text" id="searchInput" placeholder="输入任何信息搜索" onkeyup="filterTable()">
  <select id="roleFilter" onchange="filterTable()">
    <option value="">所有角色</option>
    <option value="admin">管理员</option>
    <option value="user">用户</option>
    <option value="coach">教练</option>
  </select>
  <label for="startDate">注册时间:</label>
  <input type="date" id="startDate" onchange="filterTable()">
  <label for="endDate">至</label>
  <input type="date" id="endDate" onchange="filterTable()">
</div>

<table id="userTable">
  <thead>
  <tr>
    <th>ID</th>
    <th>姓名</th>
    <th>身份证号</th>
    <th>手机号</th>
    <th>邮箱</th>
    <th>角色</th>
    <th>创建时间</th>
    <th>操作</th>
  </tr>
  </thead>
  <tbody>
  <%
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = DBUtil.getConnection();
      stmt = conn.createStatement();
      rs = stmt.executeQuery("SELECT * FROM users");

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String idnumber = rs.getString("idnumber");
        String phonenumber = rs.getString("phonenumber");
        String email = rs.getString("email");
        String role = rs.getString("role");
        Timestamp create_at = rs.getTimestamp("created_at");
  %>
  <tr>
    <td><%= id %></td>
    <td><%= name %></td>
    <td><%= idnumber %></td>
    <td><%= phonenumber %></td>
    <td><%= email %></td>
    <td><%= role %></td>
    <td><%= create_at %></td>
    <td>
      <button class="edit-btn" onclick="openEditModal(<%= id %>, '<%= name %>', '<%= idnumber %>', '<%= phonenumber %>', '<%= email %>', '<%= role %>')">编辑</button>
      <form id="reset-password-form-<%= id %>" style="display:inline;">
        <input type="hidden" name="id" value="<%= id %>">
        <button class="reset-btn" type="button" onclick="resetPasswordAdmin('<%= id %>')">重置密码</button>
      </form>
      <form style="display:inline;" onsubmit="confirmDelete(<%= id %>);">
        <button class="delete-btn" type="submit">删除</button>
      </form>
    </td>
  </tr>
  <%
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (conn != null) conn.close();
    }
  %>
  </tbody>
</table>

<div id="editModal" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; border-radius:8px; box-shadow:0 4px 8px rgba(0, 0, 0, 0.2);">
  <form action="EditUserServlet" method="post">
    <input type="hidden" id="userId" name="id">
    <label for="name">姓名:</label>
    <input type="text" id="userName" name="name" required><br><br>
    <label for="idnumber">身份证号:</label>
    <input type="text" id="userIdNumber" name="idnumber" required><br><br>
    <label for="phonenumber">手机号:</label>
    <input type="text" id="userPhoneNumber" name="phonenumber" required><br><br>
    <label for="email">邮箱:</label>
    <input type="email" id="userEmail" name="email" required><br><br>
    <label for="role">角色:</label>
    <select id="userRole" name="role" required>
      <option value="admin">管理员</option>
      <option value="user">用户</option>
      <option value="coach">教练</option>
    </select><br><br>
    <button type="submit">保存</button>
    <button type="button" onclick="closeEditModal()">取消</button>
  </form>
</div>

<!-- 删除确认模态框 -->
<div id="deleteModal" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background:white; padding:20px; border-radius:8px; box-shadow:0 4px 8px rgba(0, 0, 0, 0.2);">
  <form action="DeleteUserServlet" method="post">
    <input type="hidden" id="deleteUserId" name="id">
    <p>确定要删除用户 <span id="deleteUserName"></span> 吗？</p>
    <label for="adminPassword">管理员密码:</label>
    <input type="password" id="adminPassword" name="adminPassword" required><br><br>
    <button type="submit">确认删除</button>
    <button type="button" onclick="closeDeleteModal()">取消</button>
  </form>
</div>

<script src="js/usermanage.js"></script>

</body>
</html>

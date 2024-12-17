package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
    /**
     * 处理 HTTP POST 请求，用于更新用户个人资料
     *
     * @param request  包含客户端请求信息的 HttpServletRequest 对象
     * @param response 包含服务器响应信息的 HttpServletResponse 对象
     * @throws ServletException 如果在处理请求过程中发生异常
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从请求参数中获取用户ID
        String id = request.getParameter("id");
        // 从请求参数中获取用户名
        String name = request.getParameter("name");
        // 从请求参数中获取用户身份证号
        String idnumber = request.getParameter("idnumber");
        // 从请求参数中获取用户电话号码
        String phonenumber = request.getParameter("phonenumber");
        // 从请求参数中获取用户电子邮件
        String email = request.getParameter("email");
        // 从请求参数中获取用户角色
        String role = request.getParameter("role");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 定义SQL语句，用于更新用户信息
            String sql = "UPDATE users SET name = ?, idnumber = ?, phonenumber = ?, email = ?, role = ? WHERE id = ?";
            // 创建PreparedStatement对象，用于执行SQL语句
            pstmt = conn.prepareStatement(sql);
            // 设置SQL语句中的参数
            pstmt.setString(1, name);
            pstmt.setString(2, idnumber);
            pstmt.setString(3, phonenumber);
            pstmt.setString(4, email);
            pstmt.setString(5, role);
            pstmt.setInt(6, Integer.parseInt(id));
            // 执行SQL语句，更新用户信息
            pstmt.executeUpdate();
            // 重定向到用户管理页面
            response.sendRedirect("dashboard.jsp?section=usermanage");
        } catch (Exception e) {
            // 打印异常堆栈跟踪
            UserMannageSystemLoger.logger.error(e.getStackTrace());
        } finally {
            // 关闭PreparedStatement对象
            try { if (pstmt != null) pstmt.close(); } catch (SQLException ignored) {}
            // 关闭数据库连接
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }
}

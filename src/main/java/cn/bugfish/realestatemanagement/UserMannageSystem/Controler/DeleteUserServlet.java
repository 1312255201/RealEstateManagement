package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Listener.UserEditListener;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("id")); // 获取要删除的用户ID
        String adminPassword = request.getParameter("adminPassword"); // 获取管理员输入的密码

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();

            // 验证管理员密码
            int adminid = (int) request.getSession().getAttribute("userid"); // 教练ID从会话中获取

            String verifyPasswordSql = "SELECT password FROM users WHERE id = ? AND role = 'admin'";
            try (PreparedStatement psVerify = conn.prepareStatement(verifyPasswordSql)) {
                psVerify.setInt(1, adminid);
                try (ResultSet rsVerify = psVerify.executeQuery()) {
                    if (rsVerify.next()) {
                        String storedPassword = rsVerify.getString("password");
                        if (!storedPassword.equals(adminPassword)) {
                            // 管理员密码错误，返回错误信息
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\": false, \"message\": \"管理员密码错误！\"}");
                            return;
                        }
                    } else {
                        // 管理员身份验证失败，返回错误信息
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": false, \"message\": \"管理员身份验证失败！\"}");
                        UserEditListener.logger.info("删除用户失败，管理员账户错误！！！！");
                        return;
                    }
                }
            }

            // 删除用户
            String deleteSql = "DELETE FROM users WHERE id = ?";
            ps = conn.prepareStatement(deleteSql);
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // 用户删除成功，返回成功信息
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"message\": \"用户删除成功！\"}");
            } else {
                // 用户删除失败，返回错误信息
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"用户删除失败，请重试！\"}");
            }

        } catch (Exception e) {
            // 打印异常堆栈跟踪
            UserMannageSystemLoger.logger.error(e.getStackTrace());
            // 返回服务器错误信息
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"服务器错误，请稍后重试！\"}");
        } finally {
            try {
                // 关闭结果集、语句和连接
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                // 打印异常堆栈跟踪
                UserMannageSystemLoger.logger.error(e.getStackTrace());
            }
        }
    }
}
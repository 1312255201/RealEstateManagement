package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@WebServlet("/reset-password")
public class ForgetResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取重置密码的令牌
        String token = request.getParameter("token");
        // 获取新密码
        String newPassword = request.getParameter("password");

        // 设置响应的内容类型为 JSON，并设置字符编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 检查请求参数是否有效
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            // 如果请求参数无效，返回错误信息
            response.getWriter().write("{\"success\": false, \"message\": \"请求参数无效！\"}");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            // 检查令牌的有效性
            String queryTokenSql = """
                SELECT user_id, created_at FROM password_resets WHERE token = ? LIMIT 1
                """;
            try (PreparedStatement psToken = conn.prepareStatement(queryTokenSql)) {
                psToken.setString(1, token);
                ResultSet rs = psToken.executeQuery();

                if (!rs.next()) {
                    // 如果令牌无效，返回错误信息
                    response.getWriter().write("{\"success\": false, \"message\": \"无效的令牌！\"}");
                    return;
                }

                // 获取用户 ID 和令牌创建时间
                int userId = rs.getInt("user_id");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // 检查令牌是否过期（1 小时内有效）
                long currentTime = System.currentTimeMillis();
                if (currentTime - createdAt.getTime() > 3600000) {
                    // 如果令牌过期，返回错误信息
                    response.getWriter().write("{\"success\": false, \"message\": \"令牌已过期！\"}");
                    return;
                }

                // 更新用户密码
                String updatePasswordSql = """
                    UPDATE users SET password = ? WHERE id = ?
                    """;
                try (PreparedStatement psUpdate = conn.prepareStatement(updatePasswordSql)) {
                    // 对新密码进行加密处理
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    psUpdate.setString(1, hashedPassword);
                    psUpdate.setInt(2, userId);
                    int rowsUpdated = psUpdate.executeUpdate();

                    if (rowsUpdated > 0) {
                        // 如果密码更新成功，删除已使用的令牌
                        String deleteTokenSql = "DELETE FROM password_resets WHERE token = ?";
                        try (PreparedStatement psDelete = conn.prepareStatement(deleteTokenSql)) {
                            psDelete.setString(1, token);
                            psDelete.executeUpdate();
                        }

                        // 返回成功信息
                        response.getWriter().write("{\"success\": true, \"message\": \"密码已成功重置！\"}");
                    } else {
                        // 如果密码更新失败，返回错误信息
                        response.getWriter().write("{\"success\": false, \"message\": \"密码重置失败，请稍后再试！\"}");
                    }
                }
            }
        } catch (Exception e) {
            // 打印异常堆栈跟踪
            UserMannageSystemLoger.logger.error(e.getStackTrace());
            // 返回服务器错误信息
            response.getWriter().write("{\"success\": false, \"message\": \"服务器错误，请稍后再试！\"}");
        }
    }
}
package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        // 读取请求体中的 JSON 数据
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        // 使用 Gson 解析 JSON 数据
        String json = jsonBuilder.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        if (!jsonObject.has("id")) {
            response.getWriter().write("{\"message\": \"用户ID缺失\", \"success\": false}");
            return;
        }

        String id = jsonObject.get("id").getAsString();
        String defaultPassword = "password"; // 默认密码

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            String hashedPassword = BCrypt.hashpw(defaultPassword, BCrypt.gensalt());
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, Integer.parseInt(id));
            pstmt.executeUpdate();

            response.getWriter().write("{\"message\": \"密码已成功重置\", \"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"message\": \"服务器错误，请稍后重试\", \"success\": false}");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ignored) {}
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
    }
}

package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MultipartConfig
@WebServlet("/CompleteRealNameServlet")
public class CompleteRealNameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取当前会话
        HttpSession session = request.getSession();
        // 从会话中获取用户ID
        Integer userId = (Integer) session.getAttribute("userid");
        // 如果用户未登录，则重定向到登录页面
        if (userId == null) {
            response.sendRedirect("login.jsp"); // 未登录
            return;
        }
        // 从请求中获取用户提交的姓名和身份证号
        String name = request.getParameter("name");
        String idnumber = request.getParameter("idnumber");
        // 从请求中获取用户上传的头像文件
        Part avatarPart = request.getPart("avatar");
        // 如果未上传头像，则返回错误信息
        if (avatarPart == null || avatarPart.getSize() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "请上传证件照！");
            return;
        }

        // 验证文件类型是否为图片
        String contentType = avatarPart.getContentType();
        if (!contentType.startsWith("image/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "文件类型不正确，请上传图片！");
            return;
        }
        // 获取应用程序的绝对路径，并拼接出头像文件的保存路径
        String avatarPath = getServletContext().getRealPath("/") + "avatars/";
        // 创建保存头像文件的目录（如果不存在）
        Files.createDirectories(Paths.get(avatarPath)); // 确保目录存在
        // 清理身份证号中的非法字符，用于生成文件名
        String sanitizedIdNumber = idnumber.replaceAll("[^a-zA-Z0-9]", ""); // 清理文件名
        // 生成头像文件的相对路径
        String relativeAvatarPath = "avatars/" + sanitizedIdNumber + ".png";
        // 将头像文件写入到指定目录
        avatarPart.write(avatarPath + sanitizedIdNumber + ".png"); // 保存文件
        // 定义SQL语句，用于更新用户信息
        String sql = "SELECT * FROM users WHERE idnumber = ?";
        try (Connection conn = DBUtil.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idnumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "身份证号已经在数据库中存在了。");
            }
        } catch (SQLException e) {
            UserMannageSystemLoger.logger.error("发生错误在完成实名信息的CompleteRealNameServlet中验证身份证模块: ", e);
        }

        sql = "UPDATE users SET name = ?, idnumber = ?, finish_info = 'yes' WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             // 设置SQL语句中的参数
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, idnumber);
            pstmt.setInt(3, userId);
            // 执行SQL语句，更新用户信息
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                // 更新成功，将新的用户信息保存到会话中
                session.setAttribute("username", name);
                session.setAttribute("useridnumber", idnumber);
                session.setAttribute("avatar", relativeAvatarPath);
                // 重定向到用户主页
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "更新失败，请重试。");
            }
        } catch (Exception e) {
            // 记录错误日志
            UserMannageSystemLoger.logger.error("发生错误在完成实名信息的CompleteRealNameServlet中: ", e);
            // 返回服务器内部错误信息
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误，请稍后重试。");
        }
    }
}

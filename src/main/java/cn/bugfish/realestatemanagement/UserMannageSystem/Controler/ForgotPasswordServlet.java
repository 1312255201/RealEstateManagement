package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.DAO.EmailConfigDAO;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import cn.bugfish.realestatemanagement.UserMannageSystem.Model.EmailConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取用户提交的邮箱
        String email = request.getParameter("email");
        // 设置响应的内容类型为 JSON，并设置字符编码为 UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (Connection conn = DBUtil.getConnection()) {
            // 查询用户是否存在
            String userCheckSql = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(userCheckSql)) {
                ps.setString(1, email);
                var rs = ps.executeQuery();
                if (!rs.next()) {
                    // 如果用户不存在，返回错误信息
                    response.getWriter().write("{\"success\": false, \"message\": \"邮箱未注册！\"}");
                    return;
                }

                int userId = rs.getInt("id");
                // 生成重置密码的唯一令牌
                String token = java.util.UUID.randomUUID().toString();
                String resetLink = "http://localhost:8080/RealEstateManagement_war_exploded/reset-password.jsp?token=" + token;

                // 保存令牌到数据库
                String saveTokenSql = "INSERT INTO password_resets (user_id, token, created_at) VALUES (?, ?, NOW())";
                try (PreparedStatement psSave = conn.prepareStatement(saveTokenSql)) {
                    psSave.setInt(1, userId);
                    psSave.setString(2, token);
                    psSave.executeUpdate();
                }

                // 发送邮件
                sendEmail(email, resetLink);
                // 返回成功信息
                response.getWriter().write("{\"success\": true, \"message\": \"重置链接已发送，请查收邮箱！\"}");
            }
        } catch (Exception e) {
            // 打印异常堆栈跟踪
            UserMannageSystemLoger.logger.error(e.getStackTrace());
            // 返回服务器错误信息
            response.getWriter().write("{\"success\": false, \"message\": \"服务器错误，请稍后再试！\"}");
        }
    }

    private void sendEmail(String to, String resetLink) throws Exception {
        EmailConfig config = EmailConfigDAO.getEmailConfig(); // 从数据库获取配置

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", config.getSmtpHost());
        props.setProperty("mail.smtp.port", String.valueOf(config.getSmtpPort()));
        // 使用smtp身份验证
        props.setProperty("mail.smtp.auth", "true");
        if (config.isSslEnabled()) {
            //ssl开启
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 由于Properties默认不限制请求时间，可能会导致线程阻塞，所以指定请求时长
        props.setProperty("mail.smtp.connectiontimeout", "10000");// 与邮件服务器建立连接的时间限制
        props.setProperty("mail.smtp.timeout", "10000");// 邮件smtp读取的时间限制
        props.setProperty("mail.smtp.writetimeout", "10000");// 邮件内容上传的时间限制

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getEmailAddress(), config.getEmailPassword());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getEmailAddress()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("密码重置请求");
        message.setText("点击以下链接重置密码:\n" + resetLink);
        Transport.send(message);
    }
}

package cn.bugfish.realestatemanagement.UserMannageSystem.DAO;


import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import cn.bugfish.realestatemanagement.UserMannageSystem.Model.EmailConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmailConfigDAO {
    /**
     * 获取邮件配置信息
     *
     * @return 邮件配置对象
     * @throws Exception 如果查询失败
     */
    public static EmailConfig getEmailConfig() throws Exception {
        // SQL 查询语句，用于从 email_config 表中获取邮件配置信息
        String query = "SELECT email_address, email_password, smtp_host, smtp_port, is_ssl_enabled FROM email_config LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            // 如果查询结果集中有数据
            if (rs.next()) {
                // 创建一个 EmailConfig 对象
                EmailConfig config = new EmailConfig();
                // 设置邮件地址
                config.setEmailAddress(rs.getString("email_address"));
                // 设置邮件密码
                config.setEmailPassword(rs.getString("email_password"));
                // 设置 SMTP 主机
                config.setSmtpHost(rs.getString("smtp_host"));
                // 设置 SMTP 端口
                config.setSmtpPort(rs.getInt("smtp_port"));
                // 设置是否启用 SSL
                config.setSslEnabled(rs.getBoolean("is_ssl_enabled"));
                // 返回邮件配置对象
                return config;
            } else {
                // 如果没有找到邮件配置，抛出异常
                UserMannageSystemLoger.logger.error("邮箱没有进行配置");
                throw new Exception("邮箱配置未找到！");
            }
        }
    }
}

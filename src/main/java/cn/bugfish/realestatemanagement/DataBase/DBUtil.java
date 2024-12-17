package cn.bugfish.realestatemanagement.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接工具类（使用 HikariCP 连接池）
 */
public class DBUtil {
    private static final HikariDataSource dataSource;

    static {
        // 配置 HikariCP 数据源
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/realestate?useSSL=false&characterEncoding=utf8");
        config.setUsername("realestate");
        config.setPassword("realestate");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 设置连接池参数
        config.setMaximumPoolSize(10); // 最大连接数
        config.setMinimumIdle(2); // 最小空闲连接数
        config.setIdleTimeout(30000); // 空闲连接超时时间（毫秒）
        config.setMaxLifetime(1800000); // 连接最大存活时间（毫秒）
        config.setConnectionTimeout(30000); // 获取连接的超时时间（毫秒）

        // 初始化数据源
        dataSource = new HikariDataSource(config);
    }

    /**
     * 获取数据库连接
     * @return Connection 对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭数据库连接池
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

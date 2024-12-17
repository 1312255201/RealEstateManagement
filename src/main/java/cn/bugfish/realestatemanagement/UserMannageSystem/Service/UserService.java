package cn.bugfish.realestatemanagement.UserMannageSystem.Service;

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import cn.bugfish.realestatemanagement.UserMannageSystem.Beans.UserBean;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    /**
     * 注册新用户
     *
     * @param user 用户对象
     * @return 注册是否成功
     * @throws SQLException 如果数据库操作失败
     */
    public boolean registerUser(UserBean user) throws SQLException {
        // 初始化数据库连接对象为 null
        Connection conn = null;
        try {
            // 对用户密码进行加密
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 定义 SQL 语句，用于插入新用户数据
            String sql = "INSERT INTO users (phonenumber, email, password) VALUES (?,?,?)";
            // 创建 PreparedStatement 对象，用于执行 SQL 语句
            PreparedStatement stmt = conn.prepareStatement(sql);
            // 设置 SQL 语句中的参数
            stmt.setString(1, user.getPhonenumber());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashedPassword);
            // 执行 SQL 语句，返回受影响的行数
            return stmt.executeUpdate() > 0;
        } finally {
            // 关闭数据库连接，释放资源
            if (conn!= null) {
                conn.close();
            }
        }
    }
}

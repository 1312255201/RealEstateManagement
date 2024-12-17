package cn.bugfish.realestatemanagement.DataBase;

import cn.bugfish.realestatemanagement.UserMannageSystem.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public boolean updateUser(User user,String key,String value) {
        String sql = "UPDATE users SET " + key + " = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isStudentAssignedToSubject(int studentId, String teachLevel) {
        String sql = "SELECT COUNT(*) FROM teach_info WHERE student_id = ? AND teach_level = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, teachLevel);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isDuplicateAssignment(int coachId, int studentId, String teachLevel) {
        String sql = "SELECT COUNT(*) FROM teach_info WHERE coach_id = ? AND student_id = ? AND teach_level = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coachId);
            ps.setInt(2, studentId);
            ps.setString(3, teachLevel);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean assignStudentToCoach(int coachId, int studentId, String teachLevel) {
        String sql = "INSERT INTO teach_info (coach_id, student_id, teach_level) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coachId);
            ps.setInt(2, studentId);
            ps.setString(3, teachLevel);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(Integer Userid) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Userid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("idnumber"),
                        rs.getString("phonenumber"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at")
                        );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.DataBase.UserDao;
import cn.bugfish.realestatemanagement.UserMannageSystem.Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;


@WebServlet("/EditProfileServlet")
public class EditProfileServlet extends HttpServlet {
    /**
     * 处理 HTTP GET 请求，用于显示用户个人资料编辑页面
     *
     * @param request  包含客户端请求信息的 HttpServletRequest 对象
     * @param response 包含服务器响应信息的 HttpServletResponse 对象
     * @throws ServletException 如果在处理请求过程中发生异常
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从请求参数中获取要编辑的字段名
        String field = request.getParameter("field");

        // 检查用户是否已登录，如果未登录则重定向到登录页面
        if (request.getSession(false) == null || request.getSession(false).getAttribute("userid") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 将字段名和当前会话中对应的值设置为请求属性
        request.setAttribute("field", field);
        request.setAttribute("value", getSessionValue(field, request));
        // 转发请求到 editField.jsp 页面，以便用户编辑个人资料
        request.getRequestDispatcher("editField.jsp").forward(request, response);
    }
    /**
     * 处理 HTTP POST 请求，用于更新用户个人资料
     *
     * @param request  包含客户端请求信息的 HttpServletRequest 对象
     * @param response 包含服务器响应信息的 HttpServletResponse 对象
     * @throws ServletException 如果在处理请求过程中发生异常
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 从请求参数中获取要编辑的字段名
        String field = request.getParameter("field");
        String newValue = request.getParameter("newValue");
        String confirmValue = request.getParameter("confirmValue");
        HttpSession session = request.getSession(false);

        // 基础校验
        if (session == null || field == null || newValue == null) {
            response.getWriter().write("failure");
            return;
        }

        UserDao userDao = new UserDao();
        boolean success = false;
        Integer userId = (Integer) session.getAttribute("userid");
        User user = userDao.getUserById(userId);

        if (user == null) {
            response.getWriter().write("failure");
            return;
        }

        // 处理不同字段的更新逻辑
        switch (field) {
            case "name":
                session.setAttribute("username", newValue);
                success = userDao.updateUser(user, "name", newValue);
                break;

            case "phonenumber":
                // 验证当前密码
                if (confirmValue == null || !BCrypt.checkpw(confirmValue, user.getPassword())) {
                    response.getWriter().write("confirm-fail"); // 确认失败
                    return;
                }
                session.setAttribute("userphonenumber", newValue);
                success = userDao.updateUser(user, "phonenumber", newValue);
                break;

            case "email":
                session.setAttribute("useremail", newValue);
                success = userDao.updateUser(user, "email", newValue);
                break;

            case "idnumber":
                session.setAttribute("useridnumber", newValue);
                success = userDao.updateUser(user, "idnumber", newValue);
                break;

            case "password":
                // 验证当前密码
                if (confirmValue == null || !BCrypt.checkpw(confirmValue, user.getPassword())) {
                    response.getWriter().write("confirm-fail"); // 确认失败
                    return;
                }
                // 使用加密存储新密码
                String hashedPassword = BCrypt.hashpw(newValue, BCrypt.gensalt());
                success = userDao.updateUser(user, "password", hashedPassword);
                break;

            default:
                response.getWriter().write("invalid-field"); // 无效字段
                return;
        }

        // 返回结果
        if (success) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("fail");
        }
    }

    private String getSessionValue(String field, HttpServletRequest request) {
        return (String) request.getSession(false).getAttribute("user" + field);
    }
}

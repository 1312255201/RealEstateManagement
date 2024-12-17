package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import cn.bugfish.realestatemanagement.UserMannageSystem.Beans.UserBean;
import cn.bugfish.realestatemanagement.UserMannageSystem.Loger.UserMannageSystemLoger;
import cn.bugfish.realestatemanagement.UserMannageSystem.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();
    /**
     * 处理 HTTP POST 请求，用于注册新用户
     *
     * @param request  包含客户端请求信息的 HttpServletRequest 对象
     * @param response 包含服务器响应信息的 HttpServletResponse 对象
     * @throws ServletException 如果在处理请求过程中发生异常
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户输入
        String phonenumber = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String captcha = request.getParameter("captcha");

        // 验证输入
        if (phonenumber == null || phonenumber.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("error", "请填写所有字段！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            request.setAttribute("error", "验证码错误！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        UserBean user = new UserBean(phonenumber, email, password);
        try {
            boolean success = userService.registerUser(user);
            if (success) {
                request.setAttribute("success", "注册成功！请前往登录。");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "注册失败，请稍后再试！");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // 打印异常堆栈跟踪
            UserMannageSystemLoger.logger.error(e.getStackTrace());
            request.setAttribute("error", "手机号或邮箱已被注册！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
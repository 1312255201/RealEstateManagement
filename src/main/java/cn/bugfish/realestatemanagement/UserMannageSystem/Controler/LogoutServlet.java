package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().removeAttribute("userid"); // 移除登录用户信息(确保日志记录)
        // 销毁用户的会话
        request.getSession().invalidate();
        // 跳转到登录页面
        response.sendRedirect("login.jsp");
    }
}
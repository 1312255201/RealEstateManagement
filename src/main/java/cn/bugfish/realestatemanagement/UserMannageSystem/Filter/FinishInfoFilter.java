package cn.bugfish.realestatemanagement.UserMannageSystem.Filter;
/*
 * 实名认证过滤器，用于拦截未实名认证的用户访问进行实名认证的过滤器
 * */

import cn.bugfish.realestatemanagement.DataBase.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebFilter("/dashboard.jsp")
public class FinishInfoFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();
        String path = httpRequest.getRequestURI();
        if (path.endsWith("login.jsp") || path.endsWith("index.jsp") || path.endsWith("register.jsp")|| path.endsWith("forget-password.jsp")|| path.endsWith("reset-password.jsp")|| path.endsWith("LoginServlet")|| path.contains("CaptchaServlet")
                || path.endsWith("register")|| path.endsWith("forgot-password") || path.endsWith("reset-password") || path.contains("/css/") || path.contains("/img/")
                || path.contains("/js/")) {
            chain.doFilter(request, response); // 放行
            return;
        }
        // 从 session 获取当前用户的 ID（假设登录时已存储）
        Integer userId = (Integer) session.getAttribute("userid");
        if (userId == null) {
            httpResponse.sendRedirect("login.jsp"); // 未登录，重定向到登录页面
            return;
        }

        try {
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("SELECT finish_info FROM users WHERE id = ?")) {

                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String finishInfo = rs.getString("finish_info");
                        if (!"yes".equalsIgnoreCase(finishInfo)) {
                            httpResponse.sendRedirect("real-name.jsp"); // 跳转到实名页面
                            return;
                        }
                    } else {
                        httpResponse.sendRedirect("login.jsp"); // 用户不存在，重定向到登录页面
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred.");
            return;
        }

        chain.doFilter(request, response); // 继续执行请求
    }

    @Override
    public void destroy() {
    }
}
package cn.bugfish.realestatemanagement.UserMannageSystem.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
* 登录过滤器，用于拦截未登录的用户访问需要登录才能访问的页面
* */

@WebFilter("/*") // 拦截所有请求
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取当前访问路径
        String path = httpRequest.getRequestURI();
        // 不需要过滤的路径，例如登录页面和静态资源
        if (path.endsWith("login.jsp") || path.endsWith("index.jsp") || path.endsWith("register.jsp")|| path.endsWith("forget-password.jsp")|| path.endsWith("reset-password.jsp")|| path.endsWith("LoginServlet")|| path.contains("CaptchaServlet")
                || path.endsWith("register")|| path.endsWith("forgot-password") || path.endsWith("reset-password") || path.contains("/css/") || path.contains("/img/")
                || path.contains("/js/")) {
            chain.doFilter(request, response); // 放行
            return;
        }
        // 判断用户是否登录
        Object user = httpRequest.getSession().getAttribute("userid");
        if (user == null) {
            // 未登录，跳转到登录页面
            httpResponse.sendRedirect("login.jsp");
        } else {
            // 已登录，放行请求
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
package cn.bugfish.realestatemanagement.UserMannageSystem.Listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Logger;

@WebListener
public class UserEditListener implements ServletRequestListener {

    public static final Logger logger = Logger.getLogger(UserEditListener.class.getName());
    /**
     * 请求初始化时调用的方法
     * @param sre 请求事件对象
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest httpRequest = (HttpServletRequest) sre.getServletRequest();
        // 获取请求路径
        String servletPath = httpRequest.getServletPath();
        if ("/EditUserServlet".equals(servletPath)) {
            var adminid = ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("userid");
            String id = httpRequest.getParameter("id");
            String name = httpRequest.getParameter("name");
            String idnumber = httpRequest.getParameter("idnumber");
            String phonenumber = httpRequest.getParameter("phonenumber");
            String role = httpRequest.getParameter("role");
            String email = httpRequest.getParameter("email");
            logger.info("编辑用户");
            logger.info("编辑用户管理员ID" + adminid + ",被编辑用户: " + id+"新的姓名:"+name+",新的身份证号:"+idnumber+",新的手机号:"+phonenumber+",新的邮箱:"+email+",新的角色:"+role);
        }
        if ("/CompleteRealNameServlet".equals(servletPath)) {
            var userid = ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("userid");
            String name = httpRequest.getParameter("name");
            String idnumber = httpRequest.getParameter("idnumber");
            logger.info("用户完成实名 用户id" +userid+"姓名:"+name+",身份证号:"+idnumber);
        }
        if ("/DeleteUserServlet".equals(servletPath)) {
            var userid = ((HttpServletRequest) sre.getServletRequest()).getSession().getAttribute("userid");
            String id = httpRequest.getParameter("id");
            logger.info("删除用户 操作的管理员" +userid+"删除的用户:"+id);
        }
    }
    /**
     * 请求初始化时调用的方法
     * @param sre 请求事件对象
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest httpRequest = (HttpServletRequest) sre.getServletRequest();
        String servletPath = httpRequest.getServletPath();

        if ("/EditUserServlet".equals(servletPath)) {
            logger.info("编辑结束.");
        }
    }

}


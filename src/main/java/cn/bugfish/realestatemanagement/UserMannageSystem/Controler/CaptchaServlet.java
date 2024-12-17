package cn.bugfish.realestatemanagement.UserMannageSystem.Controler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet("/CaptchaServlet")
public class CaptchaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width = 120;
        int height = 40;
        // 创建一个指定宽高和类型的 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取 Graphics 对象，用于在图像上绘制
        Graphics g = image.getGraphics();

        Random random = new Random();
        // 设置背景颜色为浅灰色
        g.setColor(new Color(230, 230, 230));
        // 填充整个图像区域
        g.fillRect(0, 0, width, height);

        for (int i = 0; i < 5; i++) {
            // 生成随机颜色
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            // 绘制随机线条
            g.drawLine(x1, y1, x2, y2);
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char c = chars.charAt(random.nextInt(chars.length()));
            captcha.append(c);
            // 生成随机颜色
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            // 设置随机字体
            g.setFont(new Font("Arial", Font.BOLD, 20 + random.nextInt(10)));
            // 在图像上绘制字符
            g.drawString(String.valueOf(c), 20 * i + 10, 30);
        }
        // 获取当前请求的会话对象
        HttpSession session = request.getSession();
        // 将生成的验证码字符串存储在会话中
        session.setAttribute("captcha", captcha.toString());
        // 禁用图像缓存（这里因为没有权限问题，困扰了好久通过查阅资料发现可以关闭文件缓存来防止出现临时文件创建出现权限问题，所以加上这句代码）
        ImageIO.setUseCache(false);
        // 将图像以 JPG 格式写入到响应的输出流中
        ImageIO.write(image,"jpg",response.getOutputStream());
    }

}

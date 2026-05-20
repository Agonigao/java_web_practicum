package com.library.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码生成工具类
 */
public class CaptchaUtil {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    private static final String CHARACTERS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // 排除易混淆字符

    /**
     * 生成验证码图片并返回验证码字符串
     */
    public static String generateCaptcha(OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();

        // 设置背景色
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置边框
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 绘制干扰线
        for (int i = 0; i < 15; i++) {
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码字符
        StringBuilder code = new StringBuilder();
        Font font = new Font("Arial", Font.BOLD, 24);
        g.setFont(font);
        for (int i = 0; i < CODE_LENGTH; i++) {
            String ch = String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            code.append(ch);
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            g.drawString(ch, 15 + i * 20, 28);
        }

        g.dispose();
        ImageIO.write(image, "JPEG", os);
        return code.toString();
    }
}

package net.hsp.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


 
public class ValidateCodeUtil extends HttpServlet { 

	public ValidateCodeUtil() {
		super();
	}

	public void destroy() {
		super.destroy();
	}


    // 设置验证码图片中显示的字体高度
    private static int fontHeight = 19;

    private static int codeY = 16;

    // 在这里定义了验证码图片的宽度
    private static int width = 60;

    // 定义验证码图片的高度。
    private static int height = 20;

    // 定义验证码f字符个数，此处设置为4位
    private static int codeNum = 4;
 
    
    private static int x = width/5;
    
    
    
    static char[] codeSequence = { '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

 
    public  void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {
        try {
            // 定义验证码图像的缓冲流
            BufferedImage buffImg = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            // 产生图形上下文
            Graphics2D g = buffImg.createGraphics();

            // 创建随机数产生函数
            Random random = new Random();

            // 将验证码图像背景填充为白色
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            // 创建字体格式，字体的大小则根据验证码图片的高度来设定。
            Font font = new Font("Times New Roman",Font.BOLD|Font.ITALIC, fontHeight);
            // 设置字体。
            g.setFont(font);

            // 为验证码图片画边框，为一个像素。
            g.setColor(Color.pink);
            g.drawRect(0, 0, width - 1, height - 1);

            // 随机生产222跳图片干扰线条，使验证码图片中的字符不被轻易识别（加上不清晰）
            g.setColor(Color.BLACK);
            for (int i = 0; i <2; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }

            // randomCode保存随机产生的验证码
            StringBuffer randomCode = new StringBuffer();

            // 定义颜色三素
            int red = 0, green = 0, blue = 0;

            // 随机生产codeNum个数字验证码
            for (int i = 0; i < codeNum; i++) {
                // 得到随机产生的验证码
                String strRand = String
                        .valueOf(codeSequence[random.nextInt(55)]);
                // 使用随机函数产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不 同。
                red = 33;
                green = 33;
                blue = 33;

                // 用随机产生的颜色将验证码绘制到图像中。
                g.setColor(new Color(red, green, blue));
                g.drawString(strRand, (i * 13) +2, codeY);
                // 将产生的四个随机数组合在一起。
                randomCode.append(strRand);
            }
            // 将生产的验证码保存到Session中
            HttpSession session = req.getSession();
            session.setAttribute("authcode", randomCode.toString());

            // 设置图像缓存为no-cache。
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", 0);

            resp.setContentType("image/jpeg");

            // 将最终生产的验证码图片输出到Servlet的输出流中
            ServletOutputStream sos = resp.getOutputStream();
            ImageIO.write(buffImg, "jpeg", sos);
            sos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	/**
	 * 初始化验证图片属性
	 */
	public void init() throws ServletException {
		// 从web.xml中获取初始信息
		// 宽度
		String strWidth = this.getInitParameter("width");
		// 高度
		String strHeight = this.getInitParameter("height");
		// 字符个数
		String strCodeCount = this.getInitParameter("codeCount");
		// 将配置信息转换成数值
		try {
			if (strWidth != null && strWidth.length() != 0) {
				width = Integer.parseInt(strWidth);
			}
			if (strHeight != null && strHeight.length() != 0) {
				height = Integer.parseInt(strHeight);
			}
			if (strCodeCount != null & strCodeCount.length() != 0) {
				codeNum = Integer.parseInt(strCodeCount);
			}
		} catch (NumberFormatException e) {

		}
		x = width / (codeNum + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

}
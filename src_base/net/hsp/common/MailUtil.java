package net.hsp.common;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * 邮件发送公共类
 * 
 * @author admin
 * 
 */
public class MailUtil
{

	public static final String	HOSTNAME	= "smtp.163.com";
	public static final String	USERNAME	= "jsmarts@163.com";
	public static final String	PWD			= "123456";
	public static final String	NAME		= "jsmarts";

	public static int checkValue(String str)
	{
		if (str != null && str != "")
		{
			int m = str.trim().indexOf("<");
			int n = str.trim().indexOf(">");
			int index2 = str.trim().indexOf(";");
			int index4 = str.trim().indexOf(",");
			int index5 = str.trim().indexOf("'");
			int index6 = str.trim().indexOf('"');
			int index7 = str.trim().indexOf("<<");
			int index8 = str.trim().indexOf(">>");
			if (m != -1 || n != -1 || index2 != -1 || index4 != -1 || index5 != -1 || index6 != -1 || index7 != -1 || index8 != -1)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 1;
		}
	}

	public static String replaceStr(String inputString)
	{
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try
		{
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script>]*?>[\s\S]*?<\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style>]*?>[\s\S]*?<\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		}
		catch (Exception e)
		{
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;
	}

	public static void send(String to, String toName, String subject, String body) throws Exception
	{
		try
		{
			SimpleEmail email = new SimpleEmail();
			// 设置邮件编码
			email.setCharset("UTF-8");
			// 设置邮件服务器
			email.setHostName(HOSTNAME);
			// 设置登录邮件服务器用户名和密码
			email.setAuthentication(USERNAME, PWD);
			// 添加收件人
			email.addTo(to, toName);
			// 设置发件人
			email.setFrom(USERNAME, NAME);
			// 设置邮件标题
			email.setSubject(subject);
			// 设置邮件正文内容
			email.setMsg(body);
			// 发送邮件
			email.send();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param to 收件人
	 * @param toName 收件人名称 
	 * @param subject 主题 
	 * @param htmlBody 正文 可以包含HTML标签 
	 * @param file 附件
	 * @throws EmailException
	 */
	public static void send(String to, String toName, String subject, String htmlBody, File file) throws EmailException
	{
		HtmlEmail email = new HtmlEmail();
		// 设置邮件编码
		email.setCharset("UTF-8");
		// 设置邮件服务器
		email.setHostName(HOSTNAME);
		// 设置登录邮件服务器用户名和密码
		email.setAuthentication(USERNAME, PWD);
		// 添加收件人
		email.addTo(to, toName);
		// 设置发件人
		email.setFrom(USERNAME, NAME);
		// 设置邮件标题
		email.setSubject(subject);
		// 设置邮件正文内容
		email.setHtmlMsg(htmlBody);
		EmailAttachment attachment = new EmailAttachment();
		if (file!=null) {
			// 要发送的附件
			attachment.setPath(file.getPath());
			attachment.setName(file.getName());
			// 设置附件描述
			attachment.setDescription("Attachment Description");
			// 设置附件类型
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			// 添加附件
			email.attach(attachment);		
		}

		// 发送邮件
		email.send();

	}

	public static void main(String[] args)
	{
		try
		{
			MailUtil.send("ycit@vip.qq.com", "JSmart", "用户激活邮件", "dddddddddddd");
			System.out.println("end.");
		}
		catch (Exception e)
		{

			e.printStackTrace();
		}
	}
}

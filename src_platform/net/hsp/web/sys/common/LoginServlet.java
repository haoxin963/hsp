package net.hsp.web.sys.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.hsp.common.SystemContext;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.DynamicDataSource;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.sf.json.JSONObject;

public class LoginServlet extends HttpServlet {

	public LoginServlet() {
		super();
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String custId = request.getRequestURI();
		custId = custId.substring(custId.lastIndexOf("/") + 1, custId.length() - 6);
		request.setAttribute("custId", custId);
		Cookie cookie = new Cookie("custId", custId);
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
		/**
		String domain = null;
		if (domain == null) {
			domain = custId;
		}*/
		
		// 从站点(子域)缓存中校验站点是否存在
		if (HttpSessionFactory.getStation(custId) == null) {
			request.getRequestDispatcher("/com/error/405.jsp").forward(request, response);
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute(PlatFormConstant.CURRENT_SYSINSTANCE, custId);
		// session.setAttribute("domain", domain);
		DynamicDataSource.setCustId(custId);
		ActionUtil.setActionContext(request, response, null);
		
		//手机端登录返回sessionid
		String path = request.getRequestURI();
		request.getRequestURI();
		String loginType = null;
		if (path.indexOf("~") > -1) {
			String t = null;
			String regex = "(~[^/]+)\\S*";
			java.util.regex.Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(path);
			if (m.find()) {
				t = m.group(1);
			}
			if (t != null) {
				loginType = t;
			}
			loginType = loginType.replaceAll("~", ""); 
		}
		
		if("mobile".equals(loginType)) {
			JSONObject responseJSONObject = new JSONObject();
			System.out.println("session="+session.getId());
			responseJSONObject.put("sessionid", session.getId());
		    response.setCharacterEncoding("UTF-8");  
		    response.setContentType("application/json; charset=utf-8");  
		    PrintWriter out = null;  
		    try {  
		        out = response.getWriter();  
		        out.append(responseJSONObject.toString());  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        } 
		    }
		    return;
		} 
		
		// Map m = HttpSessionFactory.getStation(custId);
		String loginPage = "/page/" + custId + "/login.jsp";
		String loginFile = SystemContext.getProperty("webApp.root")+loginPage; 
		if (new File(loginFile).exists()) {
			request.getRequestDispatcher(loginPage).forward(request, response);
		} else {
			request.getRequestDispatcher("/main/login.jsp").forward(request, response);
		}
	}

}

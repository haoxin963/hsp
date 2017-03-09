package net.hsp.web.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.hsp.common.SystemContext;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.DynamicDataSource;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.SpringCtx;
import sun.misc.BASE64Decoder;
 


public class ValidateFilter implements Filter {

	@Override
	public void destroy() { 
	}
	
	private void headerCheck(HttpServletRequest request,HttpServletResponse response,String error401) throws IOException{
		String auth = null; 
		auth = request.getHeader("Authorization");
		//从头信息里取出用户名和密码 创建会话
		if(auth!=null){ 
			auth = auth.substring(6);
			auth = new String(new BASE64Decoder().decodeBuffer(auth),"UTF-8");
			int p = auth.lastIndexOf(":");
			
			String userName = auth.substring(0, p);
			String pwd = auth.substring(p+1);
			User user = new User();
			user.setUserName(userName);
			user.setPassword(pwd);
			UserService userService = (UserService) SpringCtx.getBean("userServiceImpl");
			Map loginResult = userService.login(user);
			String retCode = loginResult.get("retCode").toString();
			// 登录成功
			if ("1".equals(retCode)) {
				User loginUser = (User) loginResult.get("retUser"); 
				HttpSessionFactory.authSuccess(request, loginUser.getId(), loginUser.getUserName());
				HttpSession session = request.getSession(false);  
				ActionUtil.setActionContext(request, response, session); 
			}
		}
	}
 
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res; 
		//上下文
		String path = request.getRequestURI();  
		//兼容历史webservice请求路径 
		if (path.indexOf("/services/")>0) { 
			chain.doFilter(req, res);
			return;
		}
		
		HttpSession s = request.getSession(false);  
		String error401 = "{\"status\":\"401\",\"msg\":\"无效的会话,请重新登录!\"}";
		//匿名请求及前端页面
		if (path.indexOf("/anon/")>-1 || path.indexOf("/front/")>-1) {
			chain.doFilter(req, res);
			return;
		}else if (path.indexOf("/portal/")>-1) {
			if (s==null || s.getAttribute("mid")==null) {
				if (path.endsWith(".do")) {
					String custId = (String) s.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
					request.getRequestDispatcher("/anon/"+custId+"/portal/login.do").forward(req, res);
				}else if(path.endsWith("json")){
					response.setContentType("text/html");
					response.getWriter().print(error401);
				} 
				return;
			}
			chain.doFilter(req, res);
			return;
		}

		//利用header头重建会话
		if(s==null || s.getAttribute("userId")==null){ 
			headerCheck(request,response,error401);
		}
		
		if(s==null || s.getAttribute("userId")==null){  
			if (path.endsWith("jspx") || path.endsWith(".do") || path.endsWith(".xlsx") || path.endsWith(".pdfx")) {
				response.sendRedirect(request.getContextPath()+"/com/error/401.jsp");
			}else if(path.endsWith("json")){
				response.setContentType("text/html");
				response.getWriter().print(error401);
			} else{
				response.setContentType("text/html");
				response.getWriter().print(error401);
			}  
			return;
		}else{
			SystemContext.setUserName((String) s.getAttribute("userName"));
		}

		//url鉴权 
		if(!s.getAttribute("userName").equals(PlatFormConstant.ADMIN_USERNAME)){
			String custId = HttpSessionFactory.getCustId(request);
			DynamicDataSource.setCustId(custId);
			System.out.println("custId=>"+custId);
			Set<String> allFunctions = null;
			if(s.getAttribute("allFunctions") != null) {
				allFunctions = (Set<String>)s.getAttribute("allFunctions");
			} else {
				allFunctions = HttpSessionFactory.getAllFunctionSet();
				if(allFunctions != null && allFunctions.size() > 0) {
					s.setAttribute("allFunctions", allFunctions);
				}
			}
			path = path.replaceAll("//", "/");
			path = path.replaceAll(request.getContextPath(), "");
			int dotIndex = path.indexOf(".");
			String _path = "";
			if(dotIndex>0){
				_path = path.substring(0, dotIndex);
			}
			if(allFunctions != null && allFunctions.contains(_path)) {
				Set<String> urls = null;
				if(s.getAttribute("urls") != null) {
					urls = (Set<String>)s.getAttribute("urls");
				} else {
					urls = HttpSessionFactory.getFunctionSetByUserId(s.getAttribute("userId")+"");
					if(urls != null && urls.size() > 0) {
						s.setAttribute("urls", urls);
					}
				}				
				System.out.println(urls);
				if(urls!=null && !urls.contains(_path)){
					System.out.println("无权限:"+path);
					if (path.endsWith("jspx") || path.endsWith(".do") || path.endsWith(".xlsx") || path.endsWith(".pdfx")) {
						response.sendRedirect(request.getContextPath()+"/com/error/403.jsp");
					}else if(path.endsWith("json")){
						response.setContentType("text/html");
						response.getWriter().print("{\"status\":\"403\",\"msg\":\"没有此地址的访问权限,请尝试重新登录!\"}");
					} 
					return;
				}
			}
		}
		
		//jspx处理
		if (path.endsWith(".jspx")) {
			String realPath =  request.getSession().getServletContext().getRealPath("/"); 
			String custId = HttpSessionFactory.getCustId(request);
			String viewFolderName = "view";
			String extViewFolderName = "page";
	 
			
			path = path.replaceAll(request.getContextPath()+"/", "");
			path = path.replaceAll(".jspx", ".jsp");
			 
			String bizFolder = path.split("/")[0];
			String custPath = extViewFolderName+"/"+custId+"/"+ path.replaceAll(bizFolder+"/",bizFolder+"/");
			String sysPath = viewFolderName+"/"+ path; 
			
			File f = new File(realPath+custPath);
			//如果扩展地址不存在 
			if(!f.exists()){ 
				//System.out.println("系统地址:"+sysPath);
				request.getRequestDispatcher("/"+sysPath).forward(request, res);
				return;
			}else{
				//System.out.println("扩展地址:"+custPath);
				request.getRequestDispatcher("/"+custPath).forward(request, res);
				return;
			}
		}else{
			chain.doFilter(req, res);
		}
	}
	

	@Override
	public void init(FilterConfig arg0) throws ServletException { 
	}

}

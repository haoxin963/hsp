package net.hsp.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class JSPFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		 
		String path = request.getRequestURI(); 
		if (path.startsWith("/view/") || path.startsWith(request.getContextPath()+"/view/")) {
			HttpServletResponse response = (HttpServletResponse) res;
			//response.setStatus(403);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print("系统路径不正确！");
			//response.sendRedirect(request.getContextPath()+"/error_403.jsp");
			return;
		}else{
			HttpSession session = request.getSession();
			ActionUtil.setActionContext(request,  (HttpServletResponse) res, session);
			//System.out.println("兼容uisp2 request 绑定");
			
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

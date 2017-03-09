package net.hsp.web.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.hsp.common.constants.PlatFormConstant;


public class JSPXFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		HttpServletRequest request = (HttpServletRequest) req;
		String realPath =  request.getSession().getServletContext().getRealPath("/"); 
		String custId = (String)  request.getSession().getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
		String viewFolderName = "view";
		String extViewFolderName = "page"; 
		String path = request.getRequestURI();
		path = path.replaceAll(request.getContextPath()+"/", "");
		path = path.replaceAll(".jspx", ".jsp");
		 
		String bizFolder = path.split("/")[0];
		String custPath = extViewFolderName+"/"+custId+"/"+ path.replaceAll(bizFolder+"/",bizFolder+"/");
		String sysPath = viewFolderName+"/"+ path; 
		
		File f = new File(realPath+custPath);
		//扩展地址不存在 
		if(!f.exists()){  
			request.getRequestDispatcher("/"+sysPath).forward(request, res);
			return;
		}else{ 
			request.getRequestDispatcher("/"+custPath).forward(request, res);
			return;
		} 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException { 

	}

}

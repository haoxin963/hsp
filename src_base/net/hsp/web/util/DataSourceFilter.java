package net.hsp.web.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.hsp.common.SystemConfigUtil;
import net.hsp.common.SystemContext;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.DynamicDataSource;

import org.apache.commons.lang.StringUtils;

/**
 * 数据源策略类,会话、url、cookie、配置文件依次查找，得到的custId将会决定后续文件系统、DB资源.
 * 此CUSTID仅绑定ThreadLocal中，会话中绑定CustId将在身份认证完成后，从而避免匿名大量访问、攻击造成session量巨多
 * @autho lk0182
 *
 */
public class DataSourceFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req; 
		String path = request.getRequestURI();
		HttpSession session = request.getSession(false);
		HttpServletResponse response = (HttpServletResponse) res;
		ActionUtil.setActionContext(request, response, session); 
		String custId = null;
		// 目前有以下四种查找custId的策略，优先从session中查找
		if (session!=null) {
			custId =  (String) session.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
		}
		
		// 从地址从查找
		if (path.indexOf("~") > -1 && custId == null) {
			String t = null;
			String regex = "(~[^/]+)\\S*";
			java.util.regex.Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(path);
			if (m.find()) {
				t = m.group(1);
			}
			if (t != null) {
				custId = t;
			}
			custId = custId.replaceAll("~", ""); 
		}
		// 从cookie中查找
		if (StringUtils.isBlank(custId)) { 
			Cookie[] cs = request.getCookies();
			if (cs != null) {
				for (int i = 0; i < cs.length; i++) {
					if ("custId".equals(cs[i].getName())) {
						custId = cs[i].getValue();
						break;
					}
				}
			} 
		}
		// 从默认配置文件中查找
		if (StringUtils.isBlank(custId)) {
			custId = SystemConfigUtil.getInstance().getProperty("defaultCustId"); 
		}
		if (custId !=null) { 
			if(session == null) {
				session = request.getSession(true);
			}
			session.setAttribute(PlatFormConstant.CURRENT_SYSINSTANCE, custId);
		}
 
		//当custId为空时，也要放入null,防止线程池下某线程重用时，取出的线程里的threadLocal里是历史数据.
		DynamicDataSource.setCustId(custId);
		
		if(session != null)
		{
			DynamicDataSource.setDomain((String)session.getAttribute("domain"));
		}
		// 配合权限控制，锁定准确url
		String url = request.getRequestURI();
		url = url.replaceAll("//", "/").replaceFirst(request.getContextPath(), "");
		request.setAttribute("url", url);
		chain.doFilter(req, res);
		//清理threadlocal中的数据,tomcat等使用线程池复用线程时会造成脏数据
		SystemContext.setUserName(null);
		DynamicDataSource.setCustId(null); 
		DynamicDataSource.setDomain(null); 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

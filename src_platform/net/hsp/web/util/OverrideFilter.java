package net.hsp.web.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.hsp.common.ScanClass;
import net.hsp.dao.DynamicDataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class OverrideFilter implements Filter {

	private static class Way {
		public String from;
		public String to;

		public Way(String from, String to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public String toString() {
			return "Way [from=" + from + ", to=" + to + "]";
		}
	}

	private static class CustStrategy {
		private List<Way> ways = new ArrayList<Way>();

		public void addRedirect(String from, String to) {
			ways.add(new Way(from, to));
		}

		public Way getWay(String from) {
			for (Way w : ways) {
				if (from.contains(w.from)) {
					return w;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return "CustStrategy [ways=" + ways + "]";
		}
	}

	private Map<String, CustStrategy> custStrategies = new HashMap<String, OverrideFilter.CustStrategy>();

	// 有则给，无则创建之后给
	private CustStrategy takeCS(String custId) {
		CustStrategy cs = custStrategies.get(custId);
		if (cs == null) {
			cs = new CustStrategy();
			custStrategies.put(custId, cs);
		}
		return cs;
	}

	// 有则给，无则NULL
	private CustStrategy getCS(String custId) {
		return custStrategies.get(custId);
	}

	// 重定向方案
	private static class RedPlan {
		public boolean need;// 是否需要重新定向
		public String path;// 重定向路径
	}

	private RedPlan getPlan(String custId, String curPath) {
		RedPlan p = new RedPlan();
		p.need = false;
		CustStrategy cs = getCS(custId);
		if (cs != null) {
			Way w = cs.getWay(curPath);
			if (w != null) {
				p.need = true;
				p.path = curPath.replace(w.from, w.to);
			}
		}
		return p;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		handlePackage("net.hsp.web.*");
	}

	private void handlePackage(String pack) {
		if (pack == null) {
			return;
		}
		Set<Class<?>> clazzes = ScanClass.scanController(pack);
		if (clazzes == null) {
			return;
		}
		Iterator<Class<?>> iter = clazzes.iterator();
		while (iter.hasNext()) {
			Class<?> clazz = iter.next();
			Class<?> parentClazz = clazz.getSuperclass();
			if (parentClazz == null) {
				continue;
			}
			if (parentClazz.equals(Object.class)) {
				continue;
			}
			if (null == parentClazz.getAnnotation(Controller.class)) {
				continue;
			}
			RequestMapping parentReqMapAnno = parentClazz.getAnnotation(RequestMapping.class);
			if (null == parentReqMapAnno) {
				continue;
			}
			RequestMapping reqMapAnno = clazz.getAnnotation(RequestMapping.class);
			if (null == reqMapAnno) {
				continue;
			}
			String spn = clazz.getPackage().getName();
			String[] subpackparts = spn.split("\\.");
			if (subpackparts == null || subpackparts.length == 0) {
				continue;
			}
			String custId = subpackparts[3];
			CustStrategy cs = takeCS(custId);
			for (Method func : clazz.getDeclaredMethods()) {
				for (Method parentFunc : parentClazz.getDeclaredMethods()) {
					if (parentFunc.getName().equals(func.getName())) {
						System.out.println("子类中属于Override的方法:" + func.getName());
						RequestMapping parentFuncReqMapAnno = parentFunc.getAnnotation(RequestMapping.class);
						if (parentFuncReqMapAnno != null) {
							for (String reqInClass : parentReqMapAnno.value()) {
								for (String reqInFunc : parentFuncReqMapAnno.value()) {
									String from = reqInClass + reqInFunc;
									String to = reqMapAnno.value()[0] + reqInFunc;
									cs.addRedirect(from, to);
								}
							}
						}
					}
				}
				RequestMapping funcReqMapAnno = func.getAnnotation(RequestMapping.class);
				if (funcReqMapAnno != null) {
					for (String reqInClass : parentReqMapAnno.value()) {
						for (String reqInFunc : funcReqMapAnno.value()) {
							String from = reqInClass + reqInFunc;
							String to = reqMapAnno.value()[0] + reqInFunc;
							cs.addRedirect(from, to);
						}
					}
				}
			}
			System.out.println(custStrategies);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("OverrideFilter:doFilter");
		if (request instanceof HttpServletRequest) {
			String custId = DynamicDataSource.getCustId();
			HttpServletRequest httpReq = (HttpServletRequest) request;
			String curPath = httpReq.getServletPath();
			RedPlan p = getPlan(custId, curPath);
			if (p.need) {
				System.out.println("需要重定向为" + p.path);
				request.getRequestDispatcher(p.path).forward(request, response);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}

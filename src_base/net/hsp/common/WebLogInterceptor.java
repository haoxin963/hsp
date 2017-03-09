package net.hsp.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.web.util.ClientInfoUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class WebLogInterceptor  extends HandlerInterceptorAdapter {
	private static Logger logger = LogManager.getLogger(WebLogInterceptor.class);
	ThreadLocal<Long> timec1 = new ThreadLocal<Long>(); 
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		HandlerMethod h = (HandlerMethod) handler;
		WebLogUtil anno =  h.getBean().getClass().getAnnotation(WebLogUtil.class);
		String type = (anno!=null) ? anno.name() : "";
		MethodLogUtil anno2 = h.getMethod().getAnnotation(MethodLogUtil.class);
		//为1时表示需要记录操作log
		if(anno2!=null && "1".equals(anno2.type())){
			String action = (anno2!=null) ? anno2.value() : ""; 
			if (StringUtils.isNotBlank(type+action)) {  
				ClientInfoUtil c = new ClientInfoUtil(request.getHeader("user-agent"));
				logger.info(SystemContext.getUserName() + " Action :"+(System.currentTimeMillis()-timec1.get())+"ms|"+c.getIp(request)+"|"+type+action+"|"+h.getBean().getClass().getName()+"."+h.getMethod().getName()+";"  );
			}
		}
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		timec1.set(System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	} 

}

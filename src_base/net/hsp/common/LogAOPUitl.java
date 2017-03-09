package net.hsp.common;

import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class LogAOPUitl {
	private static Logger logger = LogManager.getLogger(LogAOPUitl.class);
	ThreadLocal<Long> time = new ThreadLocal<Long>(); 

	public void after(JoinPoint joinPoint) {
		Class targetClass = joinPoint.getTarget().getClass();
		ServiceLogUtil slog = (ServiceLogUtil) targetClass.getAnnotation(ServiceLogUtil.class);
		MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
		Class<?>[] args = joinPointObject.getMethod().getParameterTypes();
		try {
			Method method = joinPoint.getTarget().getClass().getMethod(joinPointObject.getMethod().getName(), args);
			MethodLogUtil smethod = (MethodLogUtil) method.getAnnotation(MethodLogUtil.class);
			//当业务方法标识为0时不记录日志 smethod!= null  && !"0".equals(smethod.type())
			if (true) {
				String m1 = ""; 
				if (smethod != null) {
					m1 = smethod.value(); 
				}
				logger.info(SystemContext.getUserName() + " Service:" + ((System.currentTimeMillis() - (time.get()))) + "ms " + ((slog != null) ? slog.name() : "") +  m1 + " "+ targetClass.getName() + "." + method.getName());
			}
		} catch (Exception e) { 
			logger.error("日志记录异常");
		}
	}

	public void before(JoinPoint joinPoint) {
		time.set(System.currentTimeMillis()); 
	}
}

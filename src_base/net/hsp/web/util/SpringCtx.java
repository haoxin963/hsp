package net.hsp.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationObjectSupport;

public class SpringCtx extends WebApplicationObjectSupport {
	private static ApplicationContext applicationContext = null;

	@Override
	protected void initApplicationContext(ApplicationContext context)throws BeansException {
		super.initApplicationContext(context);
		if (SpringCtx.applicationContext == null) {
			SpringCtx.applicationContext = context;
		}
	}

	public static ApplicationContext getSpringContext() {
		return applicationContext;
	}

	public static Object getBean(String id) {
		return getSpringContext().getBean(id);
	}
}

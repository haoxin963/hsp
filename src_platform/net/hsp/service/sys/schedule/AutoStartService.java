package net.hsp.service.sys.schedule;

import net.hsp.service.ServiceException;

import org.springframework.web.context.WebApplicationContext;

public interface AutoStartService {
	/**
	 * 当容器启动时，自动执行此方法
	 * 
	 */
	public Object execute() throws ServiceException;
}

package net.hsp.web.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.hsp.common.SystemContext;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.DynamicDataSource;
import net.hsp.service.ServiceException;
import net.hsp.service.sys.manage.StationService;
import net.hsp.service.sys.schedule.AutoStartService;
import net.hsp.web.util.PathCon;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * spring容器初始化类 全局资源加载监听器
 * 
 * @author jsmart
 * 
 */
public class SetupListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
	 
	}  
	
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		// 上下文地址
		ctx.setAttribute("path", ctx.getContextPath());
		ctx.setAttribute("vpath", ctx.getContextPath()+"/view");//view下资源定位
		ctx.setAttribute("separator","：");
		String root = ctx.getRealPath("/");
		SystemContext.setProperty("webApp.root",root);  
		System.setProperty("webApp.root",root);  
		// 装载log4j日志,动态指定log 文件输出位置  
		
		PropertyConfigurator.configure(root+"/WEB-INF/classes"+PlatFormConstant.CONFIGFILEPATH+"/log4j.properties");
		 
		//类的源文件，class文件存放的文件系统
		PathCon.BINDIR =ctx.getRealPath("/WEB-INF/");
		PathCon.SRCDIR = ctx.getRealPath("/WEB-INF/extsrc/"); 
		 
		//jsp运行基本目录 
		PathCon.SYSVIEWDIR = sce.getServletContext().getRealPath("/");
		ctx.log("contextPath:"+ctx.getContextPath()); 

		DynamicDataSource.setCustId(PlatFormConstant.BASESTATIONID);
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(ctx); 
		try {
			StationService stationService = (StationService) context.getBean("stationServiceImpl");
			Map<String,AutoStartService> beans = context.getBeansOfType(AutoStartService.class);
			Set<String> set = beans.keySet();
			for (String key : set) {
				try {
					beans.get(key).execute();
					ctx.log("AutoStart:"+key);
				} catch (ServiceException e) {
					ctx.log(e.getMessage());
				}
			}
			List<Map<String,Object>> list = stationService.query();
			Map<String,Map<String,Object>> stations = HttpSessionFactory.getStations(); 
			for (Map<String, Object> map : list) { 
				stations.put((String)map.get("domainAddress"), map);
			}
			ctx.log("stations:"+HttpSessionFactory.getStations()); 
			ctx.setAttribute("stations", stations);
		} catch (Exception e) {
			ctx.log(e.getMessage());
			e.printStackTrace();
		}
		
		Map<String,Map> custDic = new DicMap();
		ctx.setAttribute("dics",custDic);  
		ctx.setAttribute("dicsMap",new DicExtMap());  
		
	}

}

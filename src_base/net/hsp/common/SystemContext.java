package net.hsp.common;

import javax.servlet.http.HttpSession;


/**
 * 将HttpSession和当前线程关联 
 * @author JSmart Tools
 * @date 2007-1-14 下午10:24:58
 * @version V1.0
 */
public class SystemContext {

	private static String realPath;
	
	public static String getProperty(String key){
		return realPath;
	}
	
	public static void setProperty(String k,String v){
			realPath = v;
	}
	
	private static ThreadLocal<String> _session = new ThreadLocal<String>();
	
	public static String getUserName(){
		return _session.get();
	}
	
	public static void setUserName(String userName){
		 _session.set(userName);
	}
}

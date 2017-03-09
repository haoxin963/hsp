package net.hsp.common;

import net.hsp.common.PropConfigFactory;
import net.hsp.common.constants.PlatFormConstant;


public class SystemConfigUtil extends PropConfigFactory{
	
	private final static SystemConfigUtil mimeConfigUtil=new SystemConfigUtil();
	
	private SystemConfigUtil(){}
	
	public static SystemConfigUtil getInstance(){
		return mimeConfigUtil;
	} 
	
	@Override
	protected String getFile()
	{
		return PlatFormConstant.CONFIGPATH+"/system-conf.properties";
	}
}

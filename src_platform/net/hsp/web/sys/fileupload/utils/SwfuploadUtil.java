package net.hsp.web.sys.fileupload.utils;


import net.hsp.common.PropConfigFactory;
import net.hsp.common.constants.PlatFormConstant;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;

public class SwfuploadUtil extends PropConfigFactory{
	
    public final static String ENCODING="UTF-8";  
	private final static SwfuploadUtil swfuploadUtil=new SwfuploadUtil();
	
	private SwfuploadUtil(){}
	
	public static SwfuploadUtil getInstance(){
		return swfuploadUtil;
	}
	
	public String getValue(String key){
		return getProperty(key);
	}

	@Override
	protected String getFile()
	{
		return PlatFormConstant.CONFIGPATH+"/swfupload-conf.properties";
	}
	 
	public static String encoder(String str) {
		if (StringUtils.isNotBlank(str)) {
			return new sun.misc.BASE64Encoder().encode(str.getBytes());
			}
		return "";
	}
		 
	public static String decoder(String str) {
		if (StringUtils.isNotBlank(str)) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				return new String(decoder.decodeBuffer(str));
				} catch (Exception e) {
					e.printStackTrace();
					}
			}
		return "";
	}
	
	public static boolean isFirstSeparator(String str){
		if(str.indexOf("/")==0||str.indexOf("\\")==0)
			return true;
		return false;
	}
	
	public static boolean isLastSeparator(String str){
		if(str.lastIndexOf("/")==str.length()-1||str.lastIndexOf("\\")==str.length()-1)
			return true;
		return false;
	}
	
	public static String clearSeparatorTrim(String str){
		while(str.indexOf("//")>=0)
			str.replaceAll("//", "/");
		while(str.indexOf("\\\\")>=0)
			str.replaceAll("\\\\", "\\");
		if(isFirstSeparator(str))
			str=str.substring(1);
		if(isLastSeparator(str))
			str=str.substring(0,str.length());
		return str;
	}
}

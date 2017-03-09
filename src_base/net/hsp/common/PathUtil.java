package net.hsp.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PathUtil {

	private static boolean IS_WINDOWS = System.getProperties().getProperty(
			"os.name").startsWith("Windows");
	
	public static String getRootPath() {

		String url = PathUtil.class.getResource("/").getPath();
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			
		}
		if (IS_WINDOWS) {
			url = url.substring(1);
		} else {
			if (!url.startsWith("/")) {
				url = "/".concat(url);
			}
		}
		if (url.endsWith("/")) {

			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public static String getRootURL() {

		String url = PathUtil.class.getResource("/").toString();
		try {

			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		return url;
	}
	
	public static String getTempPath(){
		String str=new File(PathUtil.getRootPath()).getParent()+File.separator+"temp"+File.separator;
		new java.io.File(str).mkdirs();
		return str;
	}
	
	//add by Samuel
	/**
	 * 得到类所在的包路径，以separator进行格式化
	 */
	public static String getPackagePathOfClass(Class clazz,String separator){
		String  path = clazz.getPackage().getName();
		return path.replaceAll("[.]", separator);
			
		
	}
}
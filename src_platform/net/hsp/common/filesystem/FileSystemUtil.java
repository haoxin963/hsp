package net.hsp.common.filesystem;

import net.hsp.common.PropConfigFactory;
import net.hsp.common.constants.PlatFormConstant;
 

public class FileSystemUtil extends PropConfigFactory
{
	//初始化构建静态类
	private final static FileSystemUtil	fUtil	= new FileSystemUtil();

	private FileSystemUtil()
	{
	}

	/**
	 * 构建单态模式
	 * @return
	 */
	public static FileSystemUtil getInstance()
	{
		return fUtil;
	}

	public static String getRoot(String custId){
		return FileSystemUtil.getInstance().getProperty(custId+".filesystem.root")+"/"+custId;		
	}
	
	/**
	 * 覆盖扩展类方法,指定读取文件系统文件
	 */
	@Override
	protected String getFile()
	{
		return PlatFormConstant.CONFIGFILEPATH+"/filesystem-conf.properties";
	}

}

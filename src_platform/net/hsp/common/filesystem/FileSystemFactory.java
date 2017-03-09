package net.hsp.common.filesystem;

import javax.servlet.http.HttpServletRequest;

import net.hsp.common.PathUtil;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
 

public class FileSystemFactory {
	
 
	
	private static String getCurrentSystemInstance()
	{
		return getCurrentSystemInstance(ActionUtil.getCtx().getRequest());
	}
	
	private static String getCurrentSystemInstance(HttpServletRequest request)
	{
		return HttpSessionFactory.getCustId(request); 
	}

	public static IFileSystemStream createSystemFileSystem(String middleType,String fileName) 
	{
		return createSystemFileSystem(getCurrentSystemInstance(),middleType,fileName,true);
	}
	
	/**
	 * 创建某公司站点系统配置文件的文件系统
	 * 
	 * @param 	instanceName	
	 * 			公司站点名
	 * @param 	fileName	
	 * 			要读取的文件名,为空无意义
	 * @param 	middleType	
	 * 			文件在三层中所属层次，取值为PlatFormConstant中的常量值
	 * @return
	 */
	public static IFileSystemStream createSystemFileSystem(String instanceName, String middleType,String fileName,boolean isLocal) 
	{		
		String moduleName="/com/hsp/"+middleType;
		IFileSystemStream ifs=FileSystemFactory.createFileSystem("system.config.file.system",instanceName,moduleName,fileName);	
		
		/*首先在公司站点下找文件，没有的情况下再在默认路径找,公司站点可能是远程FTP服务器，也可能是本地服务器*/
		if (!ifs.exists() && isLocal) 			
			return new FileSystemStream(PathUtil.getRootPath(),"",moduleName,fileName);							
		else
			return ifs;
	}
	
	/**
	 * 创建默认系统配置文件的文件系统，读取在应用服务器内部的默认系统配置文件
	 * 
	 * @param 	fileName	
	 * 			要读取的文件名,为空无意义
	 * @param 	middleType	
	 * 			文件在三层中所属层次，取值为PlatFormConstant中的常量值
	 * @return
	 */
	public static IFileSystemStream createDefaultSystemFileSystem(String middleType,String fileName) 
	{		
		String moduleName="/com/hsp/"+middleType;
		
		return new FileSystemStream(PathUtil.getRootPath(),"",moduleName,fileName);							
	}
	
	/**
	 * 创建当前站点的文件系统，文件的主路径为配置文件filesystem-conf.properties内的
	 * (当前站点.filesystem.root)值
	 * 
	 * @param 	fileName
	 * 			文件名称，若为空，则是对目录操作
	 * @return
	 */
	public static IFileSystemStream createFileSystem(String moduleName)
	{
		return createFileSystem(moduleName,null);
	}
	
	/**
	 * 创建当前站点的文件系统，文件的主路径为配置文件filesystem-conf.properties内的
	 * (当前站点.filesystem.root)值
	 * 
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @param 	fileName
	 * 			文件名称，若为空，则是对目录操作
	 * @return
	 */
	public static IFileSystemStream createFileSystem(String moduleName,String fileName)
	{
		return createRequestFileSystem(moduleName,fileName,null);
	}
	
	/**
	 * 创建带request请求的当前站点的文件系统，文件的主路径为配置文件filesystem-conf.properties内的
	 * (当前站点.filesystem.root)值
	 * 
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @param 	request
	 * 			通过servlet调用的文件系统都需要经过这个方法，取得request中的session以获得站点
	 * @return
	 */
	public static IFileSystemStream createRequestFileSystem(String moduleName,HttpServletRequest request)
	{
		return createRequestFileSystem(moduleName,null,request);
	}
	
	/**
	 * 创建带request请求的当前站点的文件系统，文件的主路径为配置文件filesystem-conf.properties内的
	 * (当前站点.filesystem.root)值
	 * 
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @param 	fileName
	 * 			文件名称，若为空，则是对目录操作
	 * @param 	request
	 * 			通过servlet调用的文件系统都需要经过这个方法，取得request中的session以获得站点
	 * @return
	 */
	public static IFileSystemStream createRequestFileSystem(String moduleName,String fileName,HttpServletRequest request)
	{
		if(request==null) 
		{
			return createFileSystem(getCurrentSystemInstance(),moduleName,fileName);
		}
		else 
		{
			return createFileSystem(getCurrentSystemInstance(request),moduleName,fileName);
		}
	}
	
	/**
	 * 创建某公司站点的文件系统，文件的主路径为配置文件filesystem-conf.properties内的
	 * (公司站点.filesystem.root)值，即在主体功能之上创建某公司的文件系统
	 * 
	 * @param 	instanceName
	 * 			公司站点名
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @param 	fileName
	 * 			文件名称，若为空，则是对目录操作
	 * @return
	 */
	public static IFileSystemStream createFileSystem(String instanceName,String moduleName,String fileName)
	{
		return createFileSystem(instanceName+".filesystem.root",instanceName,moduleName,fileName);
	}

	public static IFileSystemStream createFileSystem(String key,String instanceName,String moduleName,String fileName)
	{
		String root=FileSystemUtil.getInstance().getProperty(key);
		if(root==null)return null;
		if(root.startsWith("ftp://"))
		{
			return null;	
		} else 
		{
			return new FileSystemStream(root,instanceName,moduleName,fileName);			
		}
	}
	
	/**
	 * 判断文件系统路径是否存在，用于决定是否创建文件系统，因一旦创建文件系统，其路径会自动生成，
	 * 不适合随机目录的控制，该方法多用于此逻辑的控制
	 * 
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @return 	boolean
	 * 			true-目录存在，false-目录不存在
	 */
	public static boolean isDirectoryExists(String moduleName)
	{
		return isRequestDirectoryExists(moduleName,null); 	
	}
	
	/**
	 * 判断文件系统路径是否存在，用于决定是否创建文件系统，因一旦创建文件系统，其路径会自动生成，
	 * 不适合随机目录的控制，该方法多用于此逻辑的控制
	 * 
	 * @param 	moduleName
	 * 			文件系统下的自定义路径
	 * @param 	request
	 * 			通过servlet调用的文件系统都需要经过这个方法，取得request中的session以获得站点
	 * @return 	boolean
	 * 			true-目录存在，false-目录不存在
	 */
	public static boolean isRequestDirectoryExists(String moduleName,HttpServletRequest request)
	{
		if(request==null)
		{
			return isDirectoryExists(getCurrentSystemInstance(),moduleName); 
		}else
		{
			return isDirectoryExists(getCurrentSystemInstance(request),moduleName); 
		}
	}
	
	/**
	 * FileSystemFactory.isDirectoryExists方法的多态方法
	 * 
	 * @param 	instanceName
	 * 			公司站点名
	 * @param 	moduleName
	 *	 		文件系统下的自定义路径
	 * @return	boolean
	 * 			true-目录存在，false-目录不存在
	 */
	public static boolean isDirectoryExists(String instanceName,String moduleName)
	{
		String root=FileSystemUtil.getInstance().getProperty(instanceName+".filesystem.root");
		if(root.startsWith("ftp://"))
		{
			return  false;		
		} else 
		{
			return  FileSystemStream.isDirectoryExists(root,instanceName,moduleName);						
		}
	}

}

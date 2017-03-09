package net.hsp.web.util;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 文件上传公共类
 * @author JSmart
 *
 */
public class FileUploadUtil {
	 
	/**
	 * 所有上传文件的总长度最大值
	 */
	private long SUM_MAX_SIZE = 50 * 1024 * 1024;
	/**
	 * 允许上传的文件默认最大值
	 */
	private long MAX_SIZE = 10 * 1024 * 1024;
	
	/**
	 * 允许上传的文件类型
	 */
	private String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "txt","doc", "docx", "mp3", "wma", "m4a" };
	
	/**
	 * 文本框中参数
	 */
    private Map<String,List<String>> parameters;
	
	/**
	 * 设置输出字符集
	 */
	private String ENCODING = "UTF-8";
	
	/**
	 * 是否重命名
	 */
	private boolean isRname = false;
	
	/**
	 * 持久化位置
	 */
	private String path;
	
	private ProgressListener progressListener;
	
	private List filenames = new ArrayList();
	
	private List errorList = new ArrayList();
	
	public List getErrorList() {
		return errorList;
	}

	/**
	 * @param SUM_MAX_SIZE 允许所有文件总长度最大值，当赋值0时使用默认，默认值是50M, 为-1时无限制
	 * @param MAX_SIZE 允许单个上传文件最大长度是，当赋值0时使用默认，默认值10M，为-1时无限制
	 * @param ENCODING 输出字符集，默认UTF-8
	 * @param allowedExt 上传文件类型，当没有传送值时则表示任何类型文件都允许
	 * @param isRname 是否重命名，默认不重命名
	 * @param path 文件存放位置
	 */
	public FileUploadUtil(long SUM_MAX_SIZE,long MAX_SIZE,String ENCODING,String[] allowedExt,boolean isRname,String path,ProgressListener progressListener) {
		if (SUM_MAX_SIZE != 0){ 
			this.SUM_MAX_SIZE = SUM_MAX_SIZE;
		}
		if (MAX_SIZE != 0){ 
			this.MAX_SIZE = MAX_SIZE;
		}
		if (ENCODING != null){
			this.ENCODING = ENCODING;
		}
		this.allowedExt = allowedExt;
		this.isRname = isRname;
		this.path = path;
		
		if (progressListener!=null) {
			this.progressListener = progressListener;
		}
	}
	
	public FileUploadUtil() {
		
	}
	
	public FileUploadUtil upload(HttpServletRequest request,HttpServletResponse response) throws UploadException {
		RequestContext context = new ServletRequestContext(request);
		if(!ServletFileUpload.isMultipartContent(context)) {//判断form表单的传输方式是否是多数据类型
			throw new UploadException("您所提供表单类型不是多数据类型multipart-formdata");
		}
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		diskFileItemFactory.setSizeThreshold(1024*1024);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		diskFileItemFactory.setRepository(f);
		ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
		if (progressListener != null) {
			fileUpload.setProgressListener(progressListener);
		}
		fileUpload.setSizeMax(SUM_MAX_SIZE);//sizeMax = -1 没有上限
		fileUpload.setFileSizeMax(MAX_SIZE);
		fileUpload.setHeaderEncoding(ENCODING);//解决路径或者文件名为乱码的问题。
		try {
			List<FileItem> items = fileUpload.parseRequest(request);
			if (items == null || items.size() == 0) {
				throw new UploadException("没有检测到文件!");
			}
			parameters = new HashMap<String, List<String>>(); 
			String currentInstance = request.getParameter("currentInstance");
	 
			
			for (FileItem item: items) {
				File f2 = null;
				String fieldName = item.getFieldName();//文本框名称 <input name="">
				fieldName = URLDecoder.decode(fieldName,"UTF-8");
				if (item.isFormField()) {
					
				}else { //文件域
					long fileLength = item.getSize();
					if (fileLength == 0) {
						throw new UploadException("文件为空或非法文件！");
					}
					
					String userPath = URLDecoder.decode(item.getName(),"UTF-8");//完整路径 C:\sdss\ssss\sss.xls 
					boolean isjava = userPath.endsWith("java");
					if(true &&isjava){
						int count = 0;
						String[] deployInclude = PathCon.DEPLOYINCLUDE.split(",");
						String userPath_ = userPath.replaceAll("\\\\", ".");
						for (int i = 0; i < deployInclude.length; i++) {
							if(userPath_.indexOf(deployInclude[i])>-1){
								count++;
								break;
							}
						}
						if (count==0) {
							System.out.println("当前用户禁止写入此文件："+userPath);
							errorList.add("权限不足(或者为系统目录)：已阻止文件写入 "+userPath+"\n");
							continue;
						}
						userPath = userPath.replaceAll("\\\\","/");
						fieldName = fieldName.replaceAll("\\\\","/"); 
					
						f2 = new File(PathCon.SRCDIR+"/"+currentInstance+"/"+PathCon.SRCFOLDER,userPath.split(fieldName)[1]);
						File f1 = f2.getParentFile();
						f1.mkdirs();
						path = f1.getCanonicalPath();
						
					}else{
						String p = userPath.split(fieldName)[1];
						p = p.replaceAll("\\\\","/"); 
						if (p.startsWith("/view/")) {
							f2 = new File(PathCon.SYSVIEWDIR+"/",p);
							File f1 = f2.getParentFile();
							f1.mkdirs();
							path = f1.getCanonicalPath();
						}else if (p.startsWith("/page/")) {
							f2 = new File(PathCon.SYSVIEWDIR+"/",p);
							File f1 = f2.getParentFile();
							 
							f1.mkdirs();
							path = f1.getCanonicalPath();
						}else{
							throw new UploadException("没有此目录的写权限!"); 
						}
						
					}
					
					String fileName = userPath.substring(userPath.lastIndexOf("\\") + 1); //文件名 sss.xls
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); //XLS
					if (! (allowedExt == null || allowedExt.length == 0) ){
						boolean extBoo = false;
						for (String ext: allowedExt) {
							if (ext.toLowerCase().equals(fileExt)) {
								extBoo = true;
								break;
							}
						} 
						if (!extBoo){
							throw new UploadException("请选择正确得文件格式,包含" + Arrays.toString(allowedExt));
						}
					}
                    String filename =  URLDecoder.decode(item.getName(),"UTF-8");
                    if (isRname) {
                        filename = filename.substring(filename.lastIndexOf("."));
                        filename = System.currentTimeMillis() + filename;
					}else{						
	                    filename = filename.substring(filename.lastIndexOf(File.separator)+1);
					}   
                    if(isjava){
		                  //java文件检查容器里其它路径是否已经存在
		    				String pp =f2.getCanonicalPath();
		    				String name = FilenameUtils.getBaseName(pp);
		    				StringBuilder sb = new StringBuilder();
		    				sb.append(Character.toLowerCase(name.charAt(0)));
		    				sb.append(name.substring(1));
		    				name = sb.toString();
		    				WebApplicationContext beanContext = null;
		    				if (name.endsWith("Controller")) {
		    					beanContext = (WebApplicationContext) request.getSession().getServletContext().getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring");
		    				} else if (name.endsWith("ServiceImpl")) {
		    					beanContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		    				}
		    				Object obj = null;
		    				try {
		    					obj = beanContext.getBean(name);
		    				} catch (Exception e) {
		    					System.out.println("bean加载异常:" + name + ":" + e.getMessage());
		    				}
		    				if (obj != null) { // 类不在一个包下，但重名时
		    					String temp = pp.replaceAll("\\\\", "/");
		    					
		    					String temp2 = PathCon.SRCDIR + "/" + currentInstance + "/" + PathCon.SRCFOLDER;
		    					temp2 = temp2.replaceAll("\\\\", "/"); 
		    					temp = temp.toString().split(temp2)[1];
		    					temp = temp.substring(1, temp.indexOf(".java"));
		    					temp = temp.replaceAll("\\/", "\\."); 
		    					
		    					
		    					if (obj.getClass().getName().indexOf("CGLIB$$") == -1 && !obj.getClass().getName().startsWith("com.sun.proxy") && !obj.getClass().getName().startsWith("$Proxy") && !obj.getClass().getName().equals(temp + "")) {
		    						throw new RuntimeException("部署中心:预处理时发现类(beanId)重名,建议重命名=>" + obj.getClass().getName() + "与" + temp);
		    					}
		    				}
                    }
				}
			}
			
			for (FileItem item: items) {
				File f2 = null;
				String fieldName = item.getFieldName();//文本框名称 <input name="">
				fieldName = URLDecoder.decode(fieldName,"UTF-8");
				if (item.isFormField()) {
					String value = item.getString(ENCODING);//文本框的值解决获取的表单字段为乱码的问题。
					List<String> values;
					if (parameters.get(fieldName) != null) {
                        values = parameters.get(fieldName);
                    } else {
                        values = new ArrayList<String>();
                    }
					values.add(value);
					parameters.put(fieldName, values);
				}else { //文件域
					long fileLength = item.getSize();
					if (fileLength == 0) {
						throw new UploadException("文件为空或非法文件！");
					}
					String userPath = URLDecoder.decode(item.getName(),"UTF-8");//完整路径 C:\sdss\ssss\sss.xls 
					if(true && userPath.endsWith("java")){
						int count = 0;
						String[] deployInclude = PathCon.DEPLOYINCLUDE.split(",");
						String userPath_ = userPath.replaceAll("\\\\", ".");
						for (int i = 0; i < deployInclude.length; i++) {
							if(userPath_.indexOf(deployInclude[i])>-1){
								count++;
								break;
							}
						}
						if (count==0) {
							System.out.println("当前用户禁止写入此文件："+userPath);
							errorList.add("权限不足(或者为系统目录)：已阻止文件写入 "+userPath+"\n");
							continue;
						}
						userPath = userPath.replaceAll("\\\\","/");
						fieldName = fieldName.replaceAll("\\\\","/"); 
					
						f2 = new File(PathCon.SRCDIR+"/"+currentInstance+"/"+PathCon.SRCFOLDER,userPath.split(fieldName)[1]);
						File f1 = f2.getParentFile();
						f1.mkdirs();
						path = f1.getCanonicalPath();
						
					}else{
						String p = userPath.split(fieldName)[1];
						p = p.replaceAll("\\\\","/"); 
						if (p.startsWith("/view/")) {
							f2 = new File(PathCon.SYSVIEWDIR+"/",p);
							File f1 = f2.getParentFile();
							f1.mkdirs();
							path = f1.getCanonicalPath();
						}else if (p.startsWith("/page/")) {
							f2 = new File(PathCon.SYSVIEWDIR+"/",p);
							File f1 = f2.getParentFile();
							 
							f1.mkdirs();
							path = f1.getCanonicalPath();
						}else{
							throw new UploadException("没有此目录的写权限!"); 
						}
						
					}
					
					String fileName = userPath.substring(userPath.lastIndexOf("\\") + 1); //文件名 sss.xls
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); //XLS
					if (! (allowedExt == null || allowedExt.length == 0) ){
						boolean extBoo = false;
						for (String ext: allowedExt) {
							if (ext.toLowerCase().equals(fileExt)) {
								extBoo = true;
								break;
							}
						} 
						if (!extBoo){
							throw new UploadException("请选择正确得文件格式,包含" + Arrays.toString(allowedExt));
						}
					}
                    String filename =  URLDecoder.decode(item.getName(),"UTF-8");
                    if (isRname) {
                        filename = filename.substring(filename.lastIndexOf("."));
                        filename = System.currentTimeMillis() + filename;
					}else{						
	                    filename = filename.substring(filename.lastIndexOf(File.separator)+1);
					} 
                    //System.out.println("write path:"+f2.getCanonicalPath());
                    filenames.add(f2.getCanonicalPath()); 
                    
                    
                    item.write(f2);
				}
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			throw new UploadException("01-请求总数据量超出了规定的大小",e);
		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			throw new UploadException("02-单个文件超出了规定的大小",e);
		} catch (FileUploadBase.IOFileUploadException e) {
			throw new UploadException("03-文件传输出现错误,例如磁盘空间不足等");
		} catch (FileUploadBase.InvalidContentTypeException e) {
			throw new UploadException("04-无效的请求类型,即请求类型enctype != \"multipart/form-data\"");
		} catch (FileUploadException e) {
			throw new UploadException("00-上传失败,原因不明.",e);
		} catch (UnsupportedEncodingException e) {
			throw new UploadException("您提供编码不能识别["+ENCODING+"]",e);
		}catch(Exception e){  
			e.printStackTrace();
			throw new UploadException(e.getMessage(),e);
		}
		return this;
	}
	

	public String getParameter(String name) {
		List<String> v = (List<String>) parameters.get(name);
		if (v != null && v.size() > 0) {
			return v.get(0);
		}
		return null;
	}

	public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }
	
	public String[] getParameterValues(String name) {
        List<String> v = parameters.get(name);
        if (v != null && v.size() > 0) {
            return (String[]) v.toArray(new String[v.size()]);
        }
        return null;
    }
	

	public Map<String, List<String>> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, List<String>> parameters) {
		this.parameters = parameters;
	}
	
	public static class UploadException extends Exception{
		private static final long serialVersionUID = -6797554437005471992L;
		
		public UploadException() {
			super();
		}
		public UploadException(String message, Throwable cause) {
			super(message, cause);
		}

		public UploadException(String message) {
			super(message);
		}

		public UploadException(Throwable cause) {
			super(cause);
		}
	}

	public List getFilenames() {
		return filenames;
	}
}

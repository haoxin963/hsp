package net.hsp.web.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.web.util.FileUploadUtil.UploadException;

public class UploadUtil extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UploadUtil() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

 
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String callback = request.getParameter("CKEditorFuncNum");//获取回调JS的函数Num
		String path = request.getContextPath() + "/";
		String type = request.getParameter("type");
		if(type == null || "".equals(type)){
			type = "";
		}else{
			type = type.toLowerCase() + "/";
		}
		String uploadPath = "files/" + type;
		String absPath = request.getRealPath("/")+ uploadPath;
		if(callback !=null && !"".equals(callback)){//ckeditor 
			String clientPath = path + uploadPath;
	 		FileUploadUtil fileUploadUtil = new FileUploadUtil(500 * 1024 * 1024, 1000 * 1024 * 1024, "UTF-8", new String[] { "zip", "jpg", "doc", "rar", "gif","png" }, true, absPath, null);
			try {
				fileUploadUtil.upload(request,response);
			} catch (UploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String fileName = fileUploadUtil.getFilenames().get(0)+"";
			clientPath +=fileName;
			//打印一段JS，调用parent页面的CKEditor的函数，传递函数编号和上传后文件的路径； 
			out.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("+callback+",'"+clientPath+"')</script>");
		}else{//swfupload
			try {
				FileUploadUtil fileUploadUtil = new FileUploadUtil(500 * 1024 * 1024, 1000 * 1024 * 1024, "UTF-8", new String[] { "zip", "jpg", "doc", "rar", "gif","png" }, true, absPath, null);
				fileUploadUtil.upload(request,response);
				System.out.print(fileUploadUtil.getParameters());
				//fileUploadUtil = null;
				String fileName = fileUploadUtil.getFilenames().get(0)+"";
				out.print("successed"+"|"+fileName);
			} catch (Exception e) {
				out.print(e.getMessage());
			} 
		}

		
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

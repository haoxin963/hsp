package net.hsp.web.util;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.dao.DynamicDataSource;
import net.hsp.web.constants.WebConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
 

public class MV extends ModelAndView {
	static String realPath = "E:\\xxx\\dev\\tomcat6\\webapps";
	static String ctxpath = "/uisp3";
	static String custId = "dev0";
	static String viewFolderName = "view";
	static String extViewFolderName = "page";

	private String viewName;
	private Object modelObject;
	private String command;
	private Object actionObj; 
	private static final Log logger = LogFactory.getLog(MV.class);
	public MV() {
		super();
	}
	
	public MV(String viewName) {   
	    super(getPath(viewName,RequestContextHolder.getRequestAttributes()==null ? null :((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest())); 
		this.viewName = viewName;
		
	}
	
	public MV(String viewName,Object actionObj) { 
		super(getPath(viewName,((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()));
		this.actionObj = actionObj;
		this.viewName = viewName;
	}

	public MV(String viewName, Map modelObject) {
		super(getPath(viewName,((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()),modelObject);
		this.viewName = viewName;
		this.modelObject = modelObject;
		
	}


	public MV(String viewName, String modelName, Object modelObject) {
		super(getPath(viewName,((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()), modelName, modelObject);
		this.viewName = viewName;
		this.modelObject = modelObject;
		this.command = modelName;
	}

	
	public MV(String viewName, String modelName, Object modelObject,Object actionObj) { 
		super(getPath(viewName,((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()), modelName, modelObject);
		this.viewName = viewName;
		this.modelObject = modelObject;
		this.command = modelName;
		this.actionObj = actionObj;
	}
  
	/**
	 * 将url转换为带view或者page目录
	 */
	public static String getPath(String url,HttpServletRequest req) {
		if(req==null){
			return url;
		}
		if (url==null || url.startsWith("redirect:")) {
			return url;
		}
		String path = url; 
		custId = DynamicDataSource.getCustId();
		path = path.replaceAll(ctxpath + "/", "");
		path = path.replaceAll(".jspx", ".jsp");

		String bizFolder = path.split("/")[0];
		String custPath = path.replaceAll(bizFolder + "/", custId + "/" + bizFolder + "/");
		custPath = extViewFolderName + custPath;
		String sysPath = viewFolderName + path;
		custPath = "page/" + custId + url;  
		realPath = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getRealPath("/");
		File f = new File(realPath +custPath+".jsp");
		try {
			if (!f.exists()) {
				req.setAttribute("prefix", req.getContextPath()+"/view");
				return sysPath;
			} else {
				req.setAttribute("prefix", req.getContextPath()+"/page/"+custId);
				return custPath;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	private ModelAndView redirect(HttpServletRequest req, HttpServletResponse res) {

		String url = req.getRequestURI();
		if (url.endsWith(".do")) {
			String custId = "dev0";
			String viewFolderName = "";
			String extViewFolderName = "page";
			// System.out.println("查看路径:"+realPath+"page"+path);
			String path = this.viewName; 
			path = path.replaceAll(req.getContextPath() + "/", "");
			path = path.replaceAll(".jspx", ".jsp");

			String bizFolder = path.split("/")[0];
			String custPath = path.replaceAll(bizFolder + "/", bizFolder + "/" + custId + "/");
			custPath = extViewFolderName + "/" + custPath; 
			String sysPath = path;

			String realPath = req.getSession().getServletContext().getRealPath("/");

			File f = new File(realPath + custPath);
			try {
				if (!f.exists()) {
					// req.getRequestDispatcher("/"+sysPath).forward(req, res);
					System.out.println("系统path" + sysPath);
					return new ModelAndView(new RedirectView(req.getContextPath() + "/" + sysPath + ".jspx"), command, modelObject);
				} else {
					// req.getRequestDispatcher("/"+custPath).forward(req, res);
					System.out.println(custPath);
					System.out.println("custPath" + custPath);

					return new ModelAndView(new RedirectView(req.getContextPath() + "/" + custPath + ".jspx"), command, modelObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else {
			if (modelObject != null) {
				try {
					PrintWriter out = res.getWriter(); 
					if (modelObject instanceof java.util.Collections) {
						out.println(JSONArray.fromObject(modelObject));
					}  else {
						out.println(JSONObject.fromObject(modelObject));
					}
					out.close();
				} catch (IOException e) { 
					e.printStackTrace();
				}
			}
			return null;
		}
	}
 
	
	
	/**
	 * 
	 * @param e 原始异常
	 * @param errorInfo 自定义错误提示信息，可以直接传中文信息，也可以传I18N key
	 * @return
	 */
	public ModelAndView processException(Exception e,String... errorInfo) { 
		HttpServletRequest req =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String url = req.getRequestURI();
		logger.error("error",e); 
		if (url.endsWith(".do")) { 
			//if (e instanceof DataAccessException) {
			try { 
				HttpServletResponse res =ActionUtil.getCtx().getResponse(); 
				/*
				res.setCharacterEncoding("utf-8");
				res.setContentType("text/html;charset=UTF-8");
				PrintWriter out = res.getWriter(); 
				Map m = new HashMap();
				m.put("status","500");
				m.put("msg",StringUtils.isNotBlank(errorInfo) ? errorInfo : "操作出错!");
				out.println(JSONObject.fromObject(m).toString());
				*/ 
				this.setViewName("/com/error/error");
				return this;
			} catch (Exception e2) {
				logger.error("error",e2); 
			}
		}else if(url.endsWith(".pdfx")){ 
			return null;
		}else if(url.endsWith(".xlsx")){ 
			
			return null;
		} else {  
			
			try {
				HttpServletResponse res =ActionUtil.getCtx().getResponse();
				res.setContentType("text/html;charset=UTF-8");
				PrintWriter out = res.getWriter();  
				if (e instanceof DataAccessException) {
					Map m = new HashMap();
					m.put("status","500");
					if (errorInfo!=null &&  errorInfo.length>0) {
						m.put("msg",errorInfo[0]);
					}else{
						m.put("msg","操作出错!");
					}
					
					out.println(JSONObject.fromObject(m).toString());
				}else{
					Map m = new HashMap();
					m.put("status","500");
					if (errorInfo!=null && errorInfo.length>0) {
						m.put("msg",errorInfo[0]);
					}else{
						m.put("msg","操作出错!");
					}
					out.println(JSONObject.fromObject(m).toString());
				}
				out.close(); 
				
			} catch (IOException ex) {
				logger.error("error",ex); 
			}
		}
		return this;
	}
	
	public ModelAndView processException(String errorInfo) { 
		HttpServletRequest req =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String url = req.getRequestURI();
		if (url.endsWith(".do")) { 
			//if (e instanceof DataAccessException) {
			try { 
				HttpServletResponse res =ActionUtil.getCtx().getResponse(); 
				 
				this.setViewName("/com/error/error");
				return this;
			} catch (Exception e2) {
				logger.error("error",e2); 
			}
		}else if(url.endsWith(".pdfx")){ 
			return null;
		}else if(url.endsWith(".xlsx")){ 
			
			return null;
		} else {   
			try {
				HttpServletResponse res =ActionUtil.getCtx().getResponse();
				res.setContentType("text/html;charset=UTF-8");
				PrintWriter out = res.getWriter();  
				 
					Map m = new HashMap();
					m.put("status","500");
					m.put("msg",StringUtils.isNotBlank(errorInfo) ? errorInfo : "操作出错!");
					out.println(JSONObject.fromObject(m).toString());
				 
				out.close(); 
				
			} catch (IOException ex) {
				logger.error("error",ex); 
			}
		}
		return null;
	}
	 
	private ModelAndView _fwd(String url) { 
		try { 
			//BatchResponseAction.set(url, this.getModelMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 会根据请求后缀名自动识别，如json,do,xlsx,pdfx,当是json后缀时会将model中的数据序列化成json输出,当.do时转发到jsp页面自定义去展示数据
	 * 是forward的替代方法
	 * @return ModelAndView
	 */
	public ModelAndView fwd() { 
		if(RequestContextHolder.getRequestAttributes()==null){
			return null;
		}
		HttpServletRequest req =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//批量处理多个地址的请求
		String batchURL = (String)req.getAttribute("batchURL");
		if(batchURL!=null){
			return _fwd(batchURL);
		}
		String url = req.getRequestURI();
		if (url.endsWith(".do")) { 
			if ("0".equals(req.getParameter("fwd"))) {
				try {
					HttpServletResponse res =ActionUtil.getCtx().getResponse(); 
					PrintWriter out = res.getWriter(); 
					if (modelObject instanceof Collection) {
						out.println(JSONArray.fromObject(modelObject));
					}  else {
						out.println(JSONObject.fromObject(modelObject));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;

			} else {
				if (actionObj!=null) {
					req.setAttribute("actionObj", actionObj);
				}
				return this;
			}
		}else if(url.endsWith(".pdfx")){ 
			return new ModelAndView(new ViewPDF(),this.getModelMap());
		}else if(url.endsWith(".xlsx")){ 
			
			return new ModelAndView(new ViewExcel(),this.getModelMap());
		} else {
			Map m = this.getModelMap(); 
			HttpServletResponse res =ActionUtil.getCtx().getResponse();	
			res.setContentType("text/html;charset=UTF-8");
			try {
				PrintWriter out = res.getWriter(); 
				JsonConfig cfg = new JsonConfig(); 
				cfg.registerJsonValueProcessor(java.sql.Date.class, new net.hsp.web.util.JsonDateValueProcessor());  
	 			cfg.registerJsonValueProcessor(java.util.Date.class, new net.hsp.web.util.JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss")); 
	 			cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new net.hsp.web.util.JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
	 			if (m.get("status")==null) {
					m.put("status", "1");
				}
				out.println(JSONObject.fromObject(m,cfg).toString());
				out.close();
			} catch (Exception e) {
				PrintWriter out;
				try {
					out = res.getWriter();
					if (m.get("status")==null) {
						m.put("status", "500");
						m.put("msg", "出错了!");
					}
					out.println(JSONObject.fromObject(m).toString());
				} catch (IOException e1) { 
					e1.printStackTrace();
				} 
			}
			return this;
		}
	}
	
	
	/**
	 * 方法已过时
	 * 建议使用fwd方法，fwd()兼容此方法所有特性,参见fwd()
	 * @return ModelAndView
	 */
	@Deprecated
	public ModelAndView forward() { 
		HttpServletRequest req =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		String url = req.getRequestURI();
		if (url.endsWith(".do")) { 
			if ("0".equals(req.getParameter("fwd"))) {
				try {
					HttpServletResponse res =ActionUtil.getCtx().getResponse();
					PrintWriter out = res.getWriter(); 
					if (modelObject instanceof Collection) {
						out.println(JSONArray.fromObject(modelObject));
					}  else {
						out.println(JSONObject.fromObject(modelObject));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;

			} else {
				if (actionObj!=null) {
					req.setAttribute("actionObj", actionObj);
				}
				return this;
			}
		}else if(url.endsWith(".pdfx")){ 
			return new ModelAndView(new ViewPDF(),(Map) modelObject);
		}else if(url.endsWith(".xlsx")){ 
			
			return new ModelAndView(new ViewExcel(),(Map) modelObject);
		} else {
			if (modelObject != null) {
				try {
					HttpServletResponse res =ActionUtil.getCtx().getResponse();
					res.setContentType("text/html;charset=UTF-8");
					PrintWriter out = res.getWriter(); 
					JsonConfig cfg = new JsonConfig(); 
					cfg.registerJsonValueProcessor(java.sql.Date.class, new net.hsp.web.util.JsonDateValueProcessor());  
		 			cfg.registerJsonValueProcessor(java.util.Date.class, new net.hsp.web.util.JsonDateValueProcessor()); 
		 			cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new net.hsp.web.util.JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
					
					if (modelObject instanceof Collection) {
						out.println(JSONArray.fromObject(modelObject,cfg));
					}  else {
						out.println(JSONObject.fromObject(modelObject,cfg));
					}
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
	}
	
	public void addErrorInfo(String msg){
		this.addObject("msg", msg);
		this.addObject("status", 0);
	}
	
	public void addObj(Object obj){
		this.addObject(WebConstant.COMMAND,obj); 
	}
	 
}

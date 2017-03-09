package net.hsp.web.sys.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.tool.Tool;
import net.hsp.entity.sys.tool.Toolcolor;
import net.hsp.service.sys.tool.ToolService;
import net.hsp.service.sys.tool.ToolcolorService;
import net.hsp.service.sys.tool.ToolstyleService;
import net.hsp.util.ReadFile;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_toolcolor_tbl控制器
 * @author lk0513
 */
@Controller
@WebLogUtil(name = "pubmodule_toolcolor_tbl")
@RequestMapping("/sys/tool/toolcolor")
@Lazy(true)
public class ToolcolorController  {

	private String listURL = "/sys/tool/toolcolorList";
	private String formURL = "/sys/tool/toolcolorForm";
	
	@Autowired
	private ToolcolorService toolcolorService; 
	
	@Autowired
	private ToolService toolService; 
	@Autowired
	private ToolstyleService toolstyleService; 
	
	private final Logger log = Logger.getLogger(ToolcolorController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Tool obj,@RequestParam(value="userid",required=false) String userid,
			@RequestParam(value="ss",required=false) String ss) {
		try {
			toolcolorService.delete(userid, obj.getId());
			List<Map<String, Object>> list = JSONArray.fromObject(ss);
			for(Map<String,Object> map:list){
				 Toolcolor obj2=new Toolcolor();
		    	 obj2.setStyleid(Integer.parseInt(map.get("id").toString()));
		    	 obj2.setUserid(Integer.parseInt(userid));
		    	 obj2.setToolid(obj.getId());
		    	 obj2.setStylevalue(map.get("value").toString());
				 toolcolorService.save(obj2);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Toolcolor obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				toolcolorService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				toolcolorService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Tool obj,HttpServletRequest res){
		try{  
			//根据id(pk)从DB加载 
			String userName = (String)HttpSessionFactory.getAttribute(res, "userName");
			String userid=toolcolorService.findbyUsername(userName).get("id").toString();
		    List<Map<String,Object>> list=toolstyleService.findByToolid(obj.getId());
		    for(Map<String,Object> map:list){
		    	Map<String,Object> map2=toolcolorService.find(obj.getId(), userid, map.get("id").toString());
		    	 if(map2!=null){
		    		 map.put("stylevalue", map2.get("stylevalue").toString());
		    	 }
			}
			MV mv = new MV(formURL,WebConstant.COMMAND, obj); 
			
			//相关初始数据加载 
			//mv.addObject("x","yy");
            mv.addObject("list", list);
            mv.addObject("userid", userid);
			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Toolcolor toolcolor, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, toolcolorService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="创建css文件")
	@RequestMapping("/doCreat")
	public ModelAndView doCreat(HttpServletRequest res) {
		try {
			String userName = (String)HttpSessionFactory.getAttribute(res, "userName");
			String userid=toolcolorService.findbyUsername(userName).get("id").toString();
			String content="";
			List<Map<String,Object>> list=toolcolorService.list();
			for(Map<String,Object> map:list){
				    content+=map.get("toolclass").toString()+"{";
				    List<Map<String,Object>> list2=toolstyleService.findByToolid(Integer.parseInt(map.get("id").toString()));
				    if(list2.size()!=0){
				    	 for(Map<String,Object> map2 :list2){
				    		 String stylevalue=map2.get("stylevalue").toString();   
				    		 String stylefunction=map2.get("stylefunction").toString();
				    		 Map<String,Object> map3=toolcolorService.find(Integer.parseInt(map.get("id").toString()), userid, map2.get("id").toString());
				    		    if(map3!=null){
				    		    	stylevalue=map3.get("stylevalue").toString();
				    		    }
				    		    stylefunction=stylefunction.replaceAll("\\*", stylevalue);
				    		    content+=stylefunction;
				    	 }
				    }
				    content+="}";
			}
		 
			String path=res.getSession().getServletContext().getRealPath("/")+"resource/h-ui/skin/zdy/"+userid+".css";
			ReadFile.createFile(path, content);
			String css="/zdy/"+userid+".css";
			toolcolorService.updateusertool(userid, css);
			return new MV(listURL, WebConstant.COMMAND, list).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="读取文件夹下所有图片")
	@RequestMapping("/imglist")
	public ModelAndView imglist(@RequestParam(value="filepath",required=false) String filepath,HttpServletRequest res) {
		  try{
			  List<String> list=new ArrayList<String>();
			 
			  filepath=res.getSession().getServletContext().getRealPath("/")+filepath;
		
			  File[] files = new File(filepath).listFiles();
		        for (int i = 0; i < files.length; i++)
		        {
		            if(files[i].getName().replaceAll("(.jpg|.png|.bmp|.gif)+","").length()!=files[i].getName().length())
		            {
		                 list.add(files[i].getName());
		            }
		        }
		  
		        MV mv=new MV(formURL);
		        mv.addObject("imglist", list);
		        return mv.fwd();
		  }catch (Exception e) {
				return new MV().processException(e,null);
			}
	}
	
	@MethodLogUtil(type="",value="读取文件夹下所有图片")
	@RequestMapping("/loadCss")
	public ModelAndView loadCss(@RequestParam(value="filepath",required=false) String filepath,HttpServletRequest res) {
        try{
       	    String userName = (String)HttpSessionFactory.getAttribute(res, "userName");
			String userid=toolcolorService.findbyUsername(userName).get("id").toString();
			String css="default/skin.css";
			Map<String,Object> map=null;
        	 if(StringUtils.isBlank(filepath)){
        		 map=toolcolorService.findbyUserid(userid);
        		 if(map!=null){
        			 css=map.get("usercss").toString();
        		 }else{
        			 toolcolorService.insertusertool(userid, css);
        		 }
        	 }else{
        		 if(filepath.equals("zdy")){
        			 css="zdy/"+userid+".css";
        		 }else{
        			 css=filepath+"/skin.css";
        		 }
        		  toolcolorService.updateusertool(userid, css);
        	 }
        	 HttpSession session = res.getSession();
        	 session.setAttribute("skincss", css);
		     MV mv=new MV(formURL);
             mv.addObject("cssname", css);
             return mv.fwd();
       }catch (Exception e) {
		     return new MV().processException(e,null);
	   }
		
	
	}
}

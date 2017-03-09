package net.hsp.web.sys.tool;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.tool.Toolstyle;
import net.hsp.service.sys.tool.ToolstyleService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_toolstyle_tbl控制器
 * @author lk0513
 */
@Controller
@WebLogUtil(name = "pubmodule_toolstyle_tbl")
@RequestMapping("/sys/tool/toolstyle")
@Lazy(true)
public class ToolstyleController  {

	private String listURL = "/sys/tool/toolstyleList";
	private String formURL = "/sys/tool/toolstyleForm";
	
	@Autowired
	private ToolstyleService toolstyleService; 
 
	private final Logger log = Logger.getLogger(ToolstyleController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Toolstyle obj) {
		try {
			if (obj.getId()!=null) {
				toolstyleService.update(obj); 
			}else{
				toolstyleService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Toolstyle obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				toolstyleService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				toolstyleService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Toolstyle obj){
		try{  
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (Toolstyle) toolstyleService.findById(obj); 
			}
			MV mv = new MV(formURL,WebConstant.COMMAND,obj); 
			
			//相关初始数据加载 
			//mv.addObject("x","yy");

			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Toolstyle toolstyle, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, toolstyleService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

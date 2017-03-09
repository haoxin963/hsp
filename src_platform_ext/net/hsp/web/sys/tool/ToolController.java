package net.hsp.web.sys.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.tool.Tool;
import net.hsp.service.sys.tool.ToolService;
import net.hsp.service.sys.tool.ToolstyleService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_tool_tbl控制器
 * @author lk0513
 */
@Controller
@WebLogUtil(name = "pubmodule_tool_tbl")
@RequestMapping("/sys/tool/tool")
@Lazy(true)
public class ToolController  {

	private String listURL = "/sys/tool/toolList";
	private String formURL = "/sys/tool/toolForm";
	
	@Autowired
	private ToolService toolService; 
	
	@Autowired
	private ToolstyleService toolstyleService; 
	
	private final Logger log = Logger.getLogger(ToolController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Tool obj,@RequestParam(value="styleid",required=false) String styleid) {
		try {
			if (obj.getId()!=null) {
				toolstyleService.delete(obj.getId());
				toolService.update(obj); 
			}else{
			    obj=(Tool)toolService.save(obj);
			}
			String[] sid=styleid.split(",");
			for(int i=0;i<sid.length;i++){
				toolstyleService.insert(obj.getId(), sid[i]);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Tool obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				toolService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				toolService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Tool obj){
		try{  
			//根据id(pk)从DB加载 
			List<Map<String,Object>> list=toolstyleService.queryall();
			
			if (obj.getId()!=null) {
				obj = (Tool) toolService.findById(obj); 
				for(Map<String,Object> map:list){
					   if(toolstyleService.check(obj.getId(), map.get("id").toString())!=null){
						   map.put("check", 1);
					   }
					  
				}
			}
			MV mv = new MV(formURL,WebConstant.COMMAND,obj); 			
			mv.addObject("stylelist", list);
			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Tool tool, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, toolService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

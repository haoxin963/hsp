package net.hsp.web.sys.select;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.select.Filter;
import net.hsp.service.sys.select.FilterService;
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
 * pubmodule_selectfilter_tbl控制器
 * @author smartTools
 */
@Controller
@WebLogUtil(name = "pubmodule_selectfilter_tbl")
@RequestMapping("/sys/select/filter")
@Lazy(true)
public class FilterController  {

	private String listURL = "/sys/select/filterList";
	private String formURL = "/sys/select/filterForm";
	
	@Autowired
	private FilterService filterService; 
 
	private final Logger log = Logger.getLogger(FilterController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Filter obj) {
		try {
			if (obj.getId()!=null) {
				filterService.update(obj); 
			}else{
				filterService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Filter obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				filterService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				filterService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Filter obj){
		try{  
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (Filter) filterService.findById(obj); 
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
	public ModelAndView doList(Filter filter, FilterMap filterMap, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, filterService.query(filterMap.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

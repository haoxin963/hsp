package net.hsp.web.sys.basedata;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.basedata.Area;
import net.hsp.service.sys.basedata.AreaService;
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
 * 区域控制器
 * @author lk0182
 */
@Controller
@WebLogUtil(name = "区域")
@RequestMapping("/sys/basedata/area")
@Lazy(true)
public class AreaController  {

	private String listURL = "/sys/basedata/areaList";
	private String formURL = "/sys/basedata/areaForm";
	
	@Autowired
	private AreaService areaService; 
 
	private final Logger log = Logger.getLogger(AreaController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Area obj) {
		try {
			if (StringUtils.isNotBlank(obj.getAreaId())) {
				areaService.update2(obj); 
			}else{
				obj.setDelTag("0");
				areaService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Area obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				areaService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				areaService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Area obj){
		try{  
			//根据id(pk)从DB加载 
			if (obj.getAreaId()!=null) {
				obj = areaService.findById(obj); 
			}
			System.out.println(obj);
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
	public ModelAndView doList(Area area, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, areaService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

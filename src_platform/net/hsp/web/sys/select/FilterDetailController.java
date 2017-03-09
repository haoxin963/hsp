package net.hsp.web.sys.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.select.FilterDetail;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.service.sys.select.FilterDetailService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_selectfilterdetail_tbl控制器
 * @author smartTools
 */
@Controller
@WebLogUtil(name = "pubmodule_selectfilterdetail_tbl")
@RequestMapping("/sys/select/filterDetail")
@Lazy(true)
public class FilterDetailController  {
	
	private String listURL = "/sys/select/filterDetailList";
	private String formURL = "/sys/select/filterDetailForm";
	
	@Autowired
	private FilterDetailService filterDetailService; 
 
	private final Logger log = Logger.getLogger(FilterDetailController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(FilterDetail obj) {
		try {
			String filterValues = obj.getFilterValue();
			obj.setFilterValue("");
			filterDetailService.deleteByFilterId(obj);
			if(!StringUtil.isEmpty(filterValues)){
				List<FilterDetail> list = new ArrayList<FilterDetail>();
				String[] values = filterValues.split(",");
				for(String x:values){
					FilterDetail filterDetail = obj;
					filterDetail.setFilterValue(x+"");
					list.add(filterDetail);
				}
				filterDetailService.addFilterDetails(list);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(FilterDetail obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				filterDetailService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				filterDetailService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(FilterDetail obj){
		try{
			int filterId = Integer.valueOf(ActionUtil.getCtx().getRequest().getParameter("filterId"));
			String filterType = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("filterType")).toLowerCase();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if(PlatFormConstant.FILTER_TYPE_ROLE.equals(filterType)){
				//角色过滤
				list=filterDetailService.getSelectRoleList(filterId);
				Map root = new HashMap();
				root.put("id", 0);
				root.put("name","角色过滤");
				root.put("pId", -0);
				root.put("nocheck",true);
				list.add(root);
			}
			if(PlatFormConstant.FILTER_TYPE_DEPT.equals(filterType)){
				//部门过滤
				list=filterDetailService.getSelectDeptList(filterId);
			}
			if(PlatFormConstant.FILTER_TYPE_GROUP.equals(filterType)){
				//群组过滤
				
			}
			if(PlatFormConstant.FILTER_TYPE_USER.equals(filterType)){
				//用户过滤
				list=filterDetailService.getSelectUserList(filterId);
			}
			Map map = new HashMap();
			map.put("dataList", list);
			map.put("filterId", filterId);
			map.put("filterType", filterType);
			MV mv = new MV(formURL,WebConstant.COMMAND,JSONObject.fromObject(map));
			
			//相关初始数据加载 
			//mv.addObject("x","yy");

			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}
	
	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(FilterDetail filterDetail, FilterMap filter, PageInfo pageInfo) {
		try {
			int filterId = Integer.valueOf(ActionUtil.getCtx().getRequest().getParameter("filterId"));
			
			return new MV(listURL, WebConstant.COMMAND, filterDetailService.query(filterId,filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
}

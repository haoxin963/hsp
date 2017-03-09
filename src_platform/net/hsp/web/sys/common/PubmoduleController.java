package net.hsp.web.sys.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ReflectionUtils;
import net.hsp.dao.DynamicDataSource;
import net.hsp.service.BaseService;
import net.hsp.service.sys.org.DepartmentService;
import net.hsp.service.sys.org.EmployeeService;
import net.hsp.service.sys.rbac.RoleService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/system/pubmodule")
public class PubmoduleController {

	@Autowired
	@Qualifier("baseServiceImpl")
	protected BaseService baseService;
	
	@Autowired 
	protected DepartmentService departmentService;
	
	@Autowired 
	protected RoleService roleService;
	
	@Autowired
	private EmployeeService employeeService; 

	@MethodLogUtil(type = "", value = "doSyncValidate")
	@RequestMapping("/doSyncValidate")
	@ResponseBody
	public boolean doSyncValidate(FilterMap filter) {

		return (Boolean) baseService.syncValidate(filter).get("result");
	}

	@MethodLogUtil(type = "", value = "doAutocomplete")
	@RequestMapping("/doAutocomplete")
	@ResponseBody
	public List doAutocomplete(FilterMap filter, @RequestParam(value = "q", required = true)
	String q) {
		filter.getFilter().put("q", q);
		return baseService.autocomplete(filter);
	}

	@MethodLogUtil(type = "1", value = "doTree")
	@RequestMapping("/doTree") 
	public ModelAndView doTree(FilterMap filter, PageInfo pageInfo) {
		Map<String,String> param = filter.getFilter();
		String serviceBeanId = param.get("serviceBeanId");
		String serviceMethod = param.get("serviceMethod"); 
		MV mv = new MV();
		Object list = null;
		try {
			if (StringUtils.isNotBlank(serviceBeanId) && StringUtils.isNotBlank(serviceMethod)) {
				Object object = net.hsp.web.util.SpringCtx.getSpringContext().getBean(serviceBeanId);
				Class[] cls = { Map.class, PageInfo.class };
				Object[] args = { param, pageInfo };
				list = ReflectionUtils.invokeMethod(object, serviceMethod, cls, args);
			}else if (StringUtils.isNotBlank(serviceBeanId)) {
				Object object = net.hsp.web.util.SpringCtx.getSpringContext().getBean(serviceBeanId);
				Class[] cls = { Map.class, PageInfo.class };
				Object[] args = {param, pageInfo };
				try {
					list = ReflectionUtils.invokeMethod(object, "buildTree", cls, args);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				String sqlKey = param.get("sqlKey");
				String treeSQL = (String) ActionUtil.getCtx().getServletContext().getAttribute(sqlKey);
				if (StringUtils.isNotBlank(treeSQL)) {
					list =  baseService.find(treeSQL, null);
				} else {
					String idField = param.get("idField").replace("'", "");
					String parentIdField = param.get("parentIdField").replace("'", "");
					String labelField = param.get("labelField").replace("'", "");
					String tableName = param.get("tableName").replace("'", "");
					list =  baseService.find("select " + idField + " as id," + parentIdField + " as parentid," + labelField + " as name from " + tableName, null);
				}
			}
		} catch (Exception e) {
			mv.processException(e);
		}
		if (ActionUtil.getCtx().getRequest().getRequestURI().endsWith(".json")) {
			mv.addObject(WebConstant.COMMAND,list);
		}else{
			try {
				ActionUtil.getCtx().getResponse().getWriter().println(JSONArray.fromObject(list));
			} catch (IOException e) { 
				e.printStackTrace();
			}
			return null;
		}
		return mv.fwd();
	}

	@MethodLogUtil(type = "", value = "按部门查询用户")
	@RequestMapping("/deptUser") 
	public ModelAndView deptUser(FilterMap filter, PageInfo pageInfo) {
		MV mv = new MV("/sys/org/deptUser",WebConstant.COMMAND,departmentService.buildDeptUserTree(filter.getFilter(),pageInfo));
		return mv.fwd();
	}

	@MethodLogUtil(type = "", value = "按角色查询用户")
	@RequestMapping("/roleUser")
	public ModelAndView roleUser(FilterMap filter, PageInfo pageInfo) {
		MV mv = new MV("/sys/rbac/roleUser",WebConstant.COMMAND,roleService.buildRoleUserTree(filter.getFilter(),pageInfo));
		return mv.fwd();
	}
	

	@MethodLogUtil(type="",value="根据岗位等信息查询对应的用户")
	@RequestMapping("/postUser")
	public ModelAndView doUserList(FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV("/sys/org/employeeList", WebConstant.COMMAND, employeeService.doUserList(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}

	@MethodLogUtil(type = "", value = "微信统一消息入口")
	@RequestMapping("/anon/weixin/{custId}")
	public ModelAndView weixin(@PathVariable(value = "custId")
	String custId) {
		try {
			DynamicDataSource.setCustId(custId);
			String page = "/page/" + custId + "/weixin/index";
			return new ModelAndView(page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 不切换dataSource
	 * @param custId
	 * @return
	 */
	@MethodLogUtil(type = "", value = "微信统一消息入口")
	@RequestMapping("/anon/weixin2/{custId}")
	public ModelAndView weixin2(@PathVariable(value = "custId")
	String custId) {
		try {
			String page = "/page/" + custId + "/weixin/index";
			return new ModelAndView(page);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 会自动以custId切换datasource
	 * @param custId
	 * @param action
	 * @return
	 */
	@MethodLogUtil(type = "", value = "微信统一oauth入口")
	@RequestMapping("/anon/weixin/oauth/{custId}/{action}")
	public ModelAndView oauth(@PathVariable(value = "custId")
	String custId, @PathVariable(value = "action")
	String action) {
		try {
			DynamicDataSource.setCustId(custId);
			String page = "/page/" + custId + "/weixin/oauth";
			ModelAndView mv = new ModelAndView(page);
			mv.addObject("custId", custId);
			mv.addObject("action", action);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 不切换datasource
	 * @param custId
	 * @param action
	 * @return
	 */
	@MethodLogUtil(type = "", value = "微信统一oauth2入口")
	@RequestMapping("/anon/weixin/oauth2/{custId}/{action}")
	public ModelAndView oauth2(@PathVariable(value = "custId")
	String custId, @PathVariable(value = "action")
	String action) {
		try {
			String page = "/page/" + custId + "/weixin/oauth";
			ModelAndView mv = new ModelAndView(page);
			mv.addObject("custId", custId);
			mv.addObject("action", action);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	 
}

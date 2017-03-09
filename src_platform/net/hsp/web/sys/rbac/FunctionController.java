package net.hsp.web.sys.rbac;

import javax.servlet.http.HttpServletRequest;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.service.sys.rbac.FunctionService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.hsp.web.util.WebUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 权限表控制器
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "权限表")
@RequestMapping("/sys/rbac/function")
@Lazy(true)
public class FunctionController  {

	private String listURL = "/sys/rbac/functionList";
	private String formURL = "/sys/rbac/functionForm";
	
	@Autowired
	private FunctionService functionService; 
 
	private final Logger log = Logger.getLogger(FunctionController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Function obj) {
		try {
			//检测url，前面默认加斜线
			String address = obj.getLinkAddress();
			if(StringUtils.isNotBlank(address)){
				if(!address.startsWith("/") && !address.startsWith("http")){
					obj.setLinkAddress("/"+address);
				}
			}
			//替换中文逗号为英文的逗号
			String innerurl = obj.getInnerUrl();				
			if (StringUtils.isNotBlank(innerurl)) {
				innerurl = innerurl.replaceAll("，",",");
				obj.setInnerUrl(innerurl);
			} 
			
			if (obj.getFunctionId()!=null&&!"".equals(obj.getFunctionId())) {
				functionService.modifyFunction(obj);
			}else{
				functionService.addFunction(obj); 
			}
			//清理缓存菜单文件
			HttpServletRequest req = ActionUtil.getCtx().getRequest();			 
			String custId = HttpSessionFactory.getCustId(req);			
			functionService.clearAllMenuFile(custId);			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Function obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			MV mv = new MV(listURL, WebConstant.COMMAND, obj);
			
			String[] dds = StringUtils.isNotBlank(ids)?ids.split(","):new String[]{obj.getFunctionId()};
			int haschild = functionService.checkFuncHasChild(dds);
			if(haschild==1){
				mv.addObject("status", "2");
				mv.addObject("msg", "您选择的菜单还包含下级节点，不能直接删除！请先删除相应的子节点！");
				return mv.fwd();			
			}			
			
			if (StringUtils.isNotBlank(ids)) {
				functionService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				functionService.delete(obj);
			}
			
			//清理缓存菜单文件
			HttpServletRequest req = ActionUtil.getCtx().getRequest();			 
			String custId = HttpSessionFactory.getCustId(req);			
			functionService.clearAllMenuFile(custId);
			return mv.fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Function obj){
		MV mv = null;
		try{  
			//根据id(pk)从DB加载 
			if (obj.getFunctionId()!=null&&obj.getFunctionName()==null) {
				obj = (Function) functionService.findById(obj); 
				
				Function parentFunc = new Function();
				parentFunc.setFunctionId(obj.getParentId());
				Object pObj = functionService.findById(parentFunc);
				if(pObj!=null){
					parentFunc = (Function)pObj;
				}				
				mv = new MV(formURL,WebConstant.COMMAND,obj);
				mv.addObject("parentObj", parentFunc);
				
			}else{
				String parentId = obj.getFunctionId();
				String parentName = obj.getFunctionName();
				//parentName = new String(parentName.getBytes("iso-8859-1"), "UTF-8");
				obj.setFunctionId(null);
				obj.setFunctionName(null);
				mv = new MV(formURL,WebConstant.COMMAND,obj); 
				mv.addObject("parentId", parentId);
				mv.addObject("parentName", parentName);				
			}			
			// mv = new MV(formURL,WebConstant.COMMAND,obj);			
			//相关初始数据加载 
			//mv.addObject("x","yy");
			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Function function, FilterMap filter, PageInfo pageInfo) {
		try {
			if(filter.getFilter().get("parent_id")==null){
				filter.getFilter().put("parent_id", "0001");
			}
			return new MV(listURL, WebConstant.COMMAND, functionService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="1",value="用户一级菜单")
	@RequestMapping("/getTopMenu")
	public ModelAndView getTopMenu(FilterMap filter) {
		MV mv = new MV();
		try { 
			String userId = ActionUtil.getCtx().getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString(); 
			mv.addObject(WebConstant.COMMAND, functionService.getFirstLevelMenu(userId, "1".equals(userId)));
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e);
		}
	}
	
	@MethodLogUtil(type="1",value="用户完整菜单，不包含 button")
	@RequestMapping("/getAllMenu")
	public ModelAndView getAllMenu(FilterMap filter) {
		MV mv = new MV();
		try { 
			String userId = ActionUtil.getCtx().getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			mv.addObject(WebConstant.COMMAND, functionService.getMenuDataByUserId(userId, "1".equals(userId),false));
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e);
		}
	}
	
	@MethodLogUtil(type="1",value="用户完整菜单,含 button")
	@RequestMapping("/getAllMenuIncBtn")
	public ModelAndView getAllMenuIncBtn(FilterMap filter) {
		MV mv = new MV();
		try { 
			String userId = ActionUtil.getCtx().getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			mv.addObject(WebConstant.COMMAND, functionService.getMenuDataByUserId(userId, "1".equals(userId),true));
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e);
		}
	}
	
	@MethodLogUtil(type="1",value="用户完整菜单,含 button,平面结构")
	@RequestMapping("/getFullMenu")
	public ModelAndView getFullMenu(FilterMap filter) {
		MV mv = new MV();
		try {
			String userId = ActionUtil.getCtx().getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			mv.addObject(WebConstant.COMMAND, functionService.listMenuDataByUserId(userId, "1".equals(userId),true));
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e);
		}
	}
	
	
	@MethodLogUtil(type="",value="变更部门层级")
	@RequestMapping("/changeFuncLevel")
	public ModelAndView changeFuncLevel(@RequestParam(value="id",required=true)String id,
			@RequestParam(value="type",required=true)String type,			 
			@RequestParam(value="parentId",required=false)String parentId,
			@RequestParam(value="sortNo",required=false)String sortNo,
			@RequestParam(value="sortList",required=false)String sortList){
		try {
			functionService.changeFunctionLevel(id, type, parentId, sortNo, sortList.split(";"));
			//清理缓存菜单文件
			HttpServletRequest req = ActionUtil.getCtx().getRequest();			 
			String custId = HttpSessionFactory.getCustId(req);			
			functionService.clearAllMenuFile(custId);
			return new MV(listURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	 
		
}

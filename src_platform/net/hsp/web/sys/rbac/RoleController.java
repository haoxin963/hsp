package net.hsp.web.sys.rbac;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.sys.rbac.FunctionService;
import net.hsp.service.sys.rbac.RoleService;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
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
 * 角色表控制器
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "角色表")
@RequestMapping("/sys/rbac/role")
@Lazy(true)
public class RoleController  {

	private String listURL = "/sys/rbac/roleList";
	private String formURL = "/sys/rbac/roleForm";
	private String grantFuncURL = "/sys/rbac/grant_func";
	private String viewRoleGrantedURL = "/sys/rbac/viewRoleGranted";
	private String roleUserURL = "/sys/rbac/userByRole";
	private String giveRoleURL = "/sys/rbac/giveRoleToUser";
	private String roleToUserURL = "/sys/rbac/roleToUser";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService; 
	
	@Autowired
	private FunctionService functionService;

	private final Logger log = Logger.getLogger(RoleController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Role obj) {
		try {
			if (obj.getId()!=null) {
				roleService.update(obj); 
			}else{
				obj.setDelTag("0");				 
				roleService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Role obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				roleService.batchDelRoleByIds(ids.split(","));
				//roleService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				roleService.delRoleById(obj.getId().toString());
				//roleService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Role obj){
		try{  
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (Role) roleService.findById(obj); 
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
	public ModelAndView doList(Role role, FilterMap filter, PageInfo pageInfo) {
		try {
			Map<String, Object> map = roleService.queryRole(role, filter, pageInfo);
			return new MV(listURL, WebConstant.COMMAND, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	
	@MethodLogUtil(type="",value="到角色模块配置界面")
	@RequestMapping("/toGrantFunc")
	public ModelAndView toGrantFunc(@RequestParam(value="rid",required=true)String rid) {
		try {
			Map<String,Object> mp = new HashMap<String,Object>();			
			mp.put("roleId", rid);
			return new MV(grantFuncURL,mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}	
	
	@MethodLogUtil(type="",value="到分配角色模块")
	@RequestMapping("/doTreePage")
	public ModelAndView doTreePage(@RequestParam(value="rid",required=true)String rid) {
		try {
			//查询出所有的功能权限列表
			List<Function> funcList = functionService.getFunctionList();			 
			List<Function> roleFuncList = functionService.getFuncListByRoleId(rid);
			 	 
			Map<String,Object> mp = new HashMap<String,Object>();			
			mp.put("treeList", funcList);
			mp.put("functionList", roleFuncList); 
			return new MV(grantFuncURL,mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="分配分配角色模块")
	@RequestMapping("/doGrantFunc") 
	public ModelAndView doGrantFunc(User obj,@RequestParam(value="rid",required=false) String rid,
			@RequestParam(value="fids",required=false) String fids) {
		try {
			functionService.grantFuncs2Role(rid, fids.split(","));
			//清理缓存菜单文件
			HttpServletRequest req = ActionUtil.getCtx().getRequest();
			HttpSession session = req.getSession();
			String custId = HttpSessionFactory.getCustId(req);			
			functionService.clearAllMenuFile(custId);
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}
	
	@MethodLogUtil(type="",value="角色分配视图")
	@RequestMapping("/viewRoleGranted") 
	public ModelAndView viewRoleGranted(FilterMap filter, PageInfo pageInfo) {
		try { 
			Role role = roleService.getRoleById(filter.get("rid").toString());
			Map map = userService.getUsersByRoleId(filter.get("rid").toString(),pageInfo);  
			MV mv = new MV(viewRoleGrantedURL, WebConstant.COMMAND, map);
			mv.addObject("role", role);
			return mv.fwd(); 
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}
	  
	  @MethodLogUtil(type="", value="角色人员")
	  @RequestMapping({"/doRoleUser"})
	  public ModelAndView doRoleUser(@RequestParam(value="roleId", required=true) String roleId,
			  FilterMap filter, PageInfo pageInfo) {
	    try {
	    	Role role = roleService.getRoleById(roleId);
	    	Map modelObj = roleService.queryUsersByRoleId(roleId, filter.getFilter(), pageInfo);
	    	MV mv = new MV(roleUserURL, WebConstant.COMMAND, modelObj);
	    	mv.addObject("roleId",roleId);
	    	mv.addObject("role",role);
	    	return mv.fwd();
	    } catch (Exception e) {
	      return new MV().processException(e, null);
	    }
	  }
	  
	  @MethodLogUtil(type = "", value = "按角色分配用户")
	  @RequestMapping("/toGiveRole")
	  public ModelAndView toGiveRole(@RequestParam(value="roleId", required=true) String roleId,
			  FilterMap filter, PageInfo pageInfo) {
		  try {
			  Role role = roleService.getRoleById(roleId);
			  Map map = userService.queryUser(null, filter, pageInfo);
			  List<Map<String,Object>> roledUsers = roleService.queryUsersByRoleId(roleId, filter.getFilter());
			  //map.put("roledUsers", roledUsers);
			  StringBuffer sb = new StringBuffer();
			  for (Map<String,Object> u : roledUsers) {
				  sb.append("'" + u.get("id") + "',");
			  }
			  String roledUserIds = roledUsers.size() > 0 ? sb.substring(0, sb.length() - 1) : "";
			  MV mv = new MV(this.giveRoleURL, WebConstant.COMMAND, map);
			  mv.addObject("roledUserIds", roledUserIds);
			  mv.addObject("role", role);
		      return mv.fwd();
		    } catch (Exception e) {
		      return new MV().processException(e, null);
		    }
	}
	
	  @MethodLogUtil(type = "", value = "给一群用户更新相关角色")
	  @RequestMapping("/updateRoleToUsers")
	  public ModelAndView updateRoleToUsers(@RequestParam(value="roleId", required=true)String roleId, 
			  @RequestParam(value="pageUserIds", required=true)String pageUserIds, 
			  @RequestParam(value="selectUserIds", required=true)String selectUserIds)
	  {
		  try 
		  {
			  Role role = roleService.getRoleById(roleId);
			  String[] pageUids = pageUserIds.split(",");
			  String[] selectUids = selectUserIds.split(",");
			  roleService.updateRoleInSomeUsers(roleId, pageUids, selectUids);
				//清理缓存菜单文件
			  HttpServletRequest req = ActionUtil.getCtx().getRequest();			 
			  String custId = HttpSessionFactory.getCustId(req);			
			  functionService.clearAllMenuFile(custId);
			  MV mv = new MV(this.listURL, WebConstant.COMMAND, role);
			  return mv.fwd();
		  } 
		  catch (Exception e)
		  {
		     return new MV().processException(e);
		  }
	  }
	  
	  @MethodLogUtil(type = "", value = "给一群用户更新相关角色")
	  @RequestMapping("/updateRoleToAllUsers")
	  public ModelAndView updateRoleToAllUsers(@RequestParam(value="roleId", required=true)String roleId, 
			  @RequestParam(value="selectUserIds", required=true)String selectUserIds)
	  {
		  try 
		  {
			  Role role = roleService.getRoleById(roleId);
			  String[] selectUids = selectUserIds.split(",");
			  roleService.grantUsers2Role(roleId, selectUids);
				//清理缓存菜单文件
			  HttpServletRequest req = ActionUtil.getCtx().getRequest();			 
			  String custId = HttpSessionFactory.getCustId(req);			
			  functionService.clearAllMenuFile(custId);
			  MV mv = new MV(this.listURL, WebConstant.COMMAND, role);
			  return mv.fwd();
		  } 
		  catch (Exception e)
		  {
		     return new MV().processException(e);
		  }
	  }
	  
	  @MethodLogUtil(type = "", value = "角色用户关系")
	  @RequestMapping("/roleToUser")
	  public ModelAndView roleToUser(@RequestParam(value="roleId", required=true)String roleId)
	  {
		  try 
		  {
			  Role role = roleService.getRoleById(roleId);
			  MV mv = new MV(this.roleToUserURL, WebConstant.COMMAND, role);
			  mv.addObject("role", role);
			  return mv.fwd();
		  } 
		  catch (Exception e)
		  {
		     return new MV().processException(e);
		  }
	  }
	  
}

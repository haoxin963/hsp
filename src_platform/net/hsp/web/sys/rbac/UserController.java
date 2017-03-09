package net.hsp.web.sys.rbac;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.hsp.common.CacheUtil;
import net.hsp.common.MethodLogUtil;
import net.hsp.common.SystemContext;
import net.hsp.common.WebLogUtil;
import net.hsp.common.crypt.MD5;
import net.hsp.entity.sys.monitor.log.UserLog;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.sys.rbac.FunctionService;
import net.hsp.service.sys.rbac.RoleService;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.ClientInfoUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户控制器
 * 
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "用户")
@RequestMapping("/sys/rbac/user")
@Lazy(true)
public class UserController {

	private String listURL = "/sys/rbac/userList";
	private String formURL = "/sys/rbac/userForm";
	private String grantRoleURL = "/sys/rbac/grant_role";
	private String indexUrl = "redirect:/main/index.jsp";
	private String modifypwdUrl = "/main/modifypwd";
	private String onlineUser = "/sys/rbac/onlineUser";
	private String viewUserAuthURL = "/sys/rbac/userFunctionList";

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private FunctionService functionService;

	private final Logger log = Logger.getLogger(UserController.class);

	@MethodLogUtil(type = "1", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(User obj) {
		try {
			MV mv = null;
			if (obj.getId() != null) {
				userService.modifyUser(obj);
				mv = new MV(listURL, WebConstant.COMMAND, obj);
			} else {
				// 删除标记为0
				obj.setDelTag("0");
				// 启用状态为1启用
				obj.setStatus("1");
				mv = new MV(listURL, WebConstant.COMMAND, obj);
				User user = userService.addUser(obj);
				if (user == null) {
					mv.addErrorInfo("创建用户失败!");
				}
			}
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(User obj, @RequestParam(value = "ids", required = false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				// userService.batchDelete(obj.getClass(),ids.split(","));
				userService.batchDelUserByIds(ids.split(","));
			} else {
				userService.delUserById(obj.getId().toString());
				// userService.update(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(User obj) {
		try {
			// 根据id(pk)从DB加载
			if (obj.getId() != null) {
				obj = (User) userService.findById(obj);
			}
			MV mv = new MV(formURL, WebConstant.COMMAND, obj);

			// 相关初始数据加载
			// mv.addObject("x","yy");
			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(User user, FilterMap filter, PageInfo pageInfo) {
		try {
			Map<String, Object> map = userService.queryUser(user, filter, pageInfo);
			return new MV(listURL, WebConstant.COMMAND, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "启用/禁用用户")
	@RequestMapping("/doEnableUser")
	public ModelAndView doEnableUser(User obj, @RequestParam(value = "keyids", required = false) String keyids, @RequestParam(value = "sts", required = true) String sts) {
		try {
			userService.enableUser(keyids.split(","), sts.split(","));
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "到分配角色页面")
	@RequestMapping("/toGrantRole")
	public ModelAndView toGrantRole(Role role, FilterMap filter, PageInfo pageInfo, @RequestParam(value = "userid", required = true) String userid) {
		try {
			// Map<String, Object> map = userService.queryUser(user, filter,
			// pageInfo);
			List<Role> roleList = roleService.getRoleListByUserId(userid);
			StringBuffer sb = new StringBuffer();
			for (Role r : roleList) {
				sb.append("'" + r.getId() + "',");
			}
			String ids = roleList.size() > 0 ? sb.substring(0, sb.length() - 1) : "";
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("UserRoleList", roleList);
			mp.put("userId", userid);
			mp.put("roleids", ids);
			return new MV(grantRoleURL, mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "分配角色")
	@RequestMapping("/doGrantRole")
	public ModelAndView doGrantRole(User obj, HttpServletRequest req, @RequestParam(value = "userid", required = false) String userid, @RequestParam(value = "rids", required = false) String rids) {
		try {
			roleService.grantRoles2User(userid, rids.split(","));
			// 清理用户的菜单文件
			HttpSession session = req.getSession();
			String cuserId = (String) session.getAttribute("CurrentSystemInstance");
			functionService.clearUserMenuFile(cuserId, userid);

			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "重置密码")
	@RequestMapping("/doResetUserPwd")
	public ModelAndView doResetUserPwd(User obj, @RequestParam(value = "userid", required = false) String userid) {
		try {
			User user = new User();
			user.setId(Long.parseLong(userid));
			userService.resetUserPwd(user);
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "用户登录")
	@RequestMapping("/anon/doLogin")
	public ModelAndView doLogin(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password, @RequestParam(value = "verifycode", required = false) String verifycode,
			@RequestParam(value = "needCode", required = false) String needCode, @RequestParam(value = "pwdModel", required = false) String pwdModel, @RequestParam(value = "defaultPage", required = false) String defaultPage,
			@RequestParam(value = "title", required = false) String title, @RequestParam(value = "needPassword", required = false) String needPassword) {
		String custId = HttpSessionFactory.getCustId();
		String path = "/page/" + custId + "/auth";
		File f = new File(SystemContext.getProperty("webApp.root") + path + ".jsp");
		if (f.exists()) {
			return new ModelAndView(path);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		MV mv = new MV();
		mv.setViewName("redirect:/" + custId + ".login");
		try {

			HttpServletRequest req = ActionUtil.getCtx().getRequest();

			if (StringUtils.isBlank(needCode)) {
				// 检测验证码
				HttpSession session = req.getSession();
				String authcode = (String) session.getAttribute("authcode");
				if (!verifycode.equalsIgnoreCase(authcode)) {
					// 验证码输入错误
					paramMap.put("status", "5");
					paramMap.put("msg", "验证码错误");
					return new MV(indexUrl, paramMap).fwd();
				}
			}
			User user = new User();
			user.setUserName(username);
			if (StringUtils.isBlank(pwdModel)) {
				user.setPassword(MD5.md5(password));
			} else {
				user.setPassword(password);
			}
			Map loginResult = null;
			if (StringUtils.isBlank(needPassword) || !"false".equals(needPassword)) {
				loginResult = userService.login(user);
			} else {
				loginResult = userService.loginByUserName(user);// 2.0跳转3.0用户名登录
			}
			String retCode = loginResult.get("retCode").toString();

			// 登录成功
			if ("1".equals(retCode)) {
				// 获取当前用户数据
				User loginUser = (User) loginResult.get("retUser");
				HttpSessionFactory.authSuccess(req, loginUser.getId(), loginUser.getUserName());
				String agent = req.getHeader("user-agent");
				try {
					ClientInfoUtil c = new ClientInfoUtil(agent);
					UserLog log = new UserLog();
					log.setUserId(loginUser.getId() + "");
					Date currTM = new Date();
					log.setOperateTime(currTM);
					log.setIp(c.getIp(req));
					log.setLogType("1");// 1web pc 2/mob
					log.setOsVersion(c.getOSName());
					log.setAgentVersion(c.getExplorerName() + "_" + c.getExplorerVer());
					functionService.save(log);

					// 更新最后登录时间
					loginUser.setLastLogTime(currTM);
					loginUser.setLastLogIp(c.getIp(req));
					userService.update2(loginUser);
				} catch (Exception e) {
					// e.printStackTrace();
				}

				// 消息服务器位置
				Map m = new HashMap();
				try {
					m.put("pushServer", HttpSessionFactory.getPushServer(HttpSessionFactory.getCustId()));
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e);
				}
				HttpSession session = req.getSession();
				paramMap.put("status", "1");
				Map<String, String> userTemp = new HashMap<String, String>();
				userTemp.put("userId", loginUser.getId() + "");
				userTemp.put("userName", loginUser.getUserName());
				userTemp.put("trueName", loginUser.getTrueName());
				userTemp.put("sessionId", session.getId());
				userTemp.put("custId", custId);
				m.put("userInfo", userTemp);
				paramMap.put(WebConstant.COMMAND, m);
				if (StringUtils.isNotBlank(defaultPage)) {
					mv.addObject("defaultPage", defaultPage);
				}
				if (StringUtils.isNotBlank(title)) {
					mv.addObject("title", title);
				}
				mv.setViewName("redirect:/main/main.jsp");
			} else if ("2".equals(retCode)) {
				// 用户名不存在
				paramMap.put("status", "2");
				paramMap.put("msg", "用户名不存在");
			} else if ("3".equals(retCode)) {
				// 密码错误
				paramMap.put("status", "3");
				paramMap.put("msg", "密码错误");
			} else if ("4".equals(retCode)) {
				// 用户已经被禁用
				paramMap.put("status", "4");
				paramMap.put("msg", "用户已经被禁用");
			}
			mv.addAllObjects(paramMap);
			if (req.getRequestURI().endsWith(".json")) {
				mv.setViewName(null);
			}
			return mv.fwd();
		} catch (Exception e) {
			mv.setViewName(null);
			paramMap.put("status", "0");
			paramMap.put("msg", "系统繁忙，请稍后再试！");
			mv.addAllObjects(paramMap);
			return mv.processException(e, e.getMessage());
		}
	}

	@MethodLogUtil(type = "1", value = "修改密码")
	@RequestMapping("/doModifyUserPwd")
	public ModelAndView doModifyUserPwd(@RequestParam(value = "oldpwd", required = true) String oldpwd, @RequestParam(value = "newpwd", required = true) String newpwd) {
		MV mv = new MV(modifypwdUrl);
		try {
			HttpServletRequest req = ActionUtil.getCtx().getRequest();
			User currentUser = (User) HttpSessionFactory.getInstance().getUserInfoByUserId(req);
			String oldpwdEncode = MD5.md5(oldpwd);
			if (!oldpwdEncode.equals(currentUser.getPassword())) {
				mv.addErrorInfo("旧密码输入错误");
			} else {
				currentUser.setPassword(MD5.md5(newpwd));
				// 更新密码修改标记
				currentUser.setIsUpdatePass("2");
				userService.modifyUserPwd(currentUser);
			}
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "分配角色")
	@RequestMapping("/doGetDirector")
	public ModelAndView doGetDirector(@RequestParam(value = "id", required = false) String id) {
		try {
			Map map = new HashMap();
			User user = userService.getDirectorByUserId(Long.parseLong(id));
			if (user != null) {
				map.put("user", user);
			}
			return new MV(listURL, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "获当前在线人列表")
	@RequestMapping("/onlineUser")
	public ModelAndView getOnlineUser() {
		try {
			CacheUtil cache = CacheUtil.getInstance();
			Cache c = cache.getCache("session1800_sync_online");
			Query q = c.createQuery();
			q.includeKeys();
			q.addCriteria(Query.KEY.ilike(HttpSessionFactory.getCustId() + "_*"));
			Results results = q.execute();// 执行查询
			List<Result> resultList = results.all();
			List online = new ArrayList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Result result : resultList) {
					if (results.hasKeys()) {
						Map m = new HashMap();
						Object k = result.getKey();
						m.put("id", k.toString().substring(k.toString().indexOf("_") + 1));
						m.put("name", c.get(k).getValue());
						m.put("time", c.get(k).getLastUpdateTime() == 0 ? new Date(c.get(k).getCreationTime()) : new Date(c.get(k).getLastUpdateTime()));
						online.add(m);
					}
				}
			}
			MV mv = new MV(onlineUser, WebConstant.COMMAND, online);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "到分配角色页面")
	@RequestMapping("/toViewUserAuth")
	public ModelAndView toViewUserAuth(@RequestParam(value = "userid", required = true) String userid) {
		try {
			List<Role> roleList = roleService.getRoleListByUserId(userid);
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("rows", roleList);
			MV mv = new MV(viewUserAuthURL, WebConstant.COMMAND, mp);
			mv.addObject("userid", userid);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "角色-菜单树")
	@RequestMapping("/buildRoleMenuTree")
	@ResponseBody
	public List buildRoleMenuTree(@RequestParam(value = "roleId", required = true) String roleId, @RequestParam(value = "userid", required = true) String userid) {
		if (StringUtils.isBlank(roleId)) {
			List<Role> roleList = roleService.getRoleListByUserId(userid);
			for (Role role : roleList) {
				roleId += role.getId() + ",";
			}
			roleId = roleId.substring(0, roleId.length() - 1);
		}
		return userService.buildMenuTreeByRoleId(roleId);
	}

}

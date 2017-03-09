package net.hsp.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.sys.manage.StationServiceImpl;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.SpringCtx;

import org.apache.commons.lang.StringUtils;

public class HttpSessionFactory {

	private UserService userService;

	private static HttpSessionFactory instance;

	private HttpSessionFactory() {
	}

	public static void setSessionAttribute(HttpSession session, String key, Object obj) {
		session.setAttribute(key, obj);
	}

	public static void removeSessionAttribute(HttpSession session, String key) {
		session.removeAttribute(key);
	}

	private UserService getService() {
		if (userService == null) {
			userService = (UserService) SpringCtx.getSpringContext().getBean("userServiceImpl");
		}
		return userService;
	}

	public static HttpSessionFactory getInstance() {
		if (instance == null) {
			synchronized (HttpSessionFactory.class) {
				if (instance == null) {
					instance = new HttpSessionFactory();
				}
			}
		}

		return instance;
	}

	public static Object getAttribute(HttpServletRequest req, String key) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	/**
	 * 取出当前登录的用户的用户id 依赖threadLocal 中HttpServletRequeset 仅限web层调用
	 * 
	 * @param req
	 * @return
	 */
	public static Object getUserId(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			return session.getAttribute(PlatFormConstant.CURRENT_USERID);
		}
		return null;
	}

	/**
	 * 取出当前登录的用户的用户id 依赖threadLocal 中HttpServletRequeset 仅限web层调用
	 * 
	 * @return
	 */
	public static Object getUserId() {
		HttpSession session = ActionUtil.getCtx().getRequest().getSession(false);
		if (session != null) {
			return session.getAttribute(PlatFormConstant.CURRENT_USERID);
		}
		return null;
	}

	/**
	 * 取出当前客户custId,如dev0 依赖threadLocal 中HttpServletRequeset 仅限web层调用
	 * 
	 * @param req
	 * @return
	 */
	public static String getCustId(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		String custId = null;
		if (session != null) {
			custId = (String) session.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
		}
		return custId;
	}

	/**
	 * 取出当前会话中的custId 依赖threadLocal 中HttpServletRequeset 仅限web层调用
	 * 
	 * @return
	 */
	public static String getCustId() {
		HttpSession session = ActionUtil.getCtx().getRequest().getSession(false);
		String custId = null;
		if (session != null) {
			custId = (String) session.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
		}
		return custId;
	}

	/**
	 * 认证成功后将userId写入会话(sso以及非sso通用)
	 * 
	 * @param req
	 * @param userId
	 * @param userName
	 */
	public static void authSuccess(HttpServletRequest req, Object userId, String userName) {
		HttpSession session = req.getSession();
		session.setAttribute(PlatFormConstant.CURRENT_USERID, userId);
		session.setAttribute(PlatFormConstant.CURRENT_USERNAME, userName);
		session.removeAttribute("allFunctions");
		session.removeAttribute("urls");
	}

	/**
	 * 取出当前登录的用户对象 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param req
	 * @return
	 */
	public User getUserInfoByUserId(HttpServletRequest req) {

		return getService().getUserById(getUserId(req).toString());
	}

	/**
	 * 取出当前登录的用户对象 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserInfoByUserId(Long userId) {
		return getService().getUserById(String.valueOf(userId));
	}

	/**
	 * 取出当前登录的直接上级对象 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param userId
	 * @return
	 */
	public User getDirectorByUserId(Long userId) {
		return getService().getDirectorByUserId(userId);
	}

	/**
	 * 取出当前用户的部门信息 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param req
	 * @return
	 */
	public Map getDeptByUserId(HttpServletRequest req) {
		Object obj = getUserId(req);
		if (obj == null) {
			return null;
		} else {
			return getService().getDeptByUserId(Long.valueOf(obj.toString()));
		}
	}

	/**
	 * 取出当前用户的部门信息 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param req
	 * @return
	 */
	public Map getDeptByUserId(Long userId) {
		return getService().getDeptByUserId(userId);
	}

	/**
	 * 从缓存中取出当前站点信息 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @return
	 */
	public static Map<String, Object> getCurrentStationInfo() {
		String custId = getCustId(ActionUtil.getCtx().getRequest());
		Map map = null;
		if (custId != null) {
			return StationServiceImpl.getStations().get(custId);
		}
		return map;
	}

	public static Map<String, Object> getStation(String custId) {
		return StationServiceImpl.getStations().get(custId);
	}

	public static Map<String, Map<String, Object>> getStations() {
		return StationServiceImpl.getStations();
	}

	/**
	 * 从缓存中取出当前用户的菜单集合 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param userid
	 * @return
	 */
	public static Set<String> getFunctionSetByUserId(String userid) {
		return HttpSessionFactory.getInstance().getService().getFunctionSetByUserId(userid);
	}

	/**
	 * 从缓存中取出当前用户的菜单集合 依赖HttpServletRequeset 仅限web层调用
	 * 
	 * @param userid
	 * @return
	 */
	public static Set<String> getAllFunctionSet() {
		return HttpSessionFactory.getInstance().getService().getAllFunctionSet();
	}

	public static Map<String, String> getPushServer(String custId) {
		Map<String, String> pushServer = null;
		String str = (String) HttpSessionFactory.getStation(HttpSessionFactory.getCustId()).get("msgServerAddr");
		if (StringUtils.isNotBlank(str)) {
			pushServer = new HashMap();
			pushServer.put("host", str.split(":")[0]);
			pushServer.put("port", str.split(":")[1]);
		}
		return pushServer;
	}

	/**
	 * 是否部署IM
	 * 
	 * @param custId
	 * @return
	 */
	public static boolean hasIM(String custId) {
		if (custId == null) {
			return false;
		}
		if (StationCfgUtil.getInstance(custId) == null) {
			return false;
		}
		String str = StationCfgUtil.getInstance(custId).get("hasIM");
		if (StringUtils.isNotBlank(str) && "true".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 取得当前站点所属行业分类、继承的父项目、当前站点的抽象模块
	 * 
	 * @return
	 */
	public static String getAbstract(String custId) {
		if (custId == null) {
			return null;
		}
		if (StationCfgUtil.getInstance(custId) == null)
			return null;
		return StationCfgUtil.getInstance(custId).get("abstract");
	}

	/**
	 * 取得当前站点所属行业分类、继承的父项目、当前站点的抽象模块，当没有时使用默认当前的custId
	 * 
	 * @param custId
	 * @return
	 */
	public static String getAbstractUseDefault(String custId) {
		String abs = getAbstract(custId);
		return abs == null ? custId : abs;
	}
}

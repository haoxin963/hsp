package net.hsp.common.constants;

public interface PlatFormConstant {
	/**
	 * 管理平台实例名
	 */
	public static final String BASESTATIONID = "uisp";
	
	/**
	 * 模板实例名
	 */
	public static final String TEMPLATEID = "template";
	

	/**
	 * 登录用户Id session中的key
	 */
	public static final String CURRENT_USERID = "userId";

	/**
	 * 登录用户名 session中的key
	 */
	public static final String CURRENT_USERNAME = "userName";

	/**
	 * 系统超级管理用户的用户名
	 */
	public final String ADMIN_USERNAME = "administrator";

	/**
	 * 当前站点实例信息 session中的key
	 */
	public static final String CURRENT_SYSINSTANCE = "CurrentSystemInstance";

	// 当前站点完整信息
	public static final String CURRENT_STATION_INFO = "currentStationInfo";

	public static final String CONFIGPATH = "/com/hsp/web";

	public static final String CONFIGFILEPATH = "/com/hsp/web";

	// 以下为选人过滤器的分类标识
	public static final String FILTER_TYPE_ROLE = "role";
	public static final String FILTER_TYPE_DEPT = "dept";
	public static final String FILTER_TYPE_GROUP = "group";
	public static final String FILTER_TYPE_USER = "user";

	public static final String COMMENT_TYPE_NOTICE = "notice";

	public static final String TEMP_PATH = "/temp";
}

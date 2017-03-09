package net.hsp.service.sys.monitor.log;

import java.util.Map;

import net.hsp.entity.sys.monitor.log.UserLog;
import net.hsp.service.BaseService;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

/**
 * 登录日志业务接口
 * @author lk0182
 */
public interface UserLogService extends BaseService  {
	
	/**
	 * 多条件查询登录日志
	 */
	public Map queryUserLog(UserLog userLog,FilterMap map,PageInfo p);
	 
	/**
	 * 查询用户操作日志
	 * @param param
	 * @param p
	 * @return
	 */
	public Map<String,Object> queryUserActionLog(Map param, PageInfo p);
}
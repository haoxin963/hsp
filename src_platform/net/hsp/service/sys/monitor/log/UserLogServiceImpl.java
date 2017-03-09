package net.hsp.service.sys.monitor.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.monitor.log.UserLog;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 登录日志业务类
 * @author lk0182
 */
@Service
@ServiceLogUtil(name = "登录日志业务类")
@Lazy(true)
public class UserLogServiceImpl extends BaseServiceImpl implements UserLogService { 

	
	@MethodLogUtil(type="",value="多条件查询登录日志")
	public Map queryUserLog(UserLog userLog, FilterMap filterMap, PageInfo p) throws ServiceException{
 		String sql = "select description,logType,operateTime,ip,userId,osVersion,agentVersion,id from pubmodule_userlog_tbl where 1=1 "; 
		Map sqlBuild = SQLBuild.buildLike(filterMap);
 		sql += sqlBuild.get("sql"); 
		return this.findPageInfo(sql, (Object[])sqlBuild.get("args"), p); 
	} 
	
	@MethodLogUtil(type="",value="多条件查询用户操作日志")
	public Map<String,Object> queryUserActionLog(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select operateTime,ip,name,ms, id,action  from pubmodule_actionlog_tbl where 1=1"); 
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql")==null ? "" : sqlBuild.get("sql"));
 		if(p==null){
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args!=null) {
				list = this.find(sb.toString(), (Object[])args);
			}else{
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows",list);
			map.put("total",list.size());
 			return map;
	 	}else{
	 		return this.findPageInfo(sb.toString(), (Object[])sqlBuild.get("args"), p); 
	 	} 
	}
}


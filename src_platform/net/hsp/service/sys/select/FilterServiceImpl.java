package net.hsp.service.sys.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.select.Filter;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_selectfilter_tbl业务类
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "pubmodule_selectfilter_tbl业务类")
@Lazy(true)
public class FilterServiceImpl extends BaseServiceImpl implements FilterService { 

	@MethodLogUtil(type="",value="多条件查询pubmodule_selectfilter_tbl")
	public Map<String,Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select filterName,filterDesc,delTag,id from pubmodule_selectfilter_tbl where 1=1"); 
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
	
	public List<Map<String,Object>> getFilterUserIdList(int filterId){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String sql="select id,userName,trueName from pubmodule_user_tbl ";
		String userSQL = sql + " where id in (select filterValue from pubmodule_selectfilterdetail_tbl where filterId=? and filterType = ?)";
		String roleSQL = sql + " where id in (select user_id from pubmodule_userrole_tbl where role_id in (select filterValue from pubmodule_selectfilterdetail_tbl where  filterId=? and filterType = ?)) ";
		String deptSQL = sql + " where department_id in (select filterValue from pubmodule_selectfilterdetail_tbl where filterId=? and filterType = ?)";
		
		Object[] args = new Object[2];
		args[0] = filterId;
		args[1] = PlatFormConstant.FILTER_TYPE_USER;
		list = this.getDAO().queryForList(userSQL,args);
		args[1] = PlatFormConstant.FILTER_TYPE_ROLE;
		list.addAll(this.getDAO().queryForList(roleSQL,args));
		args[1] = PlatFormConstant.FILTER_TYPE_DEPT;
		list.addAll(this.getDAO().queryForList(deptSQL,args));
		return list;
	}
}


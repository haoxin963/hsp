package net.hsp.service.sys.select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.select.FilterDetail;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_selectfilterdetail_tbl业务类
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "pubmodule_selectfilterdetail_tbl业务类")
@Lazy(true)
public class FilterDetailServiceImpl extends BaseServiceImpl implements FilterDetailService { 

	@MethodLogUtil(type="",value="多条件查询pubmodule_selectfilterdetail_tbl")
	public Map<String,Object> query(int filterId,Map param, PageInfo p) {	
		StringBuilder sb = new StringBuilder("select filterId,filterType,filterValue,id from pubmodule_selectfilterdetail_tbl where filterId="+filterId); 

		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql")==null ? "" : sqlBuild.get("sql"));
// 		if(p==null){
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args!=null) {
				list = this.find(sb.toString(), (Object[])args);
			}else{
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows",list);
			map.put("filterId",filterId);
 			return map;
//	 	}else{
//	 		return this.findPageInfo(sb.toString(), (Object[])sqlBuild.get("args"), p); 
//	 	} 
	}
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectRoleList(int filterId){
		String roleSQL = "select id,roleName as name,'0' as pId from pubmodule_role_tbl where delTag = 0 ";
		String filterSQL = "select filterValue from pubmodule_selectfilterdetail_tbl where filterId=? and filterType=?";
		Object[] args = new Object[2];
		args[0] = filterId;
		args[1] = PlatFormConstant.FILTER_TYPE_ROLE;
		List<Map<String,Object>> roleList = this.getDAO().queryForList(roleSQL);
		List<Map<String,Object>> filterList = this.getDAO().queryForList(filterSQL, args);
		
		for(Map<String,Object> map:roleList){
			long id = (Long)map.get("id");
			for(int i=0;i<filterList.size();i++){
				int value = Integer.parseInt(filterList.get(i).get("filterValue").toString());
				if(id == value ){
					map.put("checked", "true");
				}
			}
		}
		return roleList;
	}
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectDeptList(int filterId){
		String sql = "select id,name,parentId as pId from pubmodule_department_tbl where delTag = 0 order by sortNo desc";
		String filterSQL = "select filterValue from pubmodule_selectfilterdetail_tbl where filterId=? and filterType=?";
		Object[] args = new Object[2];
		args[0] = filterId;
		args[1] = PlatFormConstant.FILTER_TYPE_DEPT;
		List<Map<String,Object>> list = this.getDAO().queryForList(sql);
		List<Map<String,Object>> filterList = this.getDAO().queryForList(filterSQL, args);
		
		for(Map<String,Object> map:list){
			int id = (Integer)map.get("id");
			for(int i=0;i<filterList.size();i++){
				int value = Integer.parseInt(filterList.get(i).get("filterValue").toString());
				if(id == value ){
					map.put("checked", "true");
				}
			}
		}
		return list;
	}
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
//	public List<Map<String,Object>> getSelectGroupList(int filterId);
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectUserList(int filterId){
		
		String sql = "select id,name,parentId as pId,'true' as nocheck from pubmodule_department_tbl where delTag = 0 order by sortNo desc";
		String userSQL = "select id,userName as name,department_id as pId from pubmodule_user_tbl where delTag = 0 and department_id in (select id from pubmodule_department_tbl where delTag = 0) order by sortNo desc";
		String filterSQL = "select filterValue from pubmodule_selectfilterdetail_tbl where filterId=? and filterType=?";
		Object[] args = new Object[2];
		args[0] = filterId;
		args[1] = PlatFormConstant.FILTER_TYPE_USER;
		List<Map<String,Object>> list = this.getDAO().queryForList(sql);
		List<Map<String,Object>> userList = this.getDAO().queryForList(userSQL);
		List<Map<String,Object>> filterList = this.getDAO().queryForList(filterSQL, args);
		
		for(Map<String,Object> map:userList){
			long id = (Long)map.get("id");
			for(int i=0;i<filterList.size();i++){
				int value = Integer.parseInt(filterList.get(i).get("filterValue").toString());
				if(id == value ){
					map.put("checked", "true");
				}
			}
		}
		list.addAll(userList);
		return list;
	}
	
	
	 /**
	 * @param param 过滤ID
	 */
	public void deleteByFilterId(FilterDetail filterDetail){
		String sql = "delete from pubmodule_selectfilterdetail_tbl where filterId = ? and filterType = ?";
		Object[] args = new Object[2];
		args[0] = filterDetail.getFilterId();
		args[1] = filterDetail.getFilterType();
		this.getDAO().update(sql, args);
	}
	
	 /**
	 * @param param 过滤类型，过滤ID,过滤值字符串
	 */
	public void addFilterDetails(List<FilterDetail> list){
		this.batchSave(list);
	}
	
}


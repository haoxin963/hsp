package net.hsp.service.sys.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.widget.Favorite;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.service.sys.select.FilterService;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * 菜单收藏业务类
 * 
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "菜单收藏业务类")
@Lazy(true)
public class FavoriteServiceImpl extends BaseServiceImpl implements FavoriteService {

	String innerSql = "select f.* ,a.functionName from pubmodule_favorite_tbl f inner join pubmodule_function_tbl a on f.functionId = a.functionId ";
	String strSql = "select functionId,functionName from pubmodule_function_tbl ";

	@MethodLogUtil(type = "", value = "多条件查询菜单收藏")
	public Map<String, Object> query(Map param, PageInfo p, int userId) {
		StringBuilder sb = new StringBuilder(innerSql + " where userId = " + userId);
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		sb.append(" order by f.sortNo desc");
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	@MethodLogUtil(type = "", value = "根据菜单ID对应的菜单搜藏")
	public Map<String, Object> getFavoriteByFunctionId(String functionId, int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = innerSql + " where f.functionId = ? and f.userId = " + userId;
		Object[] args = new Object[1];
		args[0] = functionId;

		// Map<String,Object> map = this.getDAO().queryForMap(sql, args);
		List<Map<String, Object>> list = (List<Map<String, Object>>) this.getDAO().queryForList(sql, args);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			String sql2 = strSql + " where functionId = ?";
			return this.getDAO().queryForMap(sql2, args);
		}
	}

	@MethodLogUtil(type = "", value = "查询所有菜单收藏")
	public List<Map<String, Object>> all(int userId) {
		StringBuilder sb = new StringBuilder("select id,name,parentId as pid from pubmodule_department_tbl where delTag = 0 order by sortNo desc ");
		List<Map<String, Object>> list = this.getDAO().queryForList(sb.toString());
		return list;
	}

	@MethodLogUtil(type = "", value = "按部门选人")
	public List<Map<String, Object>> doSelectDepartment(Map param) {
		int filterId = 0;
		boolean person = false;
		boolean aSync = false;
		if (!StringUtil.isEmpty(param.get("filterId")))
			filterId = Integer.parseInt(param.get("filterId").toString());
		if (StringUtil.getString(param.get("isPerson")).equals("1"))
			person = true;
		if (StringUtil.getString(param.get("aSync")).equals("1"))
			aSync = true;

		// String userSQLSelect = "select userName as id,trueName as
		// name,department_id as pId from pubmodule_user_tbl where delTag = 0
		// and department_id in (select id from pubmodule_department_tbl where
		// delTag = 0 )";
		// String userSQLWhere = " ";

		String deptSql = "";
		String userSql = "select id,trueName as name,department_id as pId,'1' as user from pubmodule_user_tbl where delTag = 0 and department_id in (select id from pubmodule_department_tbl where delTag = 0) ";
		if (aSync) {// 异步加载
			String parentId = StringUtil.getString(param.get("parentId"));
			if (StringUtil.isEmpty(parentId)) {// 首次展示一级节点
				deptSql += "select id,name,parentId as pId,'true' as isParent from pubmodule_department_tbl where delTag = 0 and parentId in (select id from pubmodule_department_tbl where parentId = 0) or parentId = 0 ";
				userSql += "and department_id in (select id from pubmodule_department_tbl where parentId = 0) or department_id = 0 ";
			} else {// 非一级节点展开
				deptSql += " select id,name,parentId as pId,'true' as isParent from pubmodule_department_tbl where delTag = 0 and parentId = " + parentId;
				userSql += "and department_id = " + parentId;
			}
		} else {// 同步加载
			deptSql += "select id,name,parentId as pId from pubmodule_department_tbl where delTag = 0";
		}

		deptSql += " order by parentId asc,sortNo desc";
		userSql += " order by sortNo desc";
		/*
		 * String deptSQLSelect = "select id,name,parentId as pId from
		 * pubmodule_department_tbl where delTag = 0 "; String deptSQLWhere = "
		 * and id in (select department_id from pubmodule_user_tbl " +
		 * userSQLWhere + ") ";
		 */
		List<Map<String, Object>> deptList = this.getDAO().queryForList(deptSql);
		/*
		 * if(!aSync){//同步 deptList = addParentList(deptList); }
		 */
		if (person) {// 需要显示人员
			List<Map<String, Object>> userList = this.getDAO().queryForList(userSql);
			deptList.addAll(userList);
		}
		return deptList;
	}

	private List<Map<String, Object>> addParentList(List<Map<String, Object>> list) {
		int oldSize = list.size();
		StringBuffer sb = new StringBuffer("select id,name,parentId as pId from pubmodule_department_tbl where id in " + StringUtil.getWhereInNumber(list, "id")
				+ " order by parentId asc ,sortNo desc");
		list = StringUtil.getMergeList(list, this.getDAO().queryForList(sb.toString()));
		if (list.size() > oldSize) {
			addParentList(list);
		}
		return list;
	}
}
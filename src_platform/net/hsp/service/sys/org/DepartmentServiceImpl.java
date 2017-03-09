package net.hsp.service.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.entity.sys.org.Department;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_department_tbl业务类
 * 
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "部门业务类")
@Lazy(true)
public class DepartmentServiceImpl extends BaseServiceImpl implements DepartmentService {

	@Autowired
	private PostService postService;

	@MethodLogUtil(type = "", value = "多条件查询部门")
	public Map query(Map param, PageInfo p) {
		String pid = (String) param.get("parent_Id");
		param.remove("parent_Id");
		StringBuilder sb = new StringBuilder("select departmentName as name,shortName,departmentNumber as code,parent_Id as parentId,sortNo,departmentIntroduction as remark,child as hasChild,domain, departmentId as id,isdisplay from pubmodule_department_tbl where delTag = '0'  ");
		Map sqlBuild = SQLBuild.buildLike(param);
		if (StringUtils.isNotBlank(pid) && !"ALL".equalsIgnoreCase(pid)) {
			sb.append(" and parent_id = '" + pid + "'");
		}
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		sb.append(" order by sortNo asc");
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

	@MethodLogUtil(value = "多条件查询部门")
	public List<Department> queryDept(FilterMap filterMap) throws ServiceException {
		String sql = "select * from pubmodule_department_tbl where delTag='0'";
		Map sqlBuild = SQLBuild.buildEquals(filterMap);
		sql += sqlBuild.get("sql");
		return this.find(sql, (Object[]) sqlBuild.get("args"), Department.class);
	}

	@Override
	public List<Map<String, Object>> buildTree(Map map, PageInfo p) {
		String sql = "select departmentName as name,departmentName as shortName,departmentNumber as code,parent_Id as parentId ,sortNo,departmentIntroduction as remark,child as hasChild,domain, departmentId as id,isdisplay from pubmodule_department_tbl where delTag = ? order by sortNo asc";
		return this.find(sql, new Object[] { "0" });
	}

	@Override
	public Department addDepartment(Department department) {
		// TODO Auto-generated method stub
		// 得到排序号
		int sortNo = getSavingDataSortNo(department);
		// 更新受影响数据的排序号
		updateOthersSortNo(sortNo, true);
		// 设置要保存数据的排序号
		department.setSortNo(sortNo);
		// 兼容老平台结构的id
		String id = this.getDAO().queryForObject("select max(departmentId+0)+1 from pubmodule_department_tbl", String.class);
		department.setDepartmentId(id);
		this.save(department);
		// 更新受影响数据的hasChild字段
		updateHasChild();
		return department;
	}

	@Override
	public int batchDelDepartmentByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);

		// 得到需要删除数据的ID以及序号
		String sql1 = "select departmentId,sortNo from pubmodule_department_tbl where delTag = '0' and departmentId in (" + idsStr + ")";
		List<Map<String, Object>> list = this.find(sql1, new Object[] {});
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mp = list.get(i);
			String id = mp.get("departmentId").toString();
			String sno = mp.get("sortNo").toString();
			updateOthersSortNo(Integer.parseInt(sno), false);
		}

		String sql = "update pubmodule_department_tbl set delTag = '1' where departmentId in (" + idsStr + ")";
		this.getDAO().execute(sql);
		// 跟新关联数据的hasChild字段
		updateHasChild();

		// 删除岗位
		postService.delPostByDeptId(ids);

		return 1;
	}

	@Override
	public int delDepartmentById(String id) {
		// TODO Auto-generated method stub
		int sno = getDeletingDataSortNo(id);
		// 更新其他受影响的数据的排序号
		updateOthersSortNo(sno, false);
		String sql = "update pubmodule_department_tbl set delTag = '1' where departmentId = " + id;
		this.getDAO().execute(sql);
		updateHasChild();

		// 删除岗位
		postService.delPostByDeptId(new String[] { id });

		return 1;
	}

	@Override
	public int modifyDepartment(Department department) {
		// TODO Auto-generated method stub
		return this.update(department);
	}

	@Override
	public List<Map<String, Object>> getAllChildDepartmentById(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from pubmodule_department_tbl where delTag = ? and parent_id = ? ";
		List<Map<String, Object>> deptList = (List<Map<String, Object>>) this.find(sql, new Object[] { "0", id }, Department.class);
		return deptList;
	}

	@Override
	public int changeDepartmentLevel(String id, String type, String parentId, String[] sortList) {
		// TODO Auto-generated method stub
		// 更新父节点
		if ("1".equals(type)) {
			String sql1 = "update pubmodule_department_tbl d set d.parent_id = ? where d.departmentId=? ";
			this.getDAO().update(sql1, new Object[] { parentId, id });
		}
		// 更新所有关联节点的顺序

		if (sortList != null && sortList.length > 0) {
			for (String str : sortList) {
				if (str == null || "".equals(str)) {
					break;
				}
				String[] arr = str.split(",");
				String sql2 = "update pubmodule_department_tbl d set d.sortNo = ? where d.departmentId = ? ";
				this.getDAO().update(sql2, new Object[] { arr[1], arr[0] });
			}
		}

		// 更新haschild字段
		updateHasChild();

		return 1;
	}

	@Override
	public void updateHasChild() {
		// TODO Auto-generated method stub
		// 把有子节点的child=0字段更新为1
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" update pubmodule_department_tbl d set d.child='1'  ");
		sb1.append(" where d.departmentId in ( ");
		sb1.append(" select departmentId from  ");
		sb1.append("  (  ");
		sb1.append(" 	select d2.departmentId from pubmodule_department_tbl d2 where d2.delTag ='0' and d2.child='0' and   ");
		sb1.append("    exists (select d1.departmentId from pubmodule_department_tbl d1 where d1.delTag='0' and d1.parent_id = d2.departmentId) ");
		sb1.append("  ) a ");
		sb1.append(" ) ");
		this.getDAO().execute(sb1.toString());

		// 把没有子节点child=1字段跟新为0
		StringBuffer sb2 = new StringBuffer();
		sb2.append(" update pubmodule_department_tbl d set d.child='0'  ");
		sb2.append(" where d.departmentId in ( ");
		sb2.append(" select departmentId from  ");
		sb2.append("  (  ");
		sb2.append(" 	select d2.departmentId from pubmodule_department_tbl d2 where d2.delTag ='0' and d2.child='1' and not  ");
		sb2.append("   exists (select d1.departmentId from pubmodule_department_tbl d1 where d1.delTag='0' and d1.parent_id = d2.departmentId) ");
		sb2.append("  ) a ");
		sb2.append(" ) ");
		this.getDAO().execute(sb2.toString());
	}

	@Override
	public int getSavingDataSortNo(Department department) {
		// TODO Auto-generated method stub
		int sNo = 0;
		// 新增数据
		if (department.getDepartmentId() == null || "".equals(department.getDepartmentId())) {
			// post.getParentid();
			String sql = "select sortNo from pubmodule_department_tbl where delTag = '0' and parent_id=? order by sortNo desc";
			List<Map<String, Object>> list = this.find(sql, new Object[] { department.getParentId() });
			if (list.size() == 0) {
				sNo = 1;
			} else {
				Map<String, Object> mp = list.get(0);
				String stno = mp.get("sortNo").toString();
				sNo = Integer.parseInt(stno) + 1;
			}
		} else {
			// 更新数据
			sNo = department.getSortNo();
		}
		return sNo;
	}

	@Override
	public int getDeletingDataSortNo(String id) {
		int sortNo = 0;
		String sql = "select sortNo from pubmodule_department_tbl where delTag = 0 and departmentId = ? ";
		List<Map<String, Object>> list = this.find(sql, new Object[] { id });
		if (list.size() > 0) {
			sortNo = Integer.parseInt(list.get(0).get("sortNo").toString());
		}
		return sortNo;
	}

	@Override
	public void updateOthersSortNo(int sortNo, boolean isAdd) {
		String sql = "";
		if (isAdd) {
			sql = "update pubmodule_department_tbl set sortNo = sortNo+1 where sortNo>? ";
		} else {
			sql = "update pubmodule_department_tbl set sortNo = sortNo-1 where sortNo>? ";
		}
		this.getDAO().update(sql, sortNo);
	}

	@Override
	public Department getDeptByCode(String code) {
		// TODO Auto-generated method stub
		String sql = "select * from pubmodule_department_tbl d where d.code = ?";
		List<Department> departlist = find(sql, new Object[] { code }, Department.class);
		if (departlist.size() > 0) {
			return departlist.get(0);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getChilrenDeptByParentId(String parentId) {
		// TODO Auto-generated method stub
		String sql = "select d.* from pubmodule_department_tbl d where d.delTag='0' d.parent_id = ? ";
		List<Map<String, Object>> list = find(sql, new Object[] { parentId });
		return list;
	}

	@Override
	public int checkDeptHasChild(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (i == ids.length - 1) {
				sb.append(ids[i]);
			} else {
				sb.append(ids[i] + ",");
			}
		}
		String sql = "select d.* from pubmodule_department_tbl d where d.delTag='0' and d.parent_id in (" + sb.toString() + ") ";
		List list = find(sql, new Object[] {});
		if (list.size() > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> buildDeptUserTree(Map map, PageInfo p) {
		//name改成truename 2017-3-1
		String sql = "select t.cls, t.id,t.parentId as pid,t.name,t.sortNo,t.trueName from  ( select 'd' as cls, CONCAT('XXXX',d.departmentId) as id,CONCAT('XXXX',d.parent_Id) as parentId,d.departmentName as name,d.sortNo as sortNo,'' as trueName from pubmodule_department_tbl d where d.delTag = 0  union  \tselect 'u' as cls, CAST(p.id as DBSTRING) as id, CONCAT('XXXX',p.department_id )  as parentId,p.trueName as name, p.sortNo as sortNo,p.trueName as trueName from pubmodule_user_tbl p where p.username!='administrator' and p.delTag = 0  and not exists (select d1.departmentId from pubmodule_department_tbl d1 where  d1.delTag='1' and  d1.departmentId = p.department_id )  ) t order by t.cls,t.sortNo asc";
		sql = DbInfo.castString(sql, 20, DbInfo.getDbType());
		// String sql =
		// "select t.cls, t.id,t.parentId as pid,t.name,t.sortNo,t.trueName from  ( select 'd' as cls, CAST(d.departmentId as DBSTRING) as id,CAST(d.parent_Id as DBSTRING) as parentId,d.departmentName as name,d.sortNo as sortNo,'' as trueName from pubmodule_department_tbl d where d.delTag = 0  union  	select 'u' as cls, CAST(p.id as DBSTRING) as id, CAST(p.department_id as DBSTRING)  as parentId,p.username as name, p.sortNo as sortNo,p.trueName as trueName from pubmodule_user_tbl p where p.username!='"+PlatFormConstant.ADMIN_USERNAME+"' and p.delTag = 0  and not exists (select d1.departmentId from pubmodule_department_tbl d1 where  d1.delTag='1' and  d1.departmentId = p.department_id )  ) t order by t.cls,t.sortNo asc";
		// sql = DbInfo.castString(sql, 20, DbInfo.getDbType());
		return this.find(sql, null);
	}

}

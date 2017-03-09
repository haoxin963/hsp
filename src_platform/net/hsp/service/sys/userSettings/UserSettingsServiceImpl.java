package net.hsp.service.sys.userSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 员工信息业务类
 * 
 * @author 33
 */
@Service
@ServiceLogUtil(name = "员工信息业务类")
@Lazy(true)
public class UserSettingsServiceImpl extends BaseServiceImpl implements UserSettingsService {

	@MethodLogUtil(type = "", value = "多条件查询员工信息")
	public Map<String, Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select id,empno,empname,deptId  from pubmodule_employee_tbl where 1=1");
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
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

	@MethodLogUtil(type = "", value = "多条件查询员工信息")
	public Map<String, Object> queryI(String id) {
		StringBuilder sb = new StringBuilder(
				"select e.id as employeeId,e.deptId,p.postName,e.mobile ,e.officeTel,e.email,u.trueName from pubmodule_employee_tbl e INNER JOIN pubmodule_employeepost_tbl ep ON ep.empid = e.id inner join pubmodule_post_tbl p on p.id=ep.postId INNER JOIN pubmodule_user_tbl u ON u.isCreateStaff =e.id where u.id="
						+ id);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		list = this.getDAO().queryForList(sb.toString());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return map;
	}
}

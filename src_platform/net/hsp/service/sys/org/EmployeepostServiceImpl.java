package net.hsp.service.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.org.Employeepost;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 员工岗位信息业务类
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "员工岗位信息业务类")
@Lazy(true)
public class EmployeepostServiceImpl extends BaseServiceImpl implements EmployeepostService { 

	@MethodLogUtil(type="",value="多条件查询员工岗位信息")
	public Map query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select id, empid,postId,isMainPost,delTag from pubmodule_employeepost_tbl where delTag = '0' "); 
		//deptid不能用like
		String deptid = param.get("e.deptId") == null ? "" : param.get("e.deptId").toString();
			if ((!deptid.equals("")) && (!deptid.equals("0001"))) {
		      sb.append(" and e.deptId='" + deptid + "' ");
		    }
		param.remove("e.deptId");
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

	@Override
	public Employeepost addEmployeepost(Employeepost employeepost) {
		// TODO Auto-generated method stub
		this.save(employeepost);
		return employeepost;
	}

	@Override
	public int batchEmployeepostByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		String sql = "update pubmodule_employeepost_tbl set delTag = '1' where id in ("
				+ idsStr + ")";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int delEmployeepostById(String id) {
		// TODO Auto-generated method stub
		String sql = "update pubmodule_employeepost_tbl set delTag = '1' where id = " + id;
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int modifyEmployeepost(Employeepost employeepost) {
		// TODO Auto-generated method stub
		this.update(employeepost);
		return 1;
	}

	

}


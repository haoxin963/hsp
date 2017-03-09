package net.hsp.service.sys.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.tool.Toolcolor;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_toolcolor_tbl业务类
 * @author lk0513
 */
@Service
@ServiceLogUtil(name = "pubmodule_toolcolor_tbl业务类")
@Lazy(true)
public class ToolcolorServiceImpl extends BaseServiceImpl implements ToolcolorService { 

	@MethodLogUtil(type="",value="多条件查询pubmodule_toolcolor_tbl")
	public Map<String,Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select id,toolname   from pubmodule_tool_tbl where 1=1"); 
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
	@MethodLogUtil(type="",value="根据工具id和属性Id还有用户id找到对应的属性值")
	public Map<String, Object> find(Integer toolid, String userid, String styleid) {
		String sql="select stylevalue from pubmodule_toolcolor_tbl where toolid= "+toolid+" and userid="+userid+"  and styleid="+styleid;
		return this.getDAO().queryForMap(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据用户名找到userid")
	public Map<String, Object> findbyUsername(String username) {
		String sql="select id from pubmodule_user_tbl where username='"+username+"' limit 1";
		return this.getDAO().queryForMap(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据用户id和组件id删除数据")
	public  int delete(String userid, Integer toolid) {
		String sql="delete from pubmodule_toolcolor_tbl where userid="+userid+" and toolid="+toolid;
		return this.getDAO().update(sql);
	}

	@Override
	@MethodLogUtil(type="",value="列出所有组件")
	public List<Map<String, Object>> list() {
		String sql="select id,toolclass from pubmodule_tool_tbl";
		
		return this.getDAO().queryForList(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据userid找到css样式")
	public Map<String, Object> findbyUserid(String userid) {
		String sql="select usercss,id from pubmodule_usertool_tbl where userid="+userid;
		return this.getDAO().queryForMap(sql);
	}

	@Override
	@MethodLogUtil(type="",value="插入css样式")
	public int insertusertool(String userid, String filepath) {
		String sql="insert into pubmodule_usertool_tbl(userid,usercss) values('"+userid+"','"+filepath+"')";
		return this.getDAO().update(sql);
	}

	@Override
	@MethodLogUtil(type="",value="改变css样式")
	public int updateusertool(String userid, String filepath) {
		String sql="update pubmodule_usertool_tbl set usercss='"+filepath+"' where userid="+userid;
		return this.getDAO().update(sql);
	}



	
	}


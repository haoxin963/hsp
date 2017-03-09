package net.hsp.service.sys.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.tool.Toolstyle;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_toolstyle_tbl业务类
 * @author lk0513
 */
@Service
@ServiceLogUtil(name = "pubmodule_toolstyle_tbl业务类")
@Lazy(true)
public class ToolstyleServiceImpl extends BaseServiceImpl implements ToolstyleService { 

	@MethodLogUtil(type="",value="多条件查询pubmodule_toolstyle_tbl")
	public Map<String,Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select *   from pubmodule_toolstyle_tbl where 1=1"); 
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
	@MethodLogUtil(type="",value="找到所有的属性")
	public List<Map<String, Object>> queryall() {
		String sql="select stylename,id from pubmodule_toolstyle_tbl";
		return this.getDAO().queryForList(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据组件id和属性Id 判断是否存在数据")
	public Map<String,Object> check(Integer toolid,String styleid){
		String sql="select id from pubmodule_toollink_tbl  where styleid="+styleid+" and toolid="+toolid+" limit 1";
		return this.getDAO().queryForMap(sql);
		
	}

	@Override
	@MethodLogUtil(type="",value="保存对应的组件Id和属性id")
	public int insert(Integer toolid, String styleid) {
		String sql="insert into pubmodule_toollink_tbl (toolid,styleid) values("+toolid+","+styleid+")";
		return this.getDAO().update(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据组件id删除数据")
	public int delete(Integer toolid) {
		String sql="delete from pubmodule_toollink_tbl where toolid="+toolid;
		return this.getDAO().update(sql);
	}

	@Override
	@MethodLogUtil(type="",value="根据组件id找到组件的属性")
	public List<Map<String, Object>> findByToolid(Integer toolid) {
		String sql="select b.id,b.stylename,b.styletype,b.stylevalue,b.stylefunction,b.filepath from pubmodule_toollink_tbl as a left join  pubmodule_toolstyle_tbl as b on b.id=a.styleid where a.toolid="+toolid;
	
		return this.getDAO().queryForList(sql);
	}

	}


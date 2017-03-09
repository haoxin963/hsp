package net.hsp.service.sys.manage;

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
 * 客户业务类
 * @author aa
 */
@Service(value="custServiceImpl")
@ServiceLogUtil(name = "客户业务类")
@Lazy(true)
public class CustomerServiceImpl extends BaseServiceImpl implements CustomerService { 

	@MethodLogUtil(type="",value="多条件查询客户")
	public Map<String,Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select address,custName,phone,status, custId   from uisp_customer_tbl where 1=1"); 
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


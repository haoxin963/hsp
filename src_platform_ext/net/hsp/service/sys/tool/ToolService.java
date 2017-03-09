package net.hsp.service.sys.tool;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_tool_tbl业务接口
 * @author lk0513
 */
public interface ToolService extends BaseService  {
	
	 /**
	 * 多条件查询pubmodule_tool_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);

		 
}
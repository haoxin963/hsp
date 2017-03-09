package net.hsp.service.sys.select;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_selectfilter_tbl业务接口
 * @author smartTools
 */
public interface FilterService extends BaseService  {
	
	 /**
	 * 多条件查询pubmodule_selectfilter_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);
	 
	 /**
	 * 查询过滤人员列表
	 * @param 过滤ID int
	 * @return  数据集合 list<Map<String,Object>>
	 */
	public List<Map<String,Object>> getFilterUserIdList(int filterId);
}
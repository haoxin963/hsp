package net.hsp.service.sys.select;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.select.FilterDetail;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_selectfilterdetail_tbl业务接口
 * @author smartTools
 */
public interface FilterDetailService extends BaseService  {
	
	 /**
	 * 多条件查询pubmodule_selectfilterdetail_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(int filterId,Map param,PageInfo p);
	 
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectRoleList(int filterId);
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectDeptList(int filterId);
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
//	public List<Map<String,Object>> getSelectGroupList(int filterId);
	
	 /**
	 * @param param 过滤ID
	 * @return 数据集合 list<Map>
	 */
	public List<Map<String,Object>> getSelectUserList(int filterId);
	
	 /**
	 * @param param 过滤类型，过滤ID
	 */
	public void deleteByFilterId(FilterDetail filterDetail);
	
	 /**
	 * @param param 过滤类型，过滤ID,过滤值字符串
	 */
	public void addFilterDetails(List<FilterDetail> list);
}
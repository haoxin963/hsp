package net.hsp.service.sys.widget;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 菜单收藏业务接口
 * @author smartTools
 */
public interface FavoriteService extends BaseService  {
	
	 /**
	 * 多条件查询菜单收藏
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p,int userId);


	 /**
	 * 获取指定收藏菜单
	 * @return Map<String,Object>
	 */
	public Map<String,Object> getFavoriteByFunctionId(String functionId,int userId);
	 
	 /**
	 * 获取所有菜单收藏
	 * @return 数据集合 list<String,Object>
	 */
	public List<Map<String,Object>> all(int userId);
	
	//按部门选人
	public List<Map<String,Object>> doSelectDepartment(Map param);
	
}
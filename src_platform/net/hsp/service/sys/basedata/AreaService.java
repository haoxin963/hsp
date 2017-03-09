package net.hsp.service.sys.basedata;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.basedata.Area;
import net.hsp.service.BaseService;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

/**
 * 区域业务接口
 * @author lk0182
 */
public interface AreaService extends BaseService  {
	
 
	
	public List buildTree(Map map,PageInfo p);
	
	/**
	 * 多条件查询
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map param, PageInfo p) ;
	 
	 
}
package net.hsp.service.sys.manage;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 客户业务接口
 * @author aa
 */
public interface CustomerService extends BaseService  {
	
	 /**
	 * 多条件查询客户
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);

		 
}
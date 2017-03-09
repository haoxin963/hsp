package net.hsp.service.sys.notice;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_noticecategory_tbl业务接口
 * @author smartTools
 */
public interface NoticeCategoryService extends BaseService {

	/**
	 * 多条件查询pubmodule_noticecategory_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> query(Map param, PageInfo p);

	/**
	 * 组件简单树选择
	 * @return  数据集合list<Map>
	 */
	public List<Map<String, Object>> buildTree(Map param, PageInfo p);

	/**
	 * 检查代码是否已存在
	 * @return  数据集合list<Map>
	 */
	public boolean doCheckCode(String code);

	/**
	 * 组件简单树选择
	 * @return  数据集合list<Map>
	 */
	public List<Map<String, Object>> simpleTree();
}
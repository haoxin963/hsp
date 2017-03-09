package net.hsp.service.sys.userSettings;

import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 员工信息业务接口
 * 
 * @author 33
 */
public interface UserSettingsService extends BaseService {

	/**
	 * 多条件查询员工信息
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> query(Map param, PageInfo p);

	public Map<String, Object> queryI(String id);

}
package net.hsp.service.sys.notice;

import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_notice_tbl业务接口
 * 
 * @author smartTools
 */
public interface NoticeService extends BaseService {

	/**
	 * 查询一般公告
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> queryAll(Map param, PageInfo p);

	/**
	 * 查询推送公告
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> queryUser(Map param, PageInfo p);

	/**
	 * 前台查询所有公告
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> queryView(Map param, PageInfo p);

	/**
	 * 批量发布
	 * 
	 * @param id
	 *            信息编号
	 * @return Map<String,Object>
	 */
	public boolean batchRelease(String[] ids, int userId);

	/**
	 * 查看统计
	 * 
	 * @param id
	 *            信息编号
	 * @return Map<String,Object>
	 */
	public Map<String, Object> viewTotal(String id);

	/**
	 * 前台查看
	 * 
	 * @param noticeId
	 *            信息编号 ， userId 用户编号
	 */
	public Map<String, Object> view(String noticeId, String userId);
}
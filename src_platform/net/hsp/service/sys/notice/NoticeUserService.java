package net.hsp.service.sys.notice;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.notice.Notice;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_noticeuser_tbl业务接口
 * 
 * @author smartTools
 */
public interface NoticeUserService extends BaseService {

	/**
	 * 多条件查询pubmodule_noticeuser_tbl
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	// public Map<String,Object> query(Map param,PageInfo p);
	/**
	 * 推送公告用户操作
	 * 
	 * @param int
	 *            noticeId 公告ID
	 * @param String
	 *            userIds 用户ID字符串
	 * @return
	 */
	public void createNoticeUsers(int noticeId, String userIds);

	/**
	 * 一般公告用户操作
	 * 
	 * @param int
	 *            noticeId 公告ID
	 * @return
	 */
	public void createNoticeUsers(int noticeId);
	/*
	 * public Map<String,Object> loadReceipt(String noticeId,String userId);
	 * 
	 * public boolean saveReceipt(String noticeId,String userId,String content);
	 */
}
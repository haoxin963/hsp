package net.hsp.service.sys.notice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.entity.sys.notice.Notice;
import net.hsp.entity.sys.notice.NoticeComment;
import net.hsp.entity.sys.notice.NoticeUser;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_notice_tbl业务类
 * 
 * @author smartTools
 */
@Service("noticeServiceImpl2")
@ServiceLogUtil(name = "pubmodule_notice_tbl业务类")
@Lazy(true)
public class NoticeServiceImpl extends BaseServiceImpl implements NoticeService {

	@MethodLogUtil(type = "", value = "多条件查询pubmodule_notice_tbl")
	public Map<String, Object> queryAll(Map param, PageInfo p) {

		StringBuilder sql = new StringBuilder("select n.*,nc.name,nu.userName from pubmodule_notice_tbl n " + "inner join pubmodule_noticecategory_tbl nc on n.categoryId = nc.id "
				+ "left join pubmodule_user_tbl nu on n.releaseUserId = nu.id where alluser = 1");
		List<Object> paramList = new ArrayList<Object>(); 
		if (param != null) {
			String title = StringUtil.getString(param.get("title"));
			String userName = StringUtil.getString(param.get("userName"));
			String categoryId = StringUtil.getString(param.get("categoryId"));
			String createDateStart = StringUtil.getString(param.get("createDateStart"));
			String createDateEnd = StringUtil.getString(param.get("createDateEnd"));
			String taskOpenStart = StringUtil.getString(param.get("taskOpenStart"));
			String taskOpenEnd = StringUtil.getString(param.get("taskOpenEnd"));

			if (!StringUtil.isEmpty(userName)) {
				sql.append(" and n.releaseUserId in (select id from pubmodule_user_tbl where userName like ? ) ");
				paramList.add("%"+ userName +"%");
			}
			if (!StringUtil.isEmpty(title)) {
				sql.append(" and n.title like ? ");
				paramList.add("%"+ title +"%");
			}
			if (!StringUtil.isEmpty(categoryId)) {
				sql.append(" and n.categoryId = ? ");
				paramList.add(categoryId);
			}
			if (!StringUtil.isEmpty(createDateStart)) {
				sql.append(" and n.createTime >= "+DbInfo.TIME_PARAM()+" ");
				paramList.add(createDateStart+" 00:00:00");
			}
			if (!StringUtil.isEmpty(createDateEnd)) {
				sql.append(" and n.createTime <= "+DbInfo.TIME_PARAM()+" ");
				paramList.add(createDateEnd+" 23:59:59");
			}
			if (!StringUtil.isEmpty(taskOpenStart)) {
				sql.append(" and n.taskopen >= "+DbInfo.TIME_PARAM()+" ");
				paramList.add(taskOpenStart+" 00:00:00");
			}
			if (!StringUtil.isEmpty(taskOpenEnd)) {
				sql.append(" and n.taskopen <= "+DbInfo.TIME_PARAM()+" ");
				paramList.add(taskOpenEnd+" 23:59:59");
			}
		}
		sql.append(" order by n.id desc");
		Object[] objs = new Object[paramList.size()];
		paramList.toArray(objs);
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			list = this.getDAO().queryForList(sql.toString(),objs);
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sql.toString(), objs, p);
		}
	}

	@MethodLogUtil(type = "", value = "多条件查询pubmodule_notice_tbl")
	public Map<String, Object> queryUser(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select n.*,nc.name,nu.userName from pubmodule_notice_tbl n " + "inner join pubmodule_noticecategory_tbl nc on n.categoryId = nc.id "
				+ "left join pubmodule_user_tbl nu on n.releaseUserId = nu.id where alluser = 0");
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		sb.append(" order by sortNo desc");
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	public Map<String, Object> queryView(Map param, PageInfo p) {
		Integer userId = Integer.parseInt( (String)param.get("userId") ) ;
		StringBuilder sql = new StringBuilder("select n.*,nc.name,u.userName,u1.userName as updateUserName,nu.userId from pubmodule_notice_tbl n ");
		sql.append(" inner join pubmodule_noticecategory_tbl nc on n.categoryId = nc.id ");
		sql.append(" left join pubmodule_noticeuser_tbl nu on n.id = nu.noticeId ");
		sql.append(" left join pubmodule_user_tbl u on n.releaseUserId = u.id ");
		sql.append(" left join pubmodule_user_tbl u1 on n.updateUserId = u1.id ");
		sql.append(" where n.delTag = 0 and n.status = 1 ");
		sql.append(" and taskopen <= "+DbInfo.TIME_PARAM()+" and ( taskclose is null or taskclose >= "+DbInfo.TIME_PARAM()+" )");
		sql.append(" and ( alluser = 1 or ( alluser = 0 and n.id in (select noticeId from pubmodule_noticeuser_tbl where userId = ? )))");
		List<Object> paramList = new ArrayList<Object>(); 
		paramList.add(StringUtil.getDateTimeString());
		paramList.add(StringUtil.getDateTimeString());
		paramList.add(userId);
		
		if (param != null) {
			String title = StringUtil.getString(param.get("title"));
			String userName = StringUtil.getString(param.get("userName"));
			String categoryId = StringUtil.getString(param.get("categoryId"));
			String createDateStart = StringUtil.getString(param.get("createDateStart"));
			String createDateEnd = StringUtil.getString(param.get("createDateEnd"));
			String taskOpenStart = StringUtil.getString(param.get("taskOpenStart"));
			String taskOpenEnd = StringUtil.getString(param.get("taskOpenEnd"));

			if (!StringUtil.isEmpty(userName)) {
				sql.append(" and n.releaseUserId in (select id from pubmodule_user_tbl where userName like ? ) ");
				paramList.add("%"+ userName +"%");
			}
			if (!StringUtil.isEmpty(title)) {
				sql.append(" and n.title like ? ");
				paramList.add("%"+ title +"%");
			}
			if (!StringUtil.isEmpty(categoryId)) {
				sql.append(" and n.categoryId = ? ");
				paramList.add(categoryId);
			}
			if (!StringUtil.isEmpty(taskOpenStart)) {
				sql.append(" and n.taskopen >= ? ");
				paramList.add(taskOpenStart+" 00:00:00");
			}
			if (!StringUtil.isEmpty(taskOpenEnd)) {
				sql.append(" and n.taskopen <= ? ");
				paramList.add(taskOpenEnd+" 23:59:59");
			}
		}
		sql.append(" order by n.sortNo desc");
		Object[] objs = new Object[paramList.size()];
		paramList.toArray(objs);
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = this.find(sql.toString(), objs);
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sql.toString(), objs, p);
		}
	}

	public boolean batchRelease(String[] ids, int userId) {
		List<Notice> list = new ArrayList<Notice>();
		for (int i = 0; i < ids.length; i++) {
			Notice notice = new Notice();
			notice.setId(Integer.parseInt(ids[i]));
			notice = (Notice) this.findById(notice);
			notice.setReleaseTime(new Date());
			notice.setReleaseUserId(userId);
			notice.setStatus(1);
			if (StringUtil.isEmpty(notice.getTaskopen())) {
				notice.setTaskopen(notice.getReleaseTime());
			}
			list.add(notice);
		}
		this.batchUpdate(list);
		return true;
	}

	@MethodLogUtil(type = "", value = "多条件查询pubmodule_notice_tbl")
	public Map<String, Object> viewTotal(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("select * from pubmodule_notice_tbl where delTag = 0 and id = ?");
		// 消息主体
		map.put("notice", this.getDAO().queryForMap(sql.toString(), new Object[] { id }));
		sql = new StringBuilder("select nc.*,nu.userName from pubmodule_noticecomment_tbl nc ");
		sql.append("left join pubmodule_user_tbl nu on nc.userId = nu.id where nc.type = ? ");
		sql.append("and noticeId = ? and nu.delTag = 0 order by nc.id desc");
		List<Map<String, Object>> list = this.find(sql.toString(), new Object[] { PlatFormConstant.COMMENT_TYPE_NOTICE, id });
		int commentCount = 0;
		Map<String, Object> ncMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			commentCount = list.size();
			ncMap = (Map<String, Object>) list.get(0);
		}
		// 评论数和最新评论
		map.put("commentCount", commentCount);
		map.put("comment", ncMap);

		return map;
	}

	public Map<String, Object> view(String noticeId, String userId) {
		StringBuilder sql = new StringBuilder("update pubmodule_notice_tbl set hits = hits + 1 where id = ?");
		this.getDAO().update(sql.toString(), new Object[] { Integer.parseInt(noticeId) });

		Map<String, Object> map = new HashMap<String, Object>();
		sql = new StringBuilder("select n.*,u.userName as releaseUserName,u1.userName as updateUserName");
		sql.append(" from pubmodule_notice_tbl n ");
		sql.append(" left join pubmodule_user_tbl u on n.releaseUserId = u.id ");
		sql.append(" left join pubmodule_user_tbl u1 on n.updateUserId = u1.id ");
		sql.append(" where n.delTag = 0 and n.id = ?");
		// 消息主体
		Map<String, Object> moticeMap = this.getDAO().queryForMap(sql.toString(), new Object[] { noticeId });
		moticeMap.put("updateTime", StringUtil.getDateHMString(moticeMap.get("updateTime")));
		moticeMap.put("createTime", StringUtil.getDateHMString(moticeMap.get("createTime")));
		moticeMap.put("taskopen", StringUtil.getDateHMString(moticeMap.get("taskopen")));
		map.put("notice", moticeMap);

		sql = new StringBuilder("select nc.*,nu.trueName,nu.userName from pubmodule_noticecomment_tbl nc ");
		sql.append("left join pubmodule_user_tbl nu on nc.userId = nu.id where nc.type = ? ");
		sql.append("and noticeId = ? and nu.delTag = 0 order by nc.id desc");
		List<Map<String, Object>> list = this.find(sql.toString(), new Object[] { PlatFormConstant.COMMENT_TYPE_NOTICE, noticeId });
		map.put("rows", list);
		map.put("total", list.size());

		return map;
	}
	/*
	 * private void noticeView(String noticeId , String userId){ String sql =
	 * "select }
	 */
}

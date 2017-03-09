package net.hsp.service.sys.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.notice.NoticeComment;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_noticecomment_tbl业务类
 * 
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "pubmodule_noticecomment_tbl业务类")
@Lazy(true)
public class NoticeCommentServiceImpl extends BaseServiceImpl implements NoticeCommentService {

	@MethodLogUtil(type = "", value = "多条件查询pubmodule_noticecomment_tbl")
	public Map<String, Object> query(Map param, PageInfo p) {
		String noticeId = StringUtil.getString(param.get("noticeId"));
		String userName = StringUtil.getString(param.get("userName"));
		String dateTimeStart = StringUtil.getString(param.get("dateTimeStart"));
		String dateTimeEnd = StringUtil.getString(param.get("dateTimeEnd"));
		List<Object> paramList = new ArrayList<Object>(); 
		StringBuilder sql = new StringBuilder("select nc.*,nu.userName,nu.trueName from pubmodule_noticecomment_tbl nc left join ");
		sql.append(" pubmodule_user_tbl nu on nc.userId = nu.id where nc.delTag = 0 and type = ? and noticeId = ? ");
		paramList.add(PlatFormConstant.COMMENT_TYPE_NOTICE);
		paramList.add(noticeId);
		if (!StringUtil.isEmpty(userName)) {
			sql.append(" and nc.userId in (select id from pubmodule_user_tbl where userName like ?) ");
			paramList.add("%"+ userName +"%");
		}
		if (!StringUtil.isEmpty(dateTimeStart)) {
			sql.append(" and nc.dateTime >= ?");
			paramList.add(dateTimeStart+" 00:00:00");
		}
		if (!StringUtil.isEmpty(dateTimeEnd)) {
			sql.append(" and nc.dateTime <= ?");
			paramList.add(dateTimeEnd+" 23:59:59");
		}
		Object[] objs = new Object[paramList.size()];
		paramList.toArray(objs);
		
		sql.append(" order by nc.id desc");
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
}

package net.hsp.service.sys.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.notice.NoticeCategory;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_noticecategory_tbl业务类
 * 
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "pubmodule_noticecategory_tbl业务类")
@Lazy(true)
public class NoticeCategoryServiceImpl extends BaseServiceImpl implements NoticeCategoryService {

	@MethodLogUtil(type = "", value = "多条件查询pubmodule_noticecategory_tbl")
	public Map<String, Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder(
				"select nc1.*,nc2.name as pName from pubmodule_noticecategory_tbl nc1 left join (select id,name from pubmodule_noticecategory_tbl) nc2 on nc1.parentId = nc2.id where delTag = 0");
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

	public List<Map<String, Object>> buildTree(Map param, PageInfo p) {
		String sql = "select id,name,parentId,sortNo from pubmodule_noticecategory_tbl where delTag = 0 order by sortNo";
		List<Map<String, Object>> list = this.getDAO().queryForList(sql);
		list.add(StringUtil.getNoticeRoot());
		return list;
	}

	public boolean doCheckCode(String code) {
		String sql = "select id from pubmodule_noticecategory_tbl where delTag = 0 and code = ?";
		List<Map<String, Object>> list = this.getDAO().queryForList(sql, new Object[] { code });
		return list.size() > 0;
	}

	public List<Map<String, Object>> simpleTree() {
		String sql = "select id,name,parentId as pId,sortNo from pubmodule_noticecategory_tbl where delTag = 0 order by sortNo";
		List<Map<String, Object>> list = this.getDAO().queryForList(sql);
		list.add(StringUtil.getNoticeRoot());
		return list;
	}
}

package net.hsp.web.sys.notice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.notice.NoticeComment;
import net.hsp.service.sys.notice.NoticeCommentService;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_noticecomment_tbl控制器
 * 
 * @author smartTools
 */
@Controller
@WebLogUtil(name = "公告评论")
@RequestMapping("/sys/notice/noticeComment")
@Lazy(true)
public class NoticeCommentController {

	private String listURL = "/sys/notice/noticeCommentList";

	@Autowired
	private NoticeCommentService noticeCommentService;

	private final Logger log = Logger.getLogger(NoticeCommentController.class);

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave() {
		try {
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			String noticeId = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("noticeId"));
			String content = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("content"));
			NoticeComment obj = new NoticeComment();
			obj.setNoticeId(Integer.parseInt(noticeId));
			obj.setType(PlatFormConstant.COMMENT_TYPE_NOTICE);
			obj.setUserId(userId);
			obj.setContent(content);
			obj.setDateTime(new Date());
			obj.setDelTag("0");
			noticeCommentService.save(obj);

			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(NoticeComment obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			if (!StringUtil.isEmpty(ids)) {
				noticeCommentService.batchDelete(obj.getClass(), ids.split(","));
			} else {
				noticeCommentService.delete(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(NoticeComment noticeComment, FilterMap filter, PageInfo pageInfo) {
		try {
			String noticeId = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("noticeId"));
			String userName = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("userName"));
			String dateTimeStart = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("dateTimeStart"));
			String dateTimeEnd = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("dateTimeEnd"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("noticeId", noticeId);
			map.put("userName", userName);
			map.put("dateTimeStart", dateTimeStart);
			map.put("dateTimeEnd", dateTimeEnd);
			MV mv = new MV(listURL, WebConstant.COMMAND, noticeCommentService.query(map, pageInfo));
			mv.addObject("noticeId", noticeId);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
}

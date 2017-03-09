package net.hsp.web.sys.notice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.notice.Notice;
import net.hsp.entity.sys.notice.NoticeCategory;
import net.hsp.service.sys.notice.NoticeCategoryService;
import net.hsp.service.sys.notice.NoticeService;
import net.hsp.service.sys.notice.NoticeUserService;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * pubmodule_notice_tbl控制器
 * 
 * @author smartTools
 */
@Controller("noticeController2")
@WebLogUtil(name = "公告")
@RequestMapping("/sys/notice/notice")
@Lazy(true)
public class NoticeController {

	private String allListURL = "/sys/notice/noticeAllList";
	private String userListURL = "/sys/notice/noticeUserList";
	private String formURL = "/sys/notice/noticeForm";
	private String totalURL = "/sys/notice/noticeViewTotal";
	private String viewListURL = "/sys/notice/noticeViewList";
	private String viewURL = "/sys/notice/noticeView";
	private String receiptURL = "/sys/notice/noticeUserForm";
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeCategoryService noticeCategoryService;
	@Autowired
	private NoticeUserService noticeUserService;

	private final Logger log = Logger.getLogger(NoticeController.class);

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Notice obj) {
		try {
			String ismajor = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("ismajor"));
			String iscomment = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("iscomment"));
			String isback = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("isback"));
			String userIds = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("userIds"));
			if (StringUtil.isEmpty(ismajor)) {
				obj.setIsmajor(0);
			} else {
				obj.setIsmajor(Integer.parseInt(ismajor));
			}
			if (StringUtil.isEmpty(iscomment)) {
				obj.setIscomment(0);
			} else {
				obj.setIscomment(Integer.parseInt(iscomment));
			}
			if (StringUtil.isEmpty(isback)) {
				obj.setIsback(0);
			} else {
				obj.setIsback(Integer.parseInt(isback));
			}
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			obj.setReleaseUserId(userId);
			obj.setReleaseTime(new Date());
			if (obj.getId() != null) {
				if (obj.getStatus() == 1) {
					obj.setUpdateTime(new Date());
					obj.setUpdateUserId(userId);
				}
				noticeService.update(obj);
			} else {
				// 新建消息
				obj.setHits(0);
				obj.setStatus(0);
				obj.setDelTag("0");
				obj.setCreateUserId(userId);
				obj.setCreateTime(new Date());
				obj = (Notice) noticeService.save(obj);
			}
			// 更新对应用户信息
			// noticeUserService.createNoticeUsers(obj, userIds);
			obj.setContent("");
			return new MV(allListURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(Notice obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				noticeService.batchDelete(obj.getClass(), ids.split(","));
			} else {
				noticeService.delete(obj);
			}
			return new MV(allListURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "发布")
	@RequestMapping("/doRelease")
	public ModelAndView doRelease(Notice obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			int userId = Integer.parseInt(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			if (StringUtils.isNotBlank(ids)) {
				noticeService.batchRelease(ids.split(","), userId);
			} else {
				obj.setReleaseTime(new Date());
				obj.setReleaseUserId(userId);
				obj.setStatus(1);
				if (StringUtil.isEmpty(obj.getTaskopen())) {
					obj.setTaskopen(obj.getReleaseTime());
				}
				if (StringUtil.isEmpty(obj.getCreateTime())) {
					obj.setCreateTime(obj.getReleaseTime());
				}
				if (StringUtil.isEmpty(obj.getCreateUserId())) {
					obj.setCreateUserId(userId);
				}
				if (obj.getId() == null) {
					noticeService.save(obj);
				} else {
					noticeService.update(obj);
				}
				obj.setContent("");
			}
			return new MV(allListURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Notice obj) {
		try {
			String categoryName = "";
			// 根据id(pk)从DB加载
			if (obj.getId() != null) {
				obj = (Notice) noticeService.findById(obj);
				NoticeCategory nc = new NoticeCategory();
				nc.setId(obj.getCategoryId());
				nc = (NoticeCategory) noticeCategoryService.findById(nc);
				categoryName = nc.getName();
			} else {
				String alluser = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("alluser"));
				obj.setAlluser(Integer.valueOf(alluser));
				obj.setAlluser(Integer.valueOf(StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("alluser"))));
				obj.setHits(0);
				obj.setDelTag("0");
				obj.setAttachpath("/notice/" + (new Date()).getTime());
			}
			MV mv = new MV(formURL, WebConstant.COMMAND, obj);
			mv.addObject("categoryName", categoryName);
			// 相关初始数据加载
			// mv.addObject("x","yy");

			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doAllList")
	public ModelAndView doAllList(Notice notice, FilterMap filter, PageInfo pageInfo) {
		try {
			MV mv = new MV(allListURL, WebConstant.COMMAND, noticeService.queryAll(filter.getFilter(), pageInfo));
			mv.addObject("alluser", "1");
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doUserList")
	public ModelAndView doUserList(Notice notice, FilterMap filter, PageInfo pageInfo) {
		try {
			MV mv = new MV(userListURL, WebConstant.COMMAND, noticeService.queryUser(filter.getFilter(), pageInfo));
			mv.addObject("alluser", "0");
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doViewList")
	public ModelAndView doViewList(Notice notice, FilterMap filter, PageInfo pageInfo) {
		try {
			String userId = HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString();
			if (filter.getFilter() == null) {
				filter.setFilter(new HashMap<String, String>());
			}
			filter.set("userId", userId);
			MV mv = new MV(viewListURL, WebConstant.COMMAND, noticeService.queryView(filter.getFilter(), pageInfo));
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "统计")
	@RequestMapping("/doViewTotal")
	public ModelAndView doViewTotal(@RequestParam(value = "noticeId", required = false)
	String noticeId) {
		try {
			MV mv = new MV(totalURL, WebConstant.COMMAND, noticeService.viewTotal(noticeId));
			mv.addObject("alluser", "0");
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查看")
	@RequestMapping("/doView")
	public ModelAndView doView(@RequestParam(value = "noticeId", required = false)
	String noticeId) {
		try {
			String userId = HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString();
			MV mv = new MV(viewURL, WebConstant.COMMAND, noticeService.view(noticeId, userId));
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	/*
	 * @MethodLogUtil(type="",value="回执") @RequestMapping("/doLoadReceipt")
	 * public ModelAndView
	 * doLoadReceipt(@RequestParam(value="noticeId",required=false) String
	 * noticeId) { try { String userId =
	 * HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString();
	 * MV mv = new MV(receiptURL,
	 * WebConstant.COMMAND,noticeUserService.loadReceipt(noticeId,userId));
	 * 
	 * return mv.fwd(); } catch (Exception e) { return new
	 * MV().processException(e,null); } }
	 * 
	 * @MethodLogUtil(type="",value="回执") @RequestMapping("/doSaveReceipt")
	 * public ModelAndView
	 * doSaveReceipt(@RequestParam(value="noticeId",required=false) String
	 * noticeId) { try { String userId =
	 * HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString();
	 * String content =
	 * StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("content"));
	 * 
	 * MV mv = new MV(receiptURL,
	 * WebConstant.COMMAND,noticeUserService.saveReceipt(noticeId,userId,content));
	 * 
	 * return mv.fwd(); } catch (Exception e) { return new
	 * MV().processException(e,null); } }
	 */
}

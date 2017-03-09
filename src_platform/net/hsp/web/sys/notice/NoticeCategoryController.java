package net.hsp.web.sys.notice;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.notice.NoticeCategory;
import net.hsp.service.sys.notice.NoticeCategoryService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
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
 * pubmodule_noticecategory_tbl控制器
 * 
 * @author smartTools
 */
@Controller
@WebLogUtil(name = "pubmodule_noticecategory_tbl")
@RequestMapping("/sys/notice/noticeCategory")
@Lazy(true)
public class NoticeCategoryController {

	private String listURL = "/sys/notice/noticeCategory";
	private String formURL = "/sys/notice/noticeCategoryForm";
	private String jsonURL = "";

	@Autowired
	private NoticeCategoryService noticeCategoryService;

	private final Logger log = Logger.getLogger(NoticeCategoryController.class);

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(NoticeCategory obj) {
		try {
			if (obj.getId() != null) {
				noticeCategoryService.update(obj);
			} else {
				noticeCategoryService.save(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(NoticeCategory obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				noticeCategoryService.batchDelete(obj.getClass(), ids.split(","));
			} else {
				noticeCategoryService.delete(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(NoticeCategory obj) {
		try {
			boolean isEdit = false;
			String pName = "全部";
			// 根据id(pk)从DB加载
			if (obj.getId() != null) {
				obj = (NoticeCategory) noticeCategoryService.findById(obj);
				if (obj.getParentId() > 0) {
					NoticeCategory pnc = new NoticeCategory();
					pnc.setId(obj.getParentId());
					pName = ((NoticeCategory) noticeCategoryService.findById(pnc)).getName();
				}
			} else {
				isEdit = true;
				int pId = Integer.parseInt(ActionUtil.getCtx().getRequest().getParameter("pId"));
				pName = ActionUtil.getCtx().getRequest().getParameter("pName");
				pName = new String(pName.getBytes("iso-8859-1"), "UTF-8");//新增转码
				obj.setParentId(pId);
				obj.setDelTag("0");
			}
			MV mv = new MV(formURL, WebConstant.COMMAND, obj);
			mv.addObject("pName", pName);
			mv.addObject("isEdit", isEdit);
			// 相关初始数据加载
			// mv.addObject("x","yy");
			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(NoticeCategory noticeCategory, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, noticeCategoryService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "检查")
	@RequestMapping("/doCheckCode")
	public ModelAndView doCheckCode(String code) {
		try {
			return new MV(jsonURL, WebConstant.COMMAND, noticeCategoryService.doCheckCode(code)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "简单选择树")
	@RequestMapping("/simpleTree")
	public ModelAndView simpleTree() {
		try {
			return new MV(listURL, WebConstant.COMMAND, noticeCategoryService.simpleTree()).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
}

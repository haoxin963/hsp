package net.hsp.web.sys.msg;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.msg.Message;
import net.hsp.service.sys.msg.MessageService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * ec_message_tbl控制器
 * 
 * @author lk0516
 */
@Controller
@WebLogUtil(name = "pubmodule_message_tbl")
@RequestMapping("/sys/message")
@Lazy(true)
public class MsgController {

	private String listURL = "/sys/msg/messageList";
	private String formURL = "/sys/msg/messageForm";
	private String statusListURL = "/sys/msg/messageStatusList";
	private String messageViewUrl = "/sys/msg/messageView";

	@Autowired
	private MessageService messageService;

	@MethodLogUtil(type = "", value = "查询未读数")
	@RequestMapping("/unread")
	public ModelAndView unread(HttpServletRequest request) {
		try {
			String currentUserId = request.getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			MV mv = new MV();
			mv.addObject("count", messageService.unread(currentUserId));
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Message obj,String userids) {
		try {
			if (obj.getId() != null) {
				messageService.update(obj);
			} else {
				obj.setSendtime(new Date());
				Set<String> to = new HashSet<String>();
				if(StringUtils.isNotBlank(userids)) {
					userids = userids.replaceAll("，", ",");
					String[] ids = userids.split(",");
					for (int i = 0; i < ids.length; i++) {
						to.add(ids[i]);
					}
				} else {
					to.add("1");
				}
				obj = messageService.save(obj,to);
			}

			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除消息")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(Message obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				messageService.batchDelete(obj.getClass(), ids.split(","));
			} else {
				messageService.delete(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除我的消息")
	@RequestMapping("/doDeleteMine")
	public ModelAndView doDeleteMine(@RequestParam(value = "ids", required = false)
	String ids, HttpServletRequest request) {
		try {
			String currentUserId = request.getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			messageService.doDeleteMine(currentUserId, ids);
			return new MV(listURL, WebConstant.COMMAND, null).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Message obj) {
		try {
			// 根据id(pk)从DB加载
			if (obj.getId() != null) {
				obj = (Message) messageService.findById(obj);
			} else {
				obj = new Message();
				obj.setSendtime(new Date());
			}
			MV mv = new MV(formURL, WebConstant.COMMAND, obj);

			// 相关初始数据加载
			// mv.addObject("x","yy");

			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "阅读我的消息")
	@RequestMapping("/messageView")
	public ModelAndView read(Message obj, HttpServletRequest request) {
		try {
			String currentUserId = request.getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString();
			obj = (Message) messageService.findById(obj);
			// 保存阅读状态
			messageService.readSign(currentUserId, obj.getId().toString());
			MV mv = new MV(messageViewUrl, WebConstant.COMMAND, obj);
			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doStatusList")
	public ModelAndView doStatusList(Message message, FilterMap filter, PageInfo pageInfo, HttpServletRequest request) {
		try {
			String currentUserId = request.getSession().getAttribute(PlatFormConstant.CURRENT_USERID).toString(); 
			Map map = messageService.query(currentUserId, filter.getFilter(), pageInfo);
			return new MV(statusListURL, WebConstant.COMMAND, messageService.query(currentUserId, filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Message message, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, messageService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
}

package net.hsp.web.sys.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.app.UserApp;
import net.hsp.service.sys.app.AppPushMessage;
import net.hsp.service.sys.app.UserAppService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * APP推送控制器
 * 
 * @author nd0100
 */
@Controller
@WebLogUtil(name = "APP推送")
@RequestMapping("/sys/app/userApp")
@Lazy(true)
public class UserAppController {

	private String listURL = "/sys/app/userAppList";
	private String formURL = "/sys/app/userAppForm";

	@Autowired
	private UserAppService userAppService;

	@MethodLogUtil(value = "保存或更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(UserApp obj) {
		MV mv = new MV(listURL);
		userAppService.saveOrUpdate(obj);
		mv.addObject(WebConstant.COMMAND, obj);
		return mv.fwd();
	}

	@MethodLogUtil(value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(@RequestParam(value = "ids", required = false) String ids) {
		MV mv = new MV(listURL);
		String[] id = ids.split(",");
		userAppService.batchDelete(UserApp.class, id);
		mv.addObject(WebConstant.COMMAND, id);
		return mv.fwd();
	}

	@MethodLogUtil(value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(UserApp obj) {
		MV mv = new MV(formURL);
		if (obj.getId() != null) {
			obj = (UserApp) userAppService.findById(obj);
		}
		mv.addObject(WebConstant.COMMAND, obj);
		return mv.fwd();
	}

	@MethodLogUtil(value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(UserApp userApp, FilterMap filter, PageInfo pageInfo) {
		MV mv = new MV(listURL);
		mv.addObject(WebConstant.COMMAND, userAppService.query(filter.getFilter(), pageInfo));
		return mv.fwd();
	}

	@MethodLogUtil(value = "推送消息到所有用户")
	@RequestMapping("/pushToAll")
	public ModelAndView pushToAll(AppPushMessage appMsg) {
		MV mv = new MV(formURL);
		String response = null;
		try {
			response = userAppService.pushToAll(appMsg);
		} catch (IOException e) {
			return new MV().processException(e, null);
		}
		mv.addObject(WebConstant.COMMAND, response);
		return mv.fwd();
	}

	@MethodLogUtil(value = "推送消息到指定用户")
	@RequestMapping("/pushToUsers")
	public ModelAndView pushToUsers(AppPushMessage appMsg, @RequestParam(value = "userIds", required = true) String userIds) {
		MV mv = new MV(formURL);
		String response = null;
		userIds = userIds.replaceAll("，", ",");
		List<String> userList = Arrays.asList(userIds.split(","));
		try {
			response = userAppService.pushToUsers(userList, appMsg);
		} catch (IOException e) {
			return new MV().processException(e, null);
		}
		mv.addObject(WebConstant.COMMAND, response);
		return mv.fwd();
	}
}

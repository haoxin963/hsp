package net.hsp.web.sys.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.DateUtil;
import net.hsp.common.Holder;
import net.hsp.common.schedule.CronExpressionParse;
import net.hsp.common.schedule.ScheduleManager;
import net.hsp.entity.sys.schedule.Schedule;
import net.hsp.entity.sys.schedule.Task;
import net.hsp.service.sys.schedule.ScheduleService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/schedule")
@Scope("prototype")
public class ScheduleController {

	@Autowired
	private ScheduleService scheduleService;

	private String listTaskUrl = "/sys/schedule/taskList";
	private String listScheduleUrl = "/sys/schedule/scheduleList";
	private String formScheduleUrl = "/sys/schedule/scheduleForm";

	private String getCustId() {
		return HttpSessionFactory.getCustId(ActionUtil.getCtx().getRequest());
	}

	/**
	 * 遍历任务
	 * 
	 * @param filter
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping("/listTask")
	public ModelAndView listTask(FilterMap filter, PageInfo pageInfo) {
		Map<?, ?> map = scheduleService.findTask(filter, pageInfo);
		MV mv = new MV(listTaskUrl);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	/**
	 * 添加任务类
	 * 
	 * @return
	 */
	@RequestMapping("/toAddTask")
	public ModelAndView toAddTask() {
		MV mv = new MV("/sys/schedule/taskForm");
		return mv.fwd();
	}

	/**
	 * 添加任务类
	 * 
	 * @param taskClass
	 * @return
	 */
	@RequestMapping("/addTask")
	public ModelAndView addTask(String taskClass) {
		MV mv = new MV("listTaskUrl");
		Holder<String> error = new Holder<String>();
		if (!scheduleService.addTask(taskClass, error)) {
			mv.addObject("status", 0);
			mv.addObject("msg", error.value);
		} else {
			mv.addObject("status", 1);
		}
		return mv.fwd();
	}

	/**
	 * 遍历计划任务
	 * 
	 * @param filter
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping("/listSchedule")
	public ModelAndView listSchedule(FilterMap filter, PageInfo pageInfo) {
		if (null == filter.getFilter()) {
			filter.setFilter(new HashMap<String, String>());
		}
		String custId = this.getCustId();
		filter.set("station", custId);
		Map<?, ?> map = scheduleService.listSchedule(filter, pageInfo);
		MV mv = new MV(listScheduleUrl);
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}

	/**
	 * 添加计划任务
	 * 
	 * @return
	 */
	@RequestMapping("/toAddSchedule")
	public ModelAndView toAddSchedule() {
		MV mv = new MV(formScheduleUrl);
		String custId = getCustId();
		List<Task> tasks = this.scheduleService.ListTask(custId);
		mv.addObject("tasks", tasks);
		mv.addObject("station", custId);
		mv.addObject("command", new Schedule());
		return mv.fwd();
	}

	/**
	 * 编辑计划任务
	 * 
	 * @return
	 */
	@RequestMapping("/toEditSchedule")
	public ModelAndView toEditSchedule(int scheduleId) {
		MV mv = new MV(formScheduleUrl);
		String custId = getCustId();
		List<Task> tasks = this.scheduleService.ListTask(custId);
		mv.addObject("tasks", tasks);
		mv.addObject("station", custId);
		mv.addObject("command", getSchedule(scheduleId));
		return mv.fwd();
	}

	/**
	 * 添加计划任务
	 * 
	 * @return
	 */
	@RequestMapping("/saveSchedule")
	public ModelAndView saveSchedule(Schedule schedule) {
		MV mv = new MV(listScheduleUrl);
		Holder<String> error = new Holder<String>();
		if (saveSchedule(schedule, error)) {
			mv.addObject("status", 1);
		} else {
			mv.addObject("status", 0);
			mv.addObject("msg", error.value);
		}
		return mv.fwd();
	}

	private Schedule getSchedule(int scheduleId) {
		Schedule schedule = new Schedule();
		schedule.setScheduleId(scheduleId);
		schedule = this.scheduleService.findScheduleById(schedule);
		return schedule;
	}

	private boolean saveSchedule(Schedule schedule, Holder<String> error) {
		error.value = "保存失败";
		Integer scheduleId = schedule.getScheduleId();
		if (null != scheduleId && scheduleId > 0) {
			Schedule oldSchedule = this.scheduleService.findScheduleById(schedule);
			if (null == oldSchedule) {
				error.value = "计划任务不存在";
				return false;
			}
			if (oldSchedule.getOpenFlag() == 1) {
				error.value = "请先停止计划任务";
				return false;
			}
			schedule.setCreateDate(oldSchedule.getCreateDate());
		}

		String taskClass = schedule.getTaskClass();
		Task task = this.scheduleService.getTask(taskClass);
		if (null == task) {
			error.value = "计划任务不存在";
			return false;
		}
		String custId = getCustId();
		String station = task.getStation();
		if (null != station && !station.isEmpty() && !station.equals(custId)) {
			error.value = "站点与计划任务不匹配";
			return false;
		}
		schedule.setStation(custId);

		String triggerType = schedule.getTriggerType();
		String description = "";
		if ("simple".equalsIgnoreCase(triggerType)) {
			Date startTime = schedule.getStartTime();
			Integer repeatCount = schedule.getRepeatCount();
			System.out.println("scheduleId=" + scheduleId);
			System.out.println("repeatCount=" + repeatCount);
			if (null == repeatCount || repeatCount < 0) {
				repeatCount = 0;
			}
			Long repeatInterval = schedule.getRepeatInterval();

			description = "从" + DateUtil.getDateTime(startTime) + "开始 ";
			if (repeatInterval > 0) {
				description += "每隔" + repeatInterval + "毫秒 执行一次 ";
			} else {
				repeatCount = 1;
			}
			if (repeatCount > 0) {
				description += "共执行" + repeatCount + "次";
			}
		} else {
			String cronExpression = "";
			CronExpressionParse cep = null;
			if ("advanced".equalsIgnoreCase(triggerType)) {
				cep = new CronExpressionParse(schedule);

			} else {
				cep = new CronExpressionParse(schedule.getCronExpression());
			}
			cronExpression = cep.getCronExpression();
			System.out.println(cronExpression);
			if (CronExpression.isValidExpression(cronExpression)) {
				schedule.setCronExpression(cronExpression);
				description = cep.getSummary();
			} else {
				error.value = "表达式不合法";
				return false;
			}

		}

		schedule.setOpenFlag(0);
		schedule.setDescription(description);
		return this.scheduleService.saveSchedule(schedule);
	}

	/**
	 * 运行计划任务
	 * 
	 * @return
	 */
	@RequestMapping("/start")
	public ModelAndView start(int scheduleId) {
		MV mv = new MV(listScheduleUrl);
		mv.addObject("status", 0);
		Schedule schedule = getSchedule(scheduleId);
		if (null == schedule) {
			mv.addObject("msg", "计划任务不存在");
		} else if (schedule.getOpenFlag() == 1) {
			mv.addObject("msg", "计划任务正在运行");
		} else if (!ScheduleManager.startJob(schedule)) {
			mv.addObject("msg", "运行失败");
		} else {
			this.scheduleService.setScheduleStatus(scheduleId, 1);
			mv.addObject("status", 1);
		}
		return mv.fwd();
	}

	/**
	 * 停止计划任务
	 * 
	 * @return
	 */
	@RequestMapping("/stop")
	public ModelAndView stop(int scheduleId) {
		MV mv = new MV(listScheduleUrl);
		mv.addObject("status", 0);
		Schedule schedule = getSchedule(scheduleId);
		if (null == schedule) {
			mv.addObject("msg", "计划任务不存在");
		} else if (schedule.getOpenFlag() == 0) {
			mv.addObject("msg", "计划任务正在运行");
		} else if (!ScheduleManager.stopJob(schedule)) {
			mv.addObject("msg", "停止失败");
		} else {
			this.scheduleService.setScheduleStatus(scheduleId, 0);
			mv.addObject("status", 1);
		}
		return mv.fwd();
	}

	/**
	 * 遍历计划任务
	 * 
	 * @param filter
	 * @param pageInfo
	 * @return
	 */
	@RequestMapping("/listWholeSchedule")
	public ModelAndView listWholeSchedule(FilterMap filter, PageInfo pageInfo) {
		Map<?, ?> map = scheduleService.listSchedule(filter, pageInfo);
		MV mv = new MV("/sys/schedule/wholeScheduleList");
		mv.addObject(WebConstant.COMMAND, map);
		return mv.fwd();
	}
	
	@RequestMapping("/deleteSchedule")
	public ModelAndView deleteSchedule(String id) {
		MV mv = new MV();
		if(StringUtils.isNotBlank(id)){
			int scheduleId = Integer.parseInt(id);
			int status= scheduleService.deleteSchedule(scheduleId);
			if(status == 0){
				mv.addObject("msg", "正在运行不能删除！");
			} else {
				mv.addObject("msg", "删除成功！");
			}
			mv.addObject("status",status);
		}
		return mv.fwd();
	}
	
	@RequestMapping("/deleteTask")
	public ModelAndView deleteTask(String taskClass) {
		MV mv = new MV();
		if(StringUtils.isNotBlank(taskClass)){
			int status= scheduleService.deleteTask(taskClass);
			if(status == 0){
				mv.addObject("msg", "taskClass为空！");
			} else if (status == 2){
				mv.addObject("msg", "存在关联的调度实例！");
			} else {
				mv.addObject("msg", "删除成功！");
			}
			mv.addObject("status",status);
		}
		return mv.fwd();
	}
}

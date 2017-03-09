package net.hsp.service.sys.schedule;

import java.util.List;

import net.hsp.common.schedule.ScheduleManager;
import net.hsp.entity.sys.schedule.Schedule;
import net.hsp.service.ServiceException;
import net.hsp.web.sys.widget.FavoriteController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoScheduleServiceImpl implements AutoStartService {

	@Autowired
	ScheduleService scheduleService;
	
	private final Logger log = Logger.getLogger(FavoriteController.class);

	@Override
	public Object execute() throws ServiceException {
		log.info("自动启动未完成计划任务...");
		List<Schedule> schedules = scheduleService.listSchedules(true);
		for (Schedule schedule : schedules) {
			String taskClass = schedule.getTaskClass();
			try {
				Class.forName(taskClass);
				ScheduleManager.startJob(schedule);
				log.info("计划任务 id:" + schedule.getScheduleId() + "[" + taskClass + "]自动启动成功");
			} catch (Exception e) {
				log.error("计划任务 id:" + schedule.getScheduleId() + "[" + taskClass + "]自动启动失败");
			}
		}
		return null;
	}
}

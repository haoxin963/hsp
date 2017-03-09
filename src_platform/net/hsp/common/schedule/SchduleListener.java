package net.hsp.common.schedule;

import net.hsp.service.sys.schedule.ScheduleService;
import net.hsp.web.util.SpringCtx;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class SchduleListener implements JobListener {

	@Override
	public String getName() {
		return "uispJobListener";
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext arg0) {

	}

	@Override
	public void jobToBeExecuted(JobExecutionContext arg0) {

	}

	@Override
	public void jobWasExecuted(JobExecutionContext arg0, JobExecutionException arg1) {
		// 计划任务已经结束
		try {
			if (null == arg0.getNextFireTime()) {
				JobDataMap map = arg0.getJobDetail().getJobDataMap();
				if (map.containsKey("scheduleId")) {
					try {
						int scheduleId = map.getInt("scheduleId");
						if (scheduleId > 0) {
							ScheduleService service = (ScheduleService) SpringCtx.getSpringContext().getBean("scheduleServiceImpl");
							service.setScheduleStatus(scheduleId, 2);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

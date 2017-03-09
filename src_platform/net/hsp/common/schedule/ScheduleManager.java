package net.hsp.common.schedule;

import java.text.ParseException;
import java.util.Date;

import net.hsp.entity.sys.schedule.Schedule;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 计划任务管理类
 * 
 * @author caocy
 */
public class ScheduleManager {
	// 单子计划任务工厂
	private static SchedulerFactory schedulerFactory = null;
	// 单子任务
	private static Scheduler scheduler = null;

	private static SchedulerFactory getSchedulerFactory() {
		if (schedulerFactory == null) {
			schedulerFactory = new StdSchedulerFactory();
		}
		return schedulerFactory;
	}

	private static Scheduler getScheduler() throws SchedulerException {
		if (scheduler == null) {
			scheduler = getSchedulerFactory().getScheduler();
			scheduler.addJobListener(new SchduleListener());
		}
		return scheduler;
	}

	/**
	 * 开始任务 并不是直接执行任务，而是把job加载到计划任务中，具体执行以由触发器(trigger)控制
	 * 
	 * @param schedule
	 * @return
	 */
	public static boolean startJob(Schedule schedule) {
		boolean flag = false;
		if (null != schedule) {
			try {
				Scheduler scheduler = getScheduler();
				if (!scheduler.isStarted()) {
					scheduler.start();
					System.out.println("scheduler 开始....");
				}

				String taskClass = schedule.getTaskClass();
				String station = schedule.getStation();

				Integer scheduleId = schedule.getScheduleId();

				JobDetail jobDetail = new JobDetail(scheduleId.toString(), station, Class.forName(taskClass));

				jobDetail.getJobDataMap().put("scheduleId", scheduleId);
				jobDetail.getJobDataMap().put("custId", station);

				jobDetail.addJobListener("uispJobListener");

				Trigger trigger = null;
				if ("simple".equalsIgnoreCase(schedule.getTriggerType())) {
					Date startTime = schedule.getStartTime();
					Long repeatInterval = schedule.getRepeatInterval();
					Integer repeatCount = schedule.getRepeatCount();
					SimpleTrigger simpleTrigger = new SimpleTrigger(station, taskClass);
					if (null == startTime) {
						startTime = new Date();
					}
					simpleTrigger.setStartTime(startTime);
					if (null != repeatInterval && 0 < repeatInterval) {
						simpleTrigger.setRepeatInterval(repeatInterval);
					}

					if (null == repeatCount || 1 > repeatCount) {
						repeatCount = 0;
					}
					// 重复次数需要-1
					simpleTrigger.setRepeatCount(repeatCount - 1);
					trigger = simpleTrigger;

				} else if ("cron".equalsIgnoreCase(schedule.getTriggerType()) || "advanced".equalsIgnoreCase(schedule.getTriggerType())) {
					CronExpression cexp = new CronExpression(schedule.getCronExpression());
					CronTrigger cronTrigger = new CronTrigger(station, taskClass);
					cronTrigger.setCronExpression(cexp);
					trigger = cronTrigger;
				} else {
					return false;
				}
				scheduler.scheduleJob(jobDetail, trigger);
				System.out.println("计划任务 [" + station + "/" + taskClass + "]" + " 加入...");
				flag = true;
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 停止任务
	 * 
	 * @param schedule
	 * @return
	 */
	public static boolean stopJob(Schedule schedule) {
		boolean flag = false;
		try {
			Scheduler scheduler = getScheduler();
			String taskClass = schedule.getTaskClass();
			String station = schedule.getStation();
			scheduler.deleteJob(schedule.getScheduleId().toString(), station);
			System.out.println("计划任务 " + "[" + station + "/" + taskClass + "]" + " 移除...");
			flag = true;
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
}

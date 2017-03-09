package net.hsp.common.schedule;

import java.util.Date;
import java.util.Map;

import net.hsp.common.DateUtil;
import net.hsp.dao.DynamicDataSource;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 计划任务基类
 * 
 * @author caocy
 */
public abstract class BaseTask implements Job {

	/**
	 * 运行计划任务
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			JobDataMap map = arg0.getJobDetail().getJobDataMap();
			if (map.containsKey("custId")) {
				String custId = map.getString("custId");
				DynamicDataSource.setCustId(custId);
			}

			Date startTime = new Date();
			String flag = doJob(map.getWrappedMap()) ? "成功" : "失败";
			Date endTime = new Date();
			long costTime = endTime.getTime() - startTime.getTime();
			System.out.println(DateUtil.getDateTime(startTime) + "：执行 [" + getClass().getName() + "] " + flag + " 共耗时" + costTime + "毫秒");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 运行计划任务（具体执行）
	 * 
	 */
	public abstract boolean doJob(Map<String, Object> map);

}

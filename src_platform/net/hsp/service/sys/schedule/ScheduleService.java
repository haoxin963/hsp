package net.hsp.service.sys.schedule;

import java.util.List;
import java.util.Map;

import net.hsp.common.Holder;
import net.hsp.entity.sys.schedule.Schedule;
import net.hsp.entity.sys.schedule.Task;
import net.hsp.service.BaseService;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

public interface ScheduleService extends BaseService {
	public Map<?, ?> findTask(FilterMap filterMap, PageInfo p);

	public List<Task> ListTask(String station);

	public Task getTask(String taskClass);

	public boolean addTask(String taskClass, Holder<String> error);

	public Schedule findScheduleById(Schedule oldSchedule);

	public boolean saveSchedule(Schedule schedule);

	public Map<?, ?> listSchedule(FilterMap filterMap, PageInfo p);

	public boolean setScheduleStatus(int scheduleId, int openFlag);
	
	public List<Schedule> listSchedules(boolean opened);
	
	public int deleteSchedule(int scheduleId);
	
	public int deleteTask(String taskClass);
}
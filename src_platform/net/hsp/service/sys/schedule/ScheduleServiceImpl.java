package net.hsp.service.sys.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.hsp.common.Holder;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.schedule.Schedule;
import net.hsp.entity.sys.schedule.Task;
import net.hsp.service.BaseServiceImpl;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
@ServiceLogUtil(name = "计划任务")
public class ScheduleServiceImpl extends BaseServiceImpl implements ScheduleService {

	@Override
	public Map<?, ?> findTask(FilterMap filterMap, PageInfo p) {
		String sql = "SELECT * FROM uisp_task_tbl WHERE 1=1";
		Map<String, String> map = filterMap.getFilter();
		Object[] args = null;
		if (map != null) {
			List<String> argList = new ArrayList<String>();
			// 任务名
			String taskName = map.get("taskName");
			if (null != taskName && !taskName.isEmpty()) {
				argList.add("%" + taskName + "%");

				sql += " AND taskName LIKE ?)";
			}
			// 站点
			String station = map.get("station");
			if (null != station && !station.isEmpty()) {
				argList.add("%" + station + "%");

				sql += " AND station LIKE ?)";
			}
			args = new Object[argList.size()];
			for (int i = 0; i < argList.size(); i++) {
				args[i] = argList.get(i);
			}
		}
		return this.getDAO(PlatFormConstant.BASESTATIONID).queryBySql2Map(sql, args, p.getRows() * (p.getPage() - 1), p.getRows());
	}

	@Override
	public List<Task> ListTask(String station) {
		String sql = "SELECT * FROM uisp_task_tbl WHERE station IS NULL OR station = ''";
		Object[] args = null;
		if (null != station && !station.isEmpty()) {
			sql += " OR station = ?";
			args = new Object[1];
			args[0] = station;
		}
		return this.getDAO(PlatFormConstant.BASESTATIONID).query(sql, args, new BeanPropertyRowMapper<Task>(Task.class));
	}

	private static final String TASK_PACKAGE = "net.hsp.service.task";

	@Override
	public boolean addTask(String taskClass, Holder<String> error) {
		boolean result = false;
		if (null != taskClass && !taskClass.isEmpty()) {
			int index = taskClass.indexOf(TASK_PACKAGE);
			if (index != 0) {
				taskClass = TASK_PACKAGE + "." + taskClass;
			}

			try {
				Class<?> clzz = Class.forName(taskClass);
				Task oldTask = this.getTask(taskClass);
				if (null != oldTask) {
					error.value = "类" + taskClass + "已被添加过";
				} else {
					if (clzz.isAnnotationPresent(net.hsp.common.schedule.Task.class)) {
						net.hsp.common.schedule.Task taskAnnotation = clzz.getAnnotation(net.hsp.common.schedule.Task.class);
						Task task = new Task();
						task.setTaskName(taskAnnotation.name());
						task.setTaskClass(taskClass);
						task.setStation(taskAnnotation.station());
						Date createDate = new Date();
						task.setCreateDate(createDate);
						this.getDAO(PlatFormConstant.BASESTATIONID).save2(task);
						result = true;
					} else {
						error.value = "类" + taskClass + "不是计划任务类";
					}
				}
			} catch (ClassNotFoundException e) {
				error.value = "类" + taskClass + "不存在";
				e.printStackTrace();
			} catch (Exception e) {
				error.value = "添加失败";
				e.printStackTrace();
			}

		} else {
			error.value = "类名不能为空";
		}
		return result;
	}

	@Override
	public Task getTask(String taskClass) {
		Task task = null;
		try {
			task = (Task) this.getDAO(PlatFormConstant.BASESTATIONID).findById(Task.class, taskClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}

	@Override
	public Map<?, ?> listSchedule(FilterMap filterMap, PageInfo p) {
		String sql = "SELECT s.*,t.taskName FROM uisp_schedule_tbl s , uisp_task_tbl t WHERE s.taskClass = t.taskClass";
		Map<String, String> map = filterMap.getFilter();
		Object[] args = null;
		if (map != null) {
			List<String> argList = new ArrayList<String>();
			// 任务名
			String taskName = map.get("taskName");
			if (null != taskName && !taskName.isEmpty()) {
				argList.add("%" + taskName + "%");

				sql += " AND t.taskName LIKE ?";
			}
			// 站点
			String station = map.get("station");
			if (null != station && !station.isEmpty()) {
				argList.add(station);
				sql += " AND s.station = ?";
			}
			args = new Object[argList.size()];
			for (int i = 0; i < argList.size(); i++) {
				args[i] = argList.get(i);
			}
		}
		return this.getDAO(PlatFormConstant.BASESTATIONID).queryBySql2Map(sql, args, p.getRows() * (p.getPage() - 1), p.getRows());
	}

	@Override
	public Schedule findScheduleById(Schedule oldSchedule) {
		Schedule schedule = null;
		try {
			System.out.println(oldSchedule.getScheduleId());
			schedule = (Schedule) this.getDAO(PlatFormConstant.BASESTATIONID).findById(oldSchedule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schedule;
	}

	@Override
	public boolean saveSchedule(Schedule schedule) {
		boolean result = false;
		try {
			Integer scheduleId = schedule.getScheduleId();
			if (null == scheduleId || scheduleId == 0) {
				schedule.setCreateDate(new Date());
				this.getDAO(PlatFormConstant.BASESTATIONID).save(schedule);
			} else {
				this.getDAO(PlatFormConstant.BASESTATIONID).update(schedule);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean setScheduleStatus(int scheduleId, int openFlag) {
		boolean result = false;
		try {
			Schedule schedule = new Schedule();
			schedule.setScheduleId(scheduleId);
			schedule.setOpenFlag(openFlag);
			result = true;
			this.getDAO(PlatFormConstant.BASESTATIONID).update2(schedule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Schedule> listSchedules(boolean opened) {
		int openFlag = opened ? 1 : 0;
		String sql = "SELECT * FROM uisp_schedule_tbl WHERE openFlag = ?";
		return this.getDAO(PlatFormConstant.BASESTATIONID).query(sql, new Object[] { openFlag }, new BeanPropertyRowMapper<Schedule>(Schedule.class));
	}
	
	public int deleteSchedule(int scheduleId) {
		Schedule schedule = new Schedule();
		schedule.setScheduleId(scheduleId);
		schedule = this.findScheduleById(schedule);
		if(schedule.getOpenFlag() == 1){//运行中
			return 0;
		} else {
			this.getDAO(PlatFormConstant.BASESTATIONID).delete(schedule);
			return 1;
		}
	}
	
	public int deleteTask(String taskClass){
		if(StringUtils.isBlank(taskClass)) {
			return 0;
		}
		String sql = "SELECT * FROM uisp_schedule_tbl WHERE taskClass = ?";
		List<Schedule> list= this.getDAO(PlatFormConstant.BASESTATIONID).query(sql, new Object[] { taskClass }, new BeanPropertyRowMapper<Schedule>(Schedule.class));
		if(list != null && list.size() > 0) {
			return 2;//存在schedule
		}
		String delsql = "delete from uisp_task_tbl where taskClass ='"+taskClass+"'";
		this.getDAO(PlatFormConstant.BASESTATIONID).execute(delsql);
		return 1;
	}
} 

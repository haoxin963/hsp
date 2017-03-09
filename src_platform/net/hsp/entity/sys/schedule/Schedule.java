package net.hsp.entity.sys.schedule;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.hsp.common.Comment;
import net.hsp.common.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "uisp_schedule_tbl")
@Comment(value = "计划任务")
public class Schedule implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "scheduleId", unique = true, nullable = false)
	private Integer scheduleId;

	/** 计划任务类 */
	@Column(name = "taskClass", length = 100)
	private String taskClass;

	// /** 计划任务名称 */
	// @Column(name = "name", length = 50)
	// private String name;

	/** 所属站点 */
	@Column(name = "station")
	private String station;

	/** 创建时间 */
	@Column(name = "createDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	/** 开启标志 */
	@Column(name = "openFlag")
	private Integer openFlag;

	/** 触发器 */
	@Column(name = "triggerType")
	private String triggerType;

	/** 开始时间 */
	@Column(name = "startTime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/** 时间间隔 */
	@Column(name = "repeatInterval")
	private Long repeatInterval;

	/** 执行次数 */
	@Column(name = "repeatCount")
	private Integer repeatCount;

	/** 表达式 */
	@Column(name = "cronExpression")
	private String cronExpression;

	/** 描述 */
	@Column(name = "description")
	private String description;

	/** 秒 */
	@Column(name = "seconds")
	private String seconds;

	/** 分 */
	@Column(name = "minutes")
	private String minutes;

	/** 时 */
	@Column(name = "hours")
	private String hours;

	/** 日 */
	@Column(name = "days")
	private String days;

	/** 周 */
	@Column(name = "weeks")
	private String weeks;

	/** 月 */
	@Column(name = "months")
	private String months;

	/** 年 */
	@Column(name = "years")
	private String years;

	@Column(name = "fromSecond")
	private Integer fromSecond;
	@Column(name = "toSecond")
	private Integer toSecond;
	@Column(name = "intervalSecond")
	private Integer intervalSecond;
	@Column(name = "fromMinute")
	private Integer fromMinute;
	@Column(name = "toMinute")
	private Integer toMinute;
	@Column(name = "intervalMinute")
	private Integer intervalMinute;
	@Column(name = "fromHour")
	private Integer fromHour;
	@Column(name = "toHour")
	private Integer toHour;
	@Column(name = "intervalHour")
	private Integer intervalHour;
	@Column(name = "fromDay")
	private Integer fromDay;
	@Column(name = "toDay")
	private Integer toDay;
	@Column(name = "intervalDay")
	private Integer intervalDay;

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(Integer openFlag) {
		this.openFlag = openFlag;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeconds() {
		return seconds;
	}

	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}
	@Transient
	private List<String> allSeconds = new ArrayList<String>() {
		{
			for (int i = 0; i < 60; i++) {
				add(i, i + "");
			}
		}
	};

	public List<String> getAllSeconds() {
		return allSeconds;
	}
	@Transient
	private List<String> allMinutes = new ArrayList<String>() {
		{
			for (int i = 0; i < 60; i++) {
				add(i, i + "");
			}
		}
	};

	public List<String> getAllMinutes() {
		return allMinutes;
	}
	@Transient
	private List<String> allHours = new ArrayList<String>() {
		{
			for (int i = 0; i < 24; i++) {
				add(i, i + "");
			}
		}
	};

	public List<String> getAllHours() {
		return allHours;
	}

	@Transient
	private List<String> allDays = new ArrayList<String>() {
		{
			for (int i = 0; i < 31; i++) {
				add(i, i + 1 + "");
			}
		}
	};

	public List<String> getAllDays() {
		return allDays;
	}
	@Transient
	private List<String> allWeeks = new ArrayList<String>() {
		{
			for (int i = 0; i < 7; i++) {
				add(i, i + 1 + "");
			}
		}
	};

	public List<String> getAllWeeks() {
		return allWeeks;
	}

	@Transient
	private List<String> allMonths = new ArrayList<String>() {
		{
			for (int i = 0; i < 12; i++) {
				add(i, i + 1 + "");
			}
		}
	};

	public List<String> getAllMonths() {
		return allMonths;
	}

	@Transient
	private List<String> allYears = new ArrayList<String>() {
		{
			int year = DateUtil.getYear(new Date());
			for (int i = 0; i < 10; i++) {
				add(i, year + i + "");
			}
		}
	};

	public List<String> getAllYears() {
		return allYears;
	}

	public void setCheckedSeconds(String[] checkedSeconds) {
		if (null != checkedSeconds && checkedSeconds.length > 0) {

			seconds = StringUtils.join(Arrays.asList(checkedSeconds), ",");
		}
	}

	public void setCheckedMinutes(String[] checkedMinutes) {
		if (null != checkedMinutes && checkedMinutes.length > 0) {
			minutes = StringUtils.join(Arrays.asList(checkedMinutes), ",");
		}
	}

	public void setCheckedHours(String[] checkedHours) {
		if (null != checkedHours && checkedHours.length > 0) {
			hours = StringUtils.join(Arrays.asList(checkedHours), ",");
		}
	}

	public void setCheckedDays(String[] checkedDays) {
		if (null != checkedDays && checkedDays.length > 0) {
			days = StringUtils.join(Arrays.asList(checkedDays), ",");
		}
	}

	public void setCheckedWeeks(String[] checkedWeeks) {
		if (null != checkedWeeks && checkedWeeks.length > 0) {
			weeks = StringUtils.join(Arrays.asList(checkedWeeks), ",");
		}
	}

	public void setCheckedMonths(String[] checkedMonths) {
		if (null != checkedMonths && checkedMonths.length > 0) {
			months = StringUtils.join(Arrays.asList(checkedMonths), ",");
		}
	}

	public void setCheckedYears(String[] checkedYears) {
		if (null != checkedYears && checkedYears.length > 0) {
			years = StringUtils.join(Arrays.asList(checkedYears), ",");
		}
	}

	public String[] getCheckedSeconds() {
		return null == seconds ? null : seconds.split(",");
	}

	public String[] getCheckedMinutes() {
		return null == minutes ? null : minutes.split(",");
	}

	public String[] getCheckedHours() {
		return null == hours ? null : hours.split(",");
	}

	public String[] getCheckedDays() {
		return null == days ? null : days.split(",");
	}

	public String[] getCheckedWeeks() {
		return null == weeks ? null : weeks.split(",");
	}

	public String[] getCheckedMonths() {
		return null == months ? null : months.split(",");
	}

	public String[] getCheckedYears() {
		return null == years ? null : years.split(",");
	}

	public Integer getFromSecond() {
		return fromSecond;
	}

	public void setFromSecond(Integer fromSecond) {
		this.fromSecond = fromSecond;
	}

	public Integer getToSecond() {
		return toSecond;
	}

	public void setToSecond(Integer toSecond) {
		this.toSecond = toSecond;
	}

	public Integer getIntervalSecond() {
		return intervalSecond;
	}

	public void setIntervalSecond(Integer intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public Integer getFromMinute() {
		return fromMinute;
	}

	public void setFromMinute(Integer fromMinute) {
		this.fromMinute = fromMinute;
	}

	public Integer getToMinute() {
		return toMinute;
	}

	public void setToMinute(Integer toMinute) {
		this.toMinute = toMinute;
	}

	public Integer getIntervalMinute() {
		return intervalMinute;
	}

	public void setIntervalMinute(Integer intervalMinute) {
		this.intervalMinute = intervalMinute;
	}

	public Integer getFromHour() {
		return fromHour;
	}

	public void setFromHour(Integer fromHour) {
		this.fromHour = fromHour;
	}

	public Integer getToHour() {
		return toHour;
	}

	public void setToHour(Integer toHour) {
		this.toHour = toHour;
	}

	public Integer getFromDay() {
		return fromDay;
	}

	public void setFromDay(Integer fromDay) {
		this.fromDay = fromDay;
	}

	public Integer getToDay() {
		return toDay;
	}

	public void setToDay(Integer toDay) {
		this.toDay = toDay;
	}

	public Integer getIntervalHour() {
		return intervalHour;
	}

	public void setIntervalHour(Integer intervalHour) {
		this.intervalHour = intervalHour;
	}

	public Integer getIntervalDay() {
		return intervalDay;
	}

	public void setIntervalDay(Integer intervalDay) {
		this.intervalDay = intervalDay;
	}

	public void setAllSeconds(List<String> allSeconds) {
		this.allSeconds = allSeconds;
	}

	public void setAllMinutes(List<String> allMinutes) {
		this.allMinutes = allMinutes;
	}

	public void setAllHours(List<String> allHours) {
		this.allHours = allHours;
	}

	public void setAllDays(List<String> allDays) {
		this.allDays = allDays;
	}

	public void setAllWeeks(List<String> allWeeks) {
		this.allWeeks = allWeeks;
	}

	public void setAllMonths(List<String> allMonths) {
		this.allMonths = allMonths;
	}

	public void setAllYears(List<String> allYears) {
		this.allYears = allYears;
	}

}

package net.hsp.entity.sys.schedule;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "uisp_task_tbl")
@Comment(value = "计划任务")
public class Task implements Serializable {

	/** 计划任务类 */
	@Column(name = "taskClass", length = 100, unique = true, nullable = true)
	@Id
	private String taskClass;

	/** 计划任务名称 */
	@Column(name = "taskName", length = 50)
	private String taskName;

	/** 所属站点 */
	@Column(name = "station")
	private String station;

	/** 创建时间 */
	@Column(name = "createDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	public String getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
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

}

package net.hsp.entity.sys.monitor.log;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Comment(value = "用户操作日志")
@Table(name = "pubmodule_actionlog_tbl")
public class UserActionLog implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operateTime", nullable = false, length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-dd")
	private Date operateTime;

	@Comment(value = "ip")
	@Column(name = "ip", length = 20)
	private String ip;

	@Comment(value = "操作人")
	@Column(name = "name", length = 40)
	private String name;
	
	@Comment(value = "耗时毫秒")
	@Column(name = "ms", length = 10)
	private String ms;


	@Comment(value = "操作系统及版本")
	@Column(name = "action", length = 100)
	private String action;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

}

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
@Comment(value = "登录日志")
@Table(name = "pubmodule_userlog_tbl")
public class UserLog implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "域")
	@Column(name = "domain",length = 16)
	private String domain;
	
	@Comment(value = "备注")
	@Column(name = "description", length = 200)
	private String description;

	@Comment(value = "登录类型")
	@Column(name = "logType", nullable = false, length = 1)
	private String logType;

	@Comment(value = "登录时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operateTime", nullable = false, length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date operateTime;

	@Comment(value = "登录ip")
	@Column(name = "ip", length = 20)
	private String ip;
	
	@Comment(value = "登录人")
	@Column(name = "userId", length = 20)
	private String userId;
	
	@Comment(value = "操作系统及版本")
	@Column(name = "osVersion", length = 20)
	private String osVersion;
	
	@Comment(value = "终端/浏览器类型及版本")
	@Column(name = "agentVersion", length = 20)
	private String agentVersion;
	
	

	public UserLog() {
	}

	public UserLog(String logType, Date operateTime) {
		this.logType = logType;
		this.operateTime = operateTime;
	}

	public UserLog(String description, String logType, Date operateTime,
			String userId) {
		this.description = description;
		this.logType = logType;
		this.operateTime = operateTime;
		this.userId = userId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** get 备注 */
	public String getDescription() {
		return this.description;
	}

	/** set 备注 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** get 登录类型 */
	public String getLogType() {
		return this.logType;
	}

	/** set 登录类型 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	/** get 登录时间 */
	public Date getOperateTime() {
		return this.operateTime;
	}

	/** set 登录时间 */
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	/** get 登录人 */
	public String getUserId() {
		return this.userId;
	}

	/** set 登录人 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}

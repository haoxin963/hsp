package net.hsp.entity.sys.notice;

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
@Comment(value = "pubmodule_noticeuser_tbl")
@Table(name = "pubmodule_noticeuser_tbl")
public class NoticeUser implements java.io.Serializable {

	@Comment(value = "自增长ID")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "信息ID")
	@Column(name = "noticeId")
	private Integer noticeId;

	@Comment(value = "用户")
	@Column(name = "userId")
	private Integer userId;

	@Comment(value = "阅读回执")
	@Column(name = "remark", length = 100)
	private String remark;

	@Comment(value = "阅读状态")
	@Column(name = "status")
	private Integer status;

	@Comment(value = "更新时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;

	public NoticeUser() {
	}

	public NoticeUser(Integer noticeId, Integer userId, String remark,
			Integer status, Date updateTime) {
		this.noticeId = noticeId;
		this.userId = userId;
		this.remark = remark;
		this.status = status;
		this.updateTime = updateTime;
	}

	/** get 自增长ID*/
	public Integer getId() {
		return this.id;
	}

	/** set 自增长ID*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 信息ID*/
	public Integer getNoticeId() {
		return this.noticeId;
	}

	/** set 信息ID*/
	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	/** get 用户*/
	public Integer getUserId() {
		return this.userId;
	}

	/** set 用户*/
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/** get 阅读回执*/
	public String getRemark() {
		return this.remark;
	}

	/** set 阅读回执*/
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** get 阅读状态*/
	public Integer getStatus() {
		return this.status;
	}

	/** set 阅读状态*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** get 更新时间*/
	public Date getUpdateTime() {
		return this.updateTime;
	}

	/** set 更新时间*/
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

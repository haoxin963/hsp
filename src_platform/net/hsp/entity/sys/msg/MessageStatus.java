package net.hsp.entity.sys.msg;

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
@Comment(value = "pubmodule_message_status_tbl")
@Table(name = "pubmodule_message_status_tbl")
public class MessageStatus implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "用户ID")
	@Column(name = "userid", nullable = false)
	private long userid;

	@Comment(value = "消息ID")
	@Column(name = "messageid", nullable = false)
	private long messageid;

	@Comment(value = "阅读状态[0删除 1未读 2已读]")
	@Column(name = "status")
	private Integer status;

	@Comment(value = "阅读时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "readtime", nullable = false, length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date readtime;

	public MessageStatus() {
	}

	public MessageStatus(long userid, long messageid, Date readtime) {
		this.userid = userid;
		this.messageid = messageid;
		this.readtime = readtime;
	}

	public MessageStatus(long userid, long messageid, Integer status, Date readtime) {
		this.userid = userid;
		this.messageid = messageid;
		this.status = status;
		this.readtime = readtime;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** get 用户ID */
	public long getUserid() {
		return this.userid;
	}

	/** set 用户ID */
	public void setUserid(long userid) {
		this.userid = userid;
	}

	/** get 消息ID */
	public long getMessageid() {
		return this.messageid;
	}

	/** set 消息ID */
	public void setMessageid(long messageid) {
		this.messageid = messageid;
	}

	/** get 读阅状态 */
	public Integer getStatus() {
		return this.status;
	}

	/** set 读阅状态 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** get 阅读时间 */
	public Date getReadtime() {
		return this.readtime;
	}

	/** set 阅读时间 */
	public void setReadtime(Date readtime) {
		this.readtime = readtime;
	}

}

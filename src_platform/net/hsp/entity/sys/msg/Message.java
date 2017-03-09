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
@Comment(value = "pubmodule_message_tbl")
@Table(name = "pubmodule_message_tbl")
public class Message implements java.io.Serializable {

	@Comment(value = "ID")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "消息标题")
	@Column(name = "title")
	private String title;

	@Comment(value = "消息内容")
	@Column(name = "message", nullable = false)
	private String message;

	@Comment(value = "发送时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sendtime", nullable = false, length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sendtime;

	@Comment(value = "引用地址")
	@Column(name = "href")
	private String href;

	@Comment(value = "消息类型")
	@Column(name = "type")
	private Integer type;

	public Message() {
	}

	public Message(String message, Date sendtime) {
		this.message = message;
		this.sendtime = sendtime;
	}

	public Message(String title, String message, Date sendtime, String href, Integer type) {
		this.title = title;
		this.message = message;
		this.sendtime = sendtime;
		this.href = href;
		this.type = type;
	}

	/** get ID */
	public Long getId() {
		return this.id;
	}

	/** set ID */
	public void setId(Long id) {
		this.id = id;
	}

	/** get 息消标题 */
	public String getTitle() {
		return this.title;
	}

	/** set 息消标题 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** get 消息内容 */
	public String getMessage() {
		return this.message;
	}

	/** set 消息内容 */
	public void setMessage(String message) {
		this.message = message;
	}

	/** get 发送时间 */
	public Date getSendtime() {
		return this.sendtime;
	}

	/** set 发送时间 */
	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	/** get 引用地址 */
	public String getHref() {
		return this.href;
	}

	/** set 引用地址 */
	public void setHref(String href) {
		this.href = href;
	}

	/** get 消息类型 */
	public Integer getType() {
		return this.type;
	}

	/** set 消息类型 */
	public void setType(Integer type) {
		this.type = type;
	}

}

package net.hsp.entity.sys.notice;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import net.hsp.common.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "pubmodule_noticecomment_tbl")
@Table(name = "pubmodule_noticecomment_tbl")
public class NoticeComment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "评论类别")
	@Column(name = "type", nullable = false, length = 30)
	private String type;

	@Comment(value = "关联ID")
	@Column(name = "noticeId", nullable = false)
	private int noticeId;
	
	@Comment(value = "用户ID")
	@Column(name = "userId", nullable = false)
	private int userId;
	
	@Comment(value = "评论时间")
	@Column(name = "dateTime", nullable = false)
	private Date dateTime;

	@Comment(value = "评论内容")
	@Column(name = "content", nullable = false, length = 200)
	private String content;

	@Column(name = "delTag", length = 1)
	private String delTag;

	public NoticeComment() {
	}

	public NoticeComment(int id, String type, int noticeId, String content) {
		this.id = id;
		this.type = type;
		this.noticeId = noticeId;
		this.content = content;
	}

	public NoticeComment(int id, String type, int noticeId, String content,
			String delTag) {
		this.id = id;
		this.type = type;
		this.noticeId = noticeId;
		this.content = content;
		this.delTag = delTag;
	}

	/** get 自增长ID */
	public Integer getId() {
		return this.id;
	}

	/** set 自增长ID */
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 评论类别 */
	public String getType() {
		return this.type;
	}

	/** set 评论类别 */
	public void setType(String type) {
		this.type = type;
	}

	/** get 关联ID */
	public int getNoticeId() {
		return this.noticeId;
	}

	/** set 关联ID */
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}

	/** get 评论内容 */
	public String getContent() {
		return this.content;
	}

	/** set 评论内容 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getDelTag() {
		return this.delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}

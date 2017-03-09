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
@Comment(value = "pubmodule_notice_tbl")
@Table(name = "pubmodule_notice_tbl")
public class Notice implements java.io.Serializable {

	@Comment(value = "ID")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "信息类型")
	@Column(name = "categoryId", nullable = false)
	private int categoryId;

	@Comment(value = "信息标题")
	@Column(name = "title", nullable = false, length = 200)
	private String title;
	
	@Comment(value = "重要标识")
	@Column(name = "ismajor", nullable = false)
	private int ismajor;
	
	@Comment(value = "评论标识")
	@Column(name = "iscomment", nullable = false)
	private int iscomment;

	@Comment(value = "回执标识")
	@Column(name = "isback", nullable = false)
	private int isback;
	
	@Comment(value = "信息内容")
	@Column(name = "content")
	private String content;

	@Comment(value = "附件关键字")
	@Column(name = "attachpath", length = 50)
	private String attachpath;

	@Comment(value = "阅读统计")
	@Column(name = "hits")
	private Integer hits;

	@Comment(value = "创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTime;

	@Comment(value = "创建人")
	@Column(name = "createUserId")
	private Integer createUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "releaseTime", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date releaseTime;

	@Column(name = "releaseUserId")
	private Integer releaseUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;

	@Column(name = "updateUserId")
	private Integer updateUserId;

	@Comment(value = "是否全部人员")
	@Column(name = "alluser")
	private Integer alluser;

	@Comment(value = "信息状态")
	@Column(name = "status")
	private Integer status;

	@Comment(value = "定时发布")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "taskopen", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date taskopen;

	@Comment(value = "定时关闭")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "taskclose", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date taskclose;

	@Comment(value = "排序值")
	@Column(name = "sortNo", nullable = false)
	private int sortNo;

	@Comment(value = "删除标记")
	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	public Notice() {
	}

	public Notice(int categoryId, String title, int sortNo, String delTag) {
		this.categoryId = categoryId;
		this.title = title;
		this.sortNo = sortNo;
		this.delTag = delTag;
	}

	public Notice(int categoryId, String title, String content,
			String attachpath, Integer hits, Date createTime,
			Integer createUserId, Date releaseTime, Integer releaseUserId,
			Integer alluser, Integer status, Date taskopen, Date taskclose,
			int sortNo, String delTag) {
		this.categoryId = categoryId;
		this.title = title;
		this.content = content;
		this.attachpath = attachpath;
		this.hits = hits;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.releaseTime = releaseTime;
		this.releaseUserId = releaseUserId;
		this.alluser = alluser;
		this.status = status;
		this.taskopen = taskopen;
		this.taskclose = taskclose;
		this.sortNo = sortNo;
		this.delTag = delTag;
	}

	/** get ID*/
	public Integer getId() {
		return this.id;
	}

	/** set ID*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 信息类型*/
	public int getCategoryId() {
		return this.categoryId;
	}

	/** set 信息类型*/
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/** get 信息标题*/
	public String getTitle() {
		return this.title;
	}

	/** set 信息标题*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** get 信息内容*/
	public String getContent() {
		return this.content;
	}

	/** set 信息内容*/
	public void setContent(String content) {
		this.content = content;
	}

	/** get 附件关键字*/
	public String getAttachpath() {
		return this.attachpath;
	}

	/** set 附件关键字*/
	public void setAttachpath(String attachpath) {
		this.attachpath = attachpath;
	}

	/** get 阅读统计*/
	public Integer getHits() {
		return this.hits;
	}

	/** set 阅读统计*/
	public void setHits(Integer hits) {
		this.hits = hits;
	}

	/** get 创建时间*/
	public Date getCreateTime() {
		return this.createTime;
	}

	/** set 创建时间*/
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/** get 创建人*/
	public Integer getCreateUserId() {
		return this.createUserId;
	}

	/** set 创建人*/
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Date getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getReleaseUserId() {
		return this.releaseUserId;
	}

	public void setReleaseUserId(Integer releaseUserId) {
		this.releaseUserId = releaseUserId;
	}

	/** get 是否全部人员*/
	public Integer getAlluser() {
		return this.alluser;
	}

	/** set 是否全部人员*/
	public void setAlluser(Integer alluser) {
		this.alluser = alluser;
	}

	/** get 信息状态*/
	public Integer getStatus() {
		return this.status;
	}

	/** set 信息状态*/
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** get 定时发布*/
	public Date getTaskopen() {
		return this.taskopen;
	}

	/** set 定时发布*/
	public void setTaskopen(Date taskopen) {
		this.taskopen = taskopen;
	}

	/** get 定时关闭*/
	public Date getTaskclose() {
		return this.taskclose;
	}

	/** set 定时关闭*/
	public void setTaskclose(Date taskclose) {
		this.taskclose = taskclose;
	}

	/** get 排序值*/
	public int getSortNo() {
		return this.sortNo;
	}

	/** set 排序值*/
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	/** get 删除标记*/
	public String getDelTag() {
		return this.delTag;
	}

	/** set 删除标记*/
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public int getIsmajor() {
		return ismajor;
	}

	public void setIsmajor(int ismajor) {
		this.ismajor = ismajor;
	}

	public int getIscomment() {
		return iscomment;
	}

	public void setIscomment(int iscomment) {
		this.iscomment = iscomment;
	}

	public int getIsback() {
		return isback;
	}

	public void setIsback(int isback) {
		this.isback = isback;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

}

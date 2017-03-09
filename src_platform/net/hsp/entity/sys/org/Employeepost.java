package net.hsp.entity.sys.org;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "员工岗位信息")
@Table(name = "pubmodule_employeepost_tbl")
public class Employeepost implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "工员ID")
	@Column(name = "empid", nullable = false)
	private Long empid;

	@Comment(value = "岗位ID")
	@Column(name = "postId", nullable = false)
	private int postId;

	@Comment(value = "是否主要岗位")
	@Column(name = "isMainPost", nullable = false, length = 1)
	private String isMainPost;

	@Comment(value = "删除标记")
	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	public Employeepost() {
	}

	public Employeepost(long empid, int postId, String isMainPost, String delTag) {
		this.empid = empid;
		this.postId = postId;
		this.isMainPost = isMainPost;
		this.delTag = delTag;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/** get 工员ID*/
	public Long getEmpid() {
		return this.empid;
	}

	/** set 工员ID*/
	public void setEmpid(long empid) {
		this.empid = empid;
	}

	/** get 岗位ID*/
	public int getPostId() {
		return this.postId;
	}

	/** set 岗位ID*/
	public void setPostId(int postId) {
		this.postId = postId;
	}

	/** get 是否主要岗位*/
	public String getIsMainPost() {
		return this.isMainPost;
	}

	/** set 是否主要岗位*/
	public void setIsMainPost(String isMainPost) {
		this.isMainPost = isMainPost;
	}

	/** get 删除标记*/
	public String getDelTag() {
		return this.delTag;
	}

	/** set 删除标记*/
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

}

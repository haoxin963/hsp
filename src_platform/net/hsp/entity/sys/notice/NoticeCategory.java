package net.hsp.entity.sys.notice;

import net.hsp.common.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "pubmodule_noticecategory_tbl")
@Table(name = "pubmodule_noticecategory_tbl")
public class NoticeCategory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Comment(value = "自增长ID")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "编号")
	@Column(name = "code", nullable = false, length = 20)
	private String code;
	
	@Comment(value = "类型名称")
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Comment(value = "备注")
	@Column(name = "remark", nullable = false, length = 50)
	private String remark;
	
	@Comment(value = "父节点ID")
	@Column(name = "parentId", nullable = false)
	private int parentId;

	@Comment(value = "排序")
	@Column(name = "sortNo", nullable = false)
	private int sortNo;

	@Column(name = "delTag", length = 1)
	private String delTag;

	public NoticeCategory() {
	}

	public NoticeCategory(String name, int parentId, int sortNo) {
		this.name = name;
		this.parentId = parentId;
		this.sortNo = sortNo;
	}

	public NoticeCategory(String name, int parentId, int sortNo, String delTag) {
		this.name = name;
		this.parentId = parentId;
		this.sortNo = sortNo;
		this.delTag = delTag;
	}

	/** get 自增长ID*/
	public Integer getId() {
		return this.id;
	}

	/** set 自增长ID*/
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 类型名称*/
	public String getName() {
		return this.name;
	}

	/** set 类型名称*/
	public void setName(String name) {
		this.name = name;
	}

	/** get 父节点ID*/
	public int getParentId() {
		return this.parentId;
	}

	/** set 父节点ID*/
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/** get 排序*/
	public int getSortNo() {
		return this.sortNo;
	}

	/** set 排序*/
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getDelTag() {
		return this.delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

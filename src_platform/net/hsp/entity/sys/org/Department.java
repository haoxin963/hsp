package net.hsp.entity.sys.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "部门")
@Table(name = "pubmodule_department_tbl")
public class Department implements java.io.Serializable {

	@Id 
	@Column(name = "departmentId", unique = true, nullable = false)
	private String departmentId;

	@Comment(value = "名称")
	@Column(name = "departmentName", nullable = false, length = 64)
	private String departmentName;

	@Comment(value = "部门编号")
	@Column(name = "departmentNumber", nullable = false, length = 16)
	private String departmentNumber;

	@Comment(value = "父节点")
	@Column(name = "parent_id", nullable = false)
	private String parentId;

	@Comment(value = "排序")
	@Column(name = "sortNo", nullable = false)
	private int sortNo;

	@Comment(value = "描述")
	@Column(name = "departmentIntroduction")
	private String departmentIntroduction;

	@Comment(value = "是否有子节点")
	@Column(name = "child", nullable = false, length = 1)
	private String child;
	

	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;
	
	@Comment(value = "简称")
	@Column(name = "shortName", length = 50)
	private String shortName;
	
	@Comment(value = "域")
	@Column(name = "domain", nullable = false, length = 20)
	private String domain;

	
	@Comment(value = "是否显示在组织机构")
	@Column(name = "isdisplay", nullable = false, length = 1)
	private String isdisplay;
	

	public Department() {
	}

 

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String id) {
		this.departmentId = id;
	}

	/** get 名称 */
	public String getDepartmentName() {
		return this.departmentName;
	}

	/** set 名称 */
	public void setDepartmentName(String name) {
		this.departmentName = name;
	}

	/** get 简称 */
	public String getShortName() {
		return this.shortName;
	}

	/** set 简称 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
 

	/** get 父节点 */
	public String getParentId() {
		return this.parentId;
	}

	/** set 父节点 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/** get 排序 */
	public int getSortNo() {
		return this.sortNo;
	}

	/** set 排序 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

 

	/** get 域 */
	public String getDomain() {
		return this.domain;
	}

	/** set 域 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDelTag() {
		return this.delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getIsdisplay() {
		return isdisplay;
	}

	public void setIsdisplay(String isdisplay) {
		this.isdisplay = isdisplay;
	}




	public String getDepartmentNumber() {
		return departmentNumber;
	}




	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}




	public String getDepartmentIntroduction() {
		return departmentIntroduction;
	}




	public void setDepartmentIntroduction(String departmentIntroduction) {
		this.departmentIntroduction = departmentIntroduction;
	}




	public String getChild() {
		return child;
	}




	public void setChild(String child) {
		this.child = child;
	}

	
	
}
 
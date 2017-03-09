package net.hsp.entity.sys.org;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "岗位信息")
@Table(name = "pubmodule_post_tbl")
public class Post implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "岗位代号")
	@Column(name = "postNo", nullable = false, length = 50)
	private String postNo;

	@Comment(value = "所属部门")
	@Column(name = "deptId", nullable = false)
	private String deptId;

	@Comment(value = "上级岗位")
	@Column(name = "parentId", nullable = false)
	private int parentId;

	@Comment(value = "岗位名称")
	@Column(name = "postName", nullable = false)
	private String postName;

	@Comment(value = "岗位简称")
	@Column(name = "shortName")
	private String shortName;

	@Comment(value = "顺序号")
	@Column(name = "sortNo")
	private Integer sortNo;

	@Comment(value = "否是部门负责人")
	@Column(name = "isDeptHead", length = 1)
	private String isDeptHead;

	@Comment(value = "是否有子节点")
	@Column(name = "hasChild", length = 1)
	private String hasChild;
	
	@Comment(value = "职位")
	@Column(name = "positionId")
	private Integer positionId;

	@Comment(value = "备注")
	@Column(name = "remark")
	private String remark;

	@Comment(value = "除删标记")
	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	public Post() {
	}
  
	/** get 主键 */
	public Integer getId() {
		return this.id;
	}

	/** set 主键 */
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 岗位代号 */
	public String getPostNo() {
		return this.postNo;
	}

	/** set 岗位代号 */
	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}

	/** get 所属部门 */
	public String getDeptId() {
		return this.deptId;
	}

	/** set 所属部门 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** get 上级岗位 */
	public int getParentId() {
		return this.parentId;
	}

	/** set 上级岗位 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/** get 岗位名称 */
	public String getPostName() {
		return this.postName;
	}

	/** set 岗位名称 */
	public void setPostName(String postName) {
		this.postName = postName;
	}

	/** get 岗位简称 */
	public String getShortName() {
		return this.shortName;
	}

	/** set 岗位简称 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/** get 顺序号 */
	public Integer getSortNo() {
		return this.sortNo;
	}

	/** set 顺序号 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/** get 否是部门负责人 */
	public String getIsDeptHead() {
		return this.isDeptHead;
	}

	/** set 否是部门负责人 */
	public void setIsDeptHead(String isDeptHead) {
		this.isDeptHead = isDeptHead;
	}
		
	/** get 是否有子节点 */
	public String getHasChild() {
		return hasChild;
	}

	/** set 是否有子节点 */
	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	/** get 职位 */
	public Integer getPositionId() {
		return this.positionId;
	}

	/** set 职位 */
	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	/** get 备注 */
	public String getRemark() {
		return this.remark;
	}

	/** set 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** get 除删标记 */
	public String getDelTag() {
		return this.delTag;
	}

	/** set 除删标记 */
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

}

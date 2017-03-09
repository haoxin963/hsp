package net.hsp.entity.sys.basedata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "区域")
@Table(name = "pubmodule_area_tbl")
public class Area implements java.io.Serializable {

	@Id
	@Column(name = "areaId", unique = true, nullable = false, length = 48)
	private String areaId;

	@Comment(value = "区域名称")
	@Column(name = "areaName", nullable = false, length = 100)
	private String areaName;

	@Column(name = "delTag", length = 1)
	private String delTag;

	@Column(name = "hasChild", length = 1)
	private String hasChild;

	@Column(name = "parentId", nullable = false, length = 48)
	private String parentId;

	@Column(name = "sortNo", nullable = false)
	private int sortNo;

	public Area() {
	}

	public Area(String areaId, String areaName, String parentId, int sortNo) {
		this.areaId = areaId;
		this.areaName = areaName;
		this.parentId = parentId;
		this.sortNo = sortNo;
	}

	public Area(String areaId, String areaName, String delTag, String hasChild,
			String parentId, int sortNo) {
		this.areaId = areaId;
		this.areaName = areaName;
		this.delTag = delTag;
		this.hasChild = hasChild;
		this.parentId = parentId;
		this.sortNo = sortNo;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	/** get 区域名称 */
	public String getAreaName() {
		return this.areaName;
	}

	/** set 区域名称 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDelTag() {
		return this.delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getHasChild() {
		return this.hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

}

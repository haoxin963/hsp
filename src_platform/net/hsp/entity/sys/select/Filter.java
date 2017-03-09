package net.hsp.entity.sys.select;

import net.hsp.common.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "pubmodule_selectfilter_tbl")
@Table(name = "pubmodule_selectfilter_tbl")
public class Filter implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "filterName", nullable = false, length = 30)
	private String filterName;

	@Column(name = "filterDesc", length = 200)
	private String filterDesc;

	@Column(name = "delTag", nullable = false)
	private int delTag;

	public Filter() {
	}

	public Filter(String filterName, int delTag) {
		this.filterName = filterName;
		this.delTag = delTag;
	}

	public Filter(String filterName, String desc, int delTag) {
		this.filterName = filterName;
		this.filterDesc = desc;
		this.delTag = delTag;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFilterName() {
		return this.filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterDesc() {
		return this.filterDesc;
	}

	public void setFilterDesc(String filterDesc) {
		this.filterDesc = filterDesc;
	}

	public int getDelTag() {
		return this.delTag;
	}

	public void setDelTag(int delTag) {
		this.delTag = delTag;
	}

}

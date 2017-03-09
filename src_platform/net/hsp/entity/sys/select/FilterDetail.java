package net.hsp.entity.sys.select;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "pubmodule_selectfilterdetail_tbl")
@Table(name = "pubmodule_selectfilterdetail_tbl")
public class FilterDetail implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "filterId", nullable = false)
	private int filterId;

	@Column(name = "filterType", nullable = false, length = 20)
	private String filterType;

	@Column(name = "filterValue", nullable = false, length = 50)
	private String filterValue;

	public FilterDetail() {
	}

	public FilterDetail(int filterId, String filterType, String filterValue) {
		this.filterId = filterId;
		this.filterType = filterType;
		this.filterValue = filterValue;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getFilterId() {
		return this.filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	public String getFilterType() {
		return this.filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterValue() {
		return this.filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

}

package net.hsp.entity.sys.manage;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Comment(value = "站点")
@Table(name = "uisp_station_tbl")
public class Station implements java.io.Serializable {

	@Id
	@Column(name = "statId", unique = true, nullable = false, length = 48)
	private String statId;

	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	@Comment(value = "域名")
	@Column(name = "domainAddress", length = 100)
	private String domainAddress;

	@Comment(value = "语言")
	@Column(name = "language", length = 5)
	private String language;

	@Comment(value = "名称")
	@Column(name = "statName", length = 50)
	private String statName;

	@Comment(value = "状态")
	@Column(name = "status", nullable = false, length = 1)
	private String status;
	
	@Comment(value = "文件系统")
	@Column(name = "fileSystemPath", nullable = false, length = 100)
	private String fileSystemPath;

	@Comment(value = "{fk:'net.hsp.entity.sys.manage.Customer'}")
	@Column(name = "custId")
	private Long custId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createDate", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;

	public Station() {
	}

	public String getStatId() {
		return statId;
	}

	public void setStatId(String statId) {
		this.statId = statId;
	}

	public String getDelTag() {
		return delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getDomainAddress() {
		return domainAddress;
	}

	public void setDomainAddress(String domainAddress) {
		this.domainAddress = domainAddress;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFileSystemPath() {
		return fileSystemPath;
	}

	public void setFileSystemPath(String fileSystemPath) {
		this.fileSystemPath = fileSystemPath;
	}

}

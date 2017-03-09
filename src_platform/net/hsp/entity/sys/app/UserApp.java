package net.hsp.entity.sys.app;

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
@Comment(value = "用户手机端关联")
@Table(name = "pubmodule_user_app_tbl")
public class UserApp implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "用户ID")
	@Column(name = "userid", nullable = false)
	private long userid;

	@Comment(value = "客户端ID")
	@Column(name = "clientid", nullable = false, length = 500)
	private String clientid;

	@Comment(value = "状态")
	@Column(name = "status", length = 20)
	private String status;

	@Comment(value = "站点")
	@Column(name = "custid", length = 200)
	private String custid;

	@Comment(value = "标志")
	@Column(name = "tag", length = 20)
	private String tag;

	@Comment(value = "创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdate;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "userid", nullable = false)
	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	@Column(name = "clientId", nullable = false, length = 500)
	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	@Column(name = "status", length = 20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "custid", length = 200)
	public String getCustid() {
		return this.custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	@Column(name = "tag", length = 20)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", length = 19)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

}

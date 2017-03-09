package net.hsp.entity.sys.manage;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "客户")
@Table(name = "uisp_customer_tbl")
public class Customer implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "custId", unique = true, nullable = false)
	private Long custId;

	@Comment(value = "地址")
	@Column(name = "address")
	private String address;

	@Comment(value = "客户名称")
	@Column(name = "custName", length = 30)
	private String custName;

	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	@Comment(value = "电话")
	@Column(name = "phone", length = 30)
	private String phone;

	@Comment(value = "状态")
	@Column(name = "status", nullable = false, length = 1)
	private String status;

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getDelTag() {
		return delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

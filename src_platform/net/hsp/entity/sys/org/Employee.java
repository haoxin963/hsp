package net.hsp.entity.sys.org;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Comment(value = "员工信息")
@Table(name = "pubmodule_employee_tbl")
public class Employee implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "工号")
	@Column(name = "empno", nullable = false, length = 50)
	private String empno;

	@Comment(value = "姓名")
	@Column(name = "empname", nullable = false)
	private String empname;

	@Comment(value = "部门")
	@Column(name = "deptId", nullable = false)
	private String deptId;
	
	@Comment(value = "性别")
	@Column(name = "sex", length = 2)
	private String sex;

	@Comment(value = "状态")
	@Column(name = "status", nullable = false, length = 1)
	private String status;

	@Comment(value = "身份证号")
	@Column(name = "idCard", length = 50)
	private String idCard;

	@Comment(value = "入职日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "entryDate", length = 10)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date entryDate;

	@Comment(value = "职离日期")
	@Temporal(TemporalType.DATE)
	@Column(name = "departureDate", length = 10)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date departureDate;

	
	@Comment(value = "系统生成用ID")
	@Column(name = "sysUserId", length = 50)
	private Integer sysUserId;
	
	@Comment(value = "自定义用户名")
	@Column(name = "reusername", length = 50)
	private String reusername;

	@Comment(value = "序号")
	@Column(name = "sortNo")
	private Integer sortNo;

	@Comment(value = "备注")
	@Column(name = "remark")
	private String remark;

	@Comment(value = "手机号")
	@Column(name = "mobile", length = 50)
	private String mobile;

	@Comment(value = "手机号2")
	@Column(name = "mobile2", length = 50)
	private String mobile2;

	@Comment(value = "公室办电话")
	@Column(name = "officeTel", length = 50)
	private String officeTel;

	@Comment(value = "家庭电话")
	@Column(name = "homeTel", length = 50)
	private String homeTel;

	@Comment(value = "QQ")
	@Column(name = "qq", length = 50)
	private String qq;

	@Comment(value = "邮箱")
	@Column(name = "email")
	private String email;

	@Comment(value = "地址")
	@Column(name = "address")
	private String address;

	@Comment(value = "删除标记")
	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	public Employee() {
	}

	public Employee(String empno, String empname, String status, String delTag) {
		this.empno = empno;
		this.empname = empname;
		this.status = status;
		this.delTag = delTag;
	}

	public Employee(String empno, String empname, String sex, String status,
			String idCard, Date entryDate, Date departureDate, String reusername,
			Integer sortNo, String remark, String mobile, String mobile2,
			String officeTel, String homeTel, String qq, String email,
			String address, String delTag) {
		this.empno = empno;
		this.empname = empname;
		this.sex = sex;
		this.status = status;
		this.idCard = idCard;
		this.entryDate = entryDate;
		this.departureDate = departureDate;
		this.reusername = reusername;
		this.sortNo = sortNo;
		this.remark = remark;
		this.mobile = mobile;
		this.mobile2 = mobile2;
		this.officeTel = officeTel;
		this.homeTel = homeTel;
		this.qq = qq;
		this.email = email;
		this.address = address;
		this.delTag = delTag;
	}

	/** get 主键 */
	public Long getId() {
		return this.id;
	}

	/** set 主键 */
	public void setId(long id) {
		this.id = id;
	}

	/** get 工号 */
	public String getEmpno() {
		return this.empno;
	}

	/** set 工号 */
	public void setEmpno(String empno) {
		this.empno = empno;
	}

	/** get 姓名 */
	public String getEmpname() {
		return this.empname;
	}

	/** set 姓名 */
	public void setEmpname(String empname) {
		this.empname = empname;
	}

	/** get 性别 */
	public String getSex() {
		return this.sex;
	}

	/** set 性别 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/** get 状态 */
	public String getStatus() {
		return this.status;
	}

	/** set 状态 */
	public void setStatus(String status) {
		this.status = status;
	}

	/** get 身份证号 */
	public String getIdCard() {
		return this.idCard;
	}

	/** set 身份证号 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/** get 入职日期 */
	public Date getEntryDate() {
		return this.entryDate;
	}

	/** set 入职日期 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	/** get 职离日期 */
	public Date getDepartureDate() {
		return this.departureDate;
	}

	/** set 职离日期 */
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	/** get 自定义用户名 */
	public String getReusername() {
		return this.reusername;
	}

	/** set 自定义用户名 */
	public void setReusername(String reusername) {
		this.reusername = reusername;
	}

	/** get 序号 */
	public Integer getSortNo() {
		return this.sortNo;
	}

	/** set 序号 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/** get 备注 */
	public String getRemark() {
		return this.remark;
	}

	/** set 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** get 手机号 */
	public String getMobile() {
		return this.mobile;
	}

	/** set 手机号 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/** get 手机号2 */
	public String getMobile2() {
		return this.mobile2;
	}

	/** set 手机号2 */
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	/** get 公室办电话 */
	public String getOfficeTel() {
		return this.officeTel;
	}

	/** set 公室办电话 */
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	/** get 家庭电话 */
	public String getHomeTel() {
		return this.homeTel;
	}

	/** set 家庭电话 */
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	/** get QQ */
	public String getQq() {
		return this.qq;
	}

	/** set QQ */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/** get 邮箱 */
	public String getEmail() {
		return this.email;
	}

	/** set 邮箱 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** get 地址 */
	public String getAddress() {
		return this.address;
	}

	/** set 地址 */
	public void setAddress(String address) {
		this.address = address;
	}

	/** get 删除标记 */
	public String getDelTag() {
		return this.delTag;
	}

	/** set 删除标记 */
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	 

}

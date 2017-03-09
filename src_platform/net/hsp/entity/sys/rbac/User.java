package net.hsp.entity.sys.rbac;

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
@Comment(value = "用户")
@Table(name = "pubmodule_user_tbl")
public class User implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Comment(value = "域")
	@Column(name = "domain", length = 16)
	private String domain;

	@Comment(value = "所属部门")
	@Column(name = "department_id")
	private String departmentId;

	@Comment(value = "真实姓名")
	@Column(name = "trueName", nullable = false, length = 100)
	private String trueName;

	@Comment(value = "用户名")
	@Column(name = "userName", nullable = false, length = 50)
	private String userName;

	@Comment(value = "密码")
	@Column(name = "password")
	private String password;

	@Comment(value = "性别")
	@Column(name = "sex", length = 1)
	private String sex = "1";

	@Comment(value = "个性签名")
	@Column(name = "signature")
	private String signature;

	@Comment(value = "排序号")
	@Column(name = "sortNo")
	private Long sortNo;

	@Comment(value = "用户名的拼音")
	@Column(name = "spelling")
	private String spelling;

	@Comment(value = "用户名首字母")
	@Column(name = "initials")
	private String initials;


	@Comment(value = "直接上级")
	@Column(name = "director", length = 100)
	private String director;

	@Comment(value = "E-mail")
	@Column(name = "email", length = 50)
	private String email;

	// null|0:登录时不需要修改密码  1.登录时需要修改（还未修改） 2.已经修改过密码
	@Comment(value = "是否需要修改密码")
	@Column(name = "isUpdatePass", length = 1)
	private String isUpdatePass;
	
	@Comment(value = "移动电话")
	@Column(name = "mobileTelephone", length = 20)
	private String mobileTelephone;

	@Comment(value = "移动电话是否公开")
	@Column(name = "isPublicMobileTelephone", length = 1)
	private String isPublicMobileTelephone = "1";
	

	@Comment(value = "上次登录IP")
	@Column(name = "lastLogIP")
	private String lastLogIp;

	@Comment(value = "上次登录时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastLogTime", length = 19)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date lastLogTime;

	@Comment(value = "上次登录类型")
	@Column(name = "lastLogType")
	private String lastLogType;

	@Comment(value = "登录统计")
	@Column(name = "loginCount")
	private Integer loginCount;

	@Comment(value = "是否可以重复登录")
	@Column(name = "loginType", length = 1)
	private String loginType;


	@Comment(value = "创建员工档案标识")
	@Column(name = "isCreateStaff")
	private String isCreateStaff;
	
	@Comment(value = "状态")
	@Column(name = "status", length = 1)
	private String status;
	

	@Comment(value = "删除标记")
	@Column(name = "delTag", length = 1)
	private String delTag;
 
 
	/** get 主键 */
	public Long getId() {
		return this.id;
	}

	/** set 主键 */
	public void setId(Long id) {
		this.id = id;
	}

	/** get 删除标记 */
	public String getDelTag() {
		return this.delTag;
	}

	/** set 删除标记 */
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	/** get 直接上级 */
	public String getDirector() {
		return this.director;
	}

	/** set 直接上级 */
	public void setDirector(String director) {
		this.director = director;
	}

	/** get 可容纳子站点用户数 */
	public String getDomain() {
		return this.domain;
	}

	/** set 可容纳子站点用户数 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/** get E-mail */
	public String getEmail() {
		return this.email;
	}

	/** set E-mail */
	public void setEmail(String email) {
		this.email = email;
	}

	/** get 用户名首字母 */
	public String getInitials() {
		return this.initials;
	}

	/** set 用户名首字母 */
	public void setInitials(String initials) {
		this.initials = initials;
	}

	/** get 创建档案标识 */
	public String getIsCreateStaff() {
		return this.isCreateStaff;
	}

	/** set 创建档案标识 */
	public void setIsCreateStaff(String isCreateStaff) {
		this.isCreateStaff = isCreateStaff;
	}

	/** get 移动电话是否公开 */
	public String getIsPublicMobileTelephone() {
		return this.isPublicMobileTelephone;
	}

	/** set 移动电话是否公开 */
	public void setIsPublicMobileTelephone(String isPublicMobileTelephone) {
		this.isPublicMobileTelephone = isPublicMobileTelephone;
	}

	/** get 上次登录IP */
	public String getLastLogIp() {
		return this.lastLogIp;
	}

	/** set 上次登录IP */
	public void setLastLogIp(String lastLogIp) {
		this.lastLogIp = lastLogIp;
	}

	/** get 上次登录时间 */
	public Date getLastLogTime() {
		return this.lastLogTime;
	}

	/** set 上次登录时间 */
	public void setLastLogTime(Date lastLogTime) {
		this.lastLogTime = lastLogTime;
	}

	/** get 上次登录类型 */
	public String getLastLogType() {
		return this.lastLogType;
	}

	/** set 上次登录类型 */
	public void setLastLogType(String lastLogType) {
		this.lastLogType = lastLogType;
	}

	/** get 登录统计 */
	public Integer getLoginCount() {
		return this.loginCount;
	}

	/** set 登录统计 */
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	/** get 是否可以重复登录 */
	public String getLoginType() {
		return this.loginType;
	}

	/** set 是否可以重复登录 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/** get 移动电话 */
	public String getMobileTelephone() {
		return this.mobileTelephone;
	}

	/** set 移动电话 */
	public void setMobileTelephone(String mobileTelephone) {
		this.mobileTelephone = mobileTelephone;
	}

	/** get 密码 */
	public String getPassword() {
		return this.password;
	}

	/** set 密码 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** get 性别 */
	public String getSex() {
		return this.sex;
	}

	/** set 性别 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/** get 个性签名 */
	public String getSignature() {
		return this.signature;
	}

	/** set 个性签名 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/** get 排序号 */
	public Long getSortNo() {
		return this.sortNo;
	}

	/** set 排序号 */
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	/** get 用户名的拼音 */
	public String getSpelling() {
		return this.spelling;
	}

	/** set 用户名的拼音 */
	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	/** get 电子邮件状态 */
	public String getStatus() {
		return this.status;
	}

	/** set 电子邮件状态 */
	public void setStatus(String status) {
		this.status = status;
	}

	/** get 真实姓名 */
	public String getTrueName() {
		return this.trueName;
	}

	/** set 真实姓名 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/** get 用户名 */
	public String getUserName() {
		return this.userName;
	}

	/** set 用户名 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** get 所属部门 */
	public String getDepartmentId() {
		return this.departmentId;
	}

	/** set 所属部门 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/** get 是否需要修改密码 */
	public String getIsUpdatePass() {
		return isUpdatePass;
	}

	/** set 是否需要修改密码 */
	public void setIsUpdatePass(String isUpdatePass) {
		this.isUpdatePass = isUpdatePass;
	}	 

	
}

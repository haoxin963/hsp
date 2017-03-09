package net.hsp.entity.sys.rbac;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "pubmodule_userrole_tbl")
@Table(name = "pubmodule_userrole_tbl")
public class Userrole implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "wayMark", length = 10)
	private String wayMark;

	@Comment(value = "{fk:'net.hsp.entity.sys.rbac.Role'}")
	@Column(name = "role_id")
	private Long roleId;

	@Comment(value = "{fk:'net.hsp.entity.sys.rbac.User'}")
	@Column(name = "user_id")
	private Long userId;

	public Userrole() {
	}

	public Userrole(String wayMark, Long roleId, Long userId) {
		this.wayMark = wayMark;
		this.roleId = roleId;
		this.userId = userId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWayMark() {
		return this.wayMark;
	}

	public void setWayMark(String wayMark) {
		this.wayMark = wayMark;
	}

	/** get {fk:'net.hsp.entity.sys.rbac.Role'} */
	public Long getRoleId() {
		return this.roleId;
	}

	/** set {fk:'net.hsp.entity.sys.rbac.Role'} */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/** get {fk:'net.hsp.entity.sys.rbac.User'} */
	public Long getUserId() {
		return this.userId;
	}

	/** set {fk:'net.hsp.entity.sys.rbac.User'} */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}

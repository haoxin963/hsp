package net.hsp.entity.sys.rbac;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "pubmodule_rolefunction_tbl")
@Table(name = "pubmodule_rolefunction_tbl")
public class Rolefunction implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "functionId")
	private String functionId;

	@Column(name = "type")
	private String type;

	@Comment(value = "{fk:'net.hsp.entity.sys.rbac.Role'}")
	@Column(name = "role_id")
	private Long roleId;

	public Rolefunction() {
	}

	public Rolefunction(String functionId, String type, Long roleId) {
		this.functionId = functionId;
		this.type = type;
		this.roleId = roleId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunctionId() {
		return this.functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/** get {fk:'net.hsp.entity.sys.rbac.Role'} */
	public Long getRoleId() {
		return this.roleId;
	}

	/** set {fk:'net.hsp.entity.sys.rbac.Role'} */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}

package net.hsp.entity.sys.rbac;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "角色表")
@Table(name = "pubmodule_role_tbl")
public class Role implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
//	@Comment(value = "域")
//	@Column(name = "domain",length = 16)
//	private String domain;

	@Comment(value = "删除标记")
	@Column(name = "delTag", length = 1)
	private String delTag;


	@Comment(value = "角色标记")
	@Column(name = "role", nullable = false)
	private String role;

	@Comment(value = "角色名称")
	@Column(name = "roleName", nullable = false)
	private String roleName;

	@Comment(value = "途径标记")
	@Column(name = "wayMark")
	private String wayMark;
	
	public Role() {
	}

	public Role(String role, String roleName) {
		this.role = role;
		this.roleName = roleName;
	}
 

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


	/** get 角色标记 */
	public String getRole() {
		return this.role;
	}

	/** set 角色标记 */
	public void setRole(String role) {
		this.role = role;
	}

	/** get 角色名称 */
	public String getRoleName() {
		return this.roleName;
	}

	/** set 角色名称 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/** get 途径标记 */
	public String getWayMark() {
		return this.wayMark;
	}

	/** set 途径标记 */
	public void setWayMark(String wayMark) {
		this.wayMark = wayMark;
	}

//	public String getDomain() {
//		return domain;
//	}
//
//	public void setDomain(String domain) {
//		this.domain = domain;
//	}
}

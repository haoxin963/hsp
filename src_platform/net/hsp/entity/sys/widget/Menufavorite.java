package net.hsp.entity.sys.widget;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "用户菜单收藏")
@Table(name = "pubmodule_menufavorite_tbl")
public class Menufavorite implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "功能ID")
	@Column(name = "functionId", nullable = false, length = 32)
	private String functionId;

	@Comment(value = "别名")
	@Column(name = "aliasName", length = 50)
	private String aliasName;

	@Comment(value = "所属用户")
	@Column(name = "userId", nullable = false, length = 32)
	private String userId;
	
	@Comment(value = "排序")
	@Column(name = "sortNo", length =32)
	private String sortNo;

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public Menufavorite() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** get 功能ID */
	public String getFunctionId() {
		return this.functionId;
	}

	/** set 功能ID */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/** get 别名 */
	public String getAliasName() {
		return this.aliasName;
	}

	/** set 别名 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	/** get 所属用户 */
	public String getUserId() {
		return this.userId;
	}

	/** set 所属用户 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}

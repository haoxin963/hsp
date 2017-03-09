package net.hsp.entity.sys.widget;

import net.hsp.common.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "菜单收藏")
@Table(name = "pubmodule_favorite_tbl")
public class Favorite implements java.io.Serializable {

	@Comment(value = "自增长ID")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "用户ID")
	@Column(name = "userId", nullable = false)
	private int userId;

	@Comment(value = "菜单ID")
	@Column(name = "functionId", nullable = false, length = 100)
	private String functionId;

	@Comment(value = "菜单图标")
	@Column(name = "iconPath", length = 100)
	private String iconPath;

	@Comment(value = "菜单别名")
	@Column(name = "aliasName", length = 100)
	private String aliasName;

	@Comment(value = "排序值")
	@Column(name = "sortNo", nullable = false)
	private int sortNo;

	public Favorite() {
	}

	public Favorite(int userId, String functionId, int sortNo) {
		this.userId = userId;
		this.functionId = functionId;
		this.sortNo = sortNo;
	}

	public Favorite(int userId, String functionId, String iconPath,
			String aliasName, int sortNo) {
		this.userId = userId;
		this.functionId = functionId;
		this.iconPath = iconPath;
		this.aliasName = aliasName;
		this.sortNo = sortNo;
	}

	/** get 自增长ID */
	public Integer getId() {
		return this.id;
	}

	/** set 自增长ID */
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 用户ID */
	public int getUserId() {
		return this.userId;
	}

	/** set 用户ID */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/** get 菜单ID */
	public String getFunctionId() {
		return this.functionId;
	}

	/** set 菜单ID */
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	/** get 菜单图标 */
	public String getIconPath() {
		return this.iconPath;
	}

	/** set 菜单图标 */
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	/** get 菜单别名 */
	public String getAliasName() {
		return this.aliasName;
	}

	/** set 菜单别名 */
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	/** get 排序值 */
	public int getSortNo() {
		return this.sortNo;
	}

	/** set 排序值 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
}
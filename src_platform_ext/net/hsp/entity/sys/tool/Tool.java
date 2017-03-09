package net.hsp.entity.sys.tool;

import net.hsp.common.Comment;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Comment(value = "pubmodule_tool_tbl")
@Table(name = "pubmodule_tool_tbl")
public class Tool implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "组件名")
	@Column(name = "toolname", length = 20)
	private String toolname;

	@Comment(value = "组件class")
	@Column(name = "toolclass", length = 50)
	private String toolclass;

	public Tool() {
	}

	public Tool(String toolname, String toolclass) {
		this.toolname = toolname;
		this.toolclass = toolclass;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** get 组件名 */
	public String getToolname() {
		return this.toolname;
	}

	/** set 组件名 */
	public void setToolname(String toolname) {
		this.toolname = toolname;
	}

	/** get 组件class */
	public String getToolclass() {
		return this.toolclass;
	}

	/** set 组件class */
	public void setToolclass(String toolclass) {
		this.toolclass = toolclass;
	}

}

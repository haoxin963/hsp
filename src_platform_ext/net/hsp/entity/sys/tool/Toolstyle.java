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
@Comment(value = "pubmodule_toolstyle_tbl")
@Table(name = "pubmodule_toolstyle_tbl")
public class Toolstyle implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "属性名")
	@Column(name = "stylename", length = 30)
	private String stylename;

	@Comment(value = "属性规则")
	@Column(name = "stylefunction", length = 100)
	private String stylefunction;

	@Comment(value = "0颜色1图片2实线虚线")
	@Column(name = "styletype")
	private Integer styletype;

	@Comment(value = "属性初始值")
	@Column(name = "stylevalue", length = 50)
	private String stylevalue;

	@Comment(value = "图片所在文件夹")
	@Column(name = "filepath", length = 50)
	private String filepath;

	public Toolstyle() {
	}

	public Toolstyle(String stylename, String stylefunction, Integer styletype, String stylevalue, String filepath) {
		this.stylename = stylename;
		this.stylefunction = stylefunction;
		this.styletype = styletype;
		this.stylevalue = stylevalue;
		this.filepath = filepath;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** get 属性名 */
	public String getStylename() {
		return this.stylename;
	}

	/** set 属性名 */
	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

	/** get 属性规则 */
	public String getStylefunction() {
		return this.stylefunction;
	}

	/** set 属性规则 */
	public void setStylefunction(String stylefunction) {
		this.stylefunction = stylefunction;
	}

	/** get 0颜色1图片2实线虚线 */
	public Integer getStyletype() {
		return this.styletype;
	}

	/** set 0颜色1图片2实线虚线 */
	public void setStyletype(Integer styletype) {
		this.styletype = styletype;
	}

	/** get 属性初始值 */
	public String getStylevalue() {
		return this.stylevalue;
	}

	/** set 属性初始值 */
	public void setStylevalue(String stylevalue) {
		this.stylevalue = stylevalue;
	}

	/** get 图片所在文件夹 */
	public String getFilepath() {
		return this.filepath;
	}

	/** set 图片所在文件夹 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

}

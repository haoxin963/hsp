package net.hsp.entity.sys.org;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.hsp.common.Comment;

@Entity
@Comment(value = "职位信息")
@Table(name = "pubmodule_position_tbl")
public class Position implements java.io.Serializable {

	@Comment(value = "主键")
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Comment(value = "职位代号")
	@Column(name = "positionNo", nullable = false, length = 50)
	private String positionNo;

	@Comment(value = "职位名称")
	@Column(name = "positionName", nullable = false)
	private String positionName;

	@Comment(value = "备注")
	@Column(name = "remark", length = 500)
	private String remark;
	
	@Comment(value = "对应角色")
	@Column(name = "refrole", length = 255)
	private String refrole;
	
	@Comment(value = "删除标记")
	@Column(name = "delTag", nullable = false, length = 1)
	private String delTag;

	public Position() {
	}

	public Position(String positionNo, String positionName, String delTag) {
		this.positionNo = positionNo;
		this.positionName = positionName;
		this.delTag = delTag;
	}

	public Position(String positionNo, String positionName, String remark,
			String delTag) {
		this.positionNo = positionNo;
		this.positionName = positionName;
		this.remark = remark;
		this.delTag = delTag;
	}

	/** get 主键 */
	public Integer getId() {
		return this.id;
	}

	/** set 主键 */
	public void setId(Integer id) {
		this.id = id;
	}

	/** get 职位代号 */
	public String getPositionNo() {
		return this.positionNo;
	}

	/** set 职位代号 */
	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}

	/** get 职位名称 */
	public String getPositionName() {
		return this.positionName;
	}

	/** set 职位名称 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	/** get 备注 */
	public String getRemark() {
		return this.remark;
	}

	/** set 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** get 删除标记 */
	public String getDelTag() {
		return this.delTag;
	}

	/** set 删除标记 */
	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getRefrole() {
		return refrole;
	}

	public void setRefrole(String refrole) {
		this.refrole = refrole;
	}

	
}

package net.hsp.entity.sys.dic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "pubmodule_dictype_tbl")
public class DicTypeEntity implements Serializable {

	// 类型ID
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dicTypeId")
	private Integer dicTypeId;

	// 类型名
	@Column(name = "dicTypeName", length = 100)
	private String dicTypeName;

	// 类型码（唯一）
	@Column(name = "typeCode", length = 20, nullable = false)
	private String typeCode;

	// 删除标记（默认未删除）
	@Column(name = "delTag", length = 1)
	private String delTag = "0";

	// 是否启用（默认不启用）
	@Column(name = "state", length = 1)
	private String state = "0";

	public Integer getDicTypeId() {
		return dicTypeId;
	}

	public void setDicTypeId(Integer dicTypeId) {
		this.dicTypeId = dicTypeId;
	}

	public String getDicTypeName() {
		return dicTypeName;
	}

	public void setDicTypeName(String dicTypeName) {
		this.dicTypeName = dicTypeName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDelTag() {
		return delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Transient
	private List<DicExtFieldEntity> extFields = new ArrayList<DicExtFieldEntity>();

	public List<DicExtFieldEntity> getExtFields() {
		return extFields;
	}

	public void setExtFields(List<DicExtFieldEntity> extFields) {
		this.extFields = extFields;
	}

}

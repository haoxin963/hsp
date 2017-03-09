package net.hsp.entity.sys.dic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pubmodule_dicextfield_tbl")
public class DicExtFieldEntity implements Serializable {
	@Id
	@Column(name = "extField", nullable = false, updatable = false)
	private String extField;
	@Id
	@Column(name = "dicTypeId", nullable = false, updatable = false)
	private Integer dicTypeId;
	@Column(name = "fieldName")
	private String fieldName;
	@Column(name = "validation")
	private String validation;
	@Column(name = "description")
	private String description;
	@Column(name = "errorMsg")
	private String errorMsg;
	@Column(name = "display")
	private Boolean display = true;

	public Integer getDicTypeId() {
		return dicTypeId;
	}

	public void setDicTypeId(Integer dicTypeId) {
		this.dicTypeId = dicTypeId;
	}

	public String getExtField() {
		return extField;
	}

	public void setExtField(String extField) {
		this.extField = extField;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

}

package net.hsp.entity.sys.dic;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pubmodule_dictionary_tbl")
public class DictionaryEntity implements Serializable {

	// 字典ID
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "dictId", unique = true, nullable = false)
	private Long dictId;

	@Column(name = "dicTypeId", nullable = false, updatable = false)
	private Integer dicTypeId;

	@Column(name = "dicCode")
	private String dicCode;
	// 字典名称
	@Column(name = "dictName", length = 100)
	private String dictName;

	// 类型码(对应关联字典类型)
	@Column(name = "typeCode", length = 20, nullable = false)
	private String typeCode;

	// 扩展字段
	@Column(name = "extField", length = 50)
	private String extField;

	// 字典排序
	@Column(name = "sortNo")
	private Integer sortNo;

	// 字典是否启用（默认禁用）
	@Column(name = "state", length = 1)
	private String state = "0";

	// 删除标记 (默认未删除)
	@Column(name = "delTag", length = 1)
	private String delTag = "0";

	// 缺省值
	@Column(name = "isDefault")
	private String isDefault = "0";

	@Column(name = "ext0")
	private String ext0;
	@Column(name = "ext1")
	private String ext1;
	@Column(name = "ext2")
	private String ext2;
	@Column(name = "ext3")
	private String ext3;
	@Column(name = "ext4")
	private String ext4;
	@Column(name = "ext5")
	private String ext5;
	@Column(name = "ext6")
	private String ext6;
	@Column(name = "ext7")
	private String ext7;
	@Column(name = "ext8")
	private String ext8;
	@Column(name = "ext9")
	private String ext9;
	@Column(name = "ext10")
	private String ext10;
	@Column(name = "ext11")
	private String ext11;
	@Column(name = "ext12")
	private String ext12;
	@Column(name = "ext13")
	private String ext13;
	@Column(name = "ext14")
	private String ext14;
	@Column(name = "ext15")
	private String ext15;
	@Column(name = "ext16")
	private String ext16;
	@Column(name = "ext17")
	private String ext17;
	@Column(name = "ext18")
	private String ext18;
	@Column(name = "ext19")
	private String ext19;

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getExtField() {
		return extField;
	}

	public void setExtField(String extField) {
		this.extField = extField;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDelTag() {
		return delTag;
	}

	public void setDelTag(String delTag) {
		this.delTag = delTag;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getExt0() {
		return ext0;
	}

	public void setExt0(String ext0) {
		this.ext0 = ext0;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getExt10() {
		return ext10;
	}

	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}

	public String getExt11() {
		return ext11;
	}

	public void setExt11(String ext11) {
		this.ext11 = ext11;
	}

	public String getExt12() {
		return ext12;
	}

	public void setExt12(String ext12) {
		this.ext12 = ext12;
	}

	public String getExt13() {
		return ext13;
	}

	public void setExt13(String ext13) {
		this.ext13 = ext13;
	}

	public String getExt14() {
		return ext14;
	}

	public void setExt14(String ext14) {
		this.ext14 = ext14;
	}

	public String getExt15() {
		return ext15;
	}

	public void setExt15(String ext15) {
		this.ext15 = ext15;
	}

	public String getExt16() {
		return ext16;
	}

	public void setExt16(String ext16) {
		this.ext16 = ext16;
	}

	public String getExt17() {
		return ext17;
	}

	public void setExt17(String ext17) {
		this.ext17 = ext17;
	}

	public String getExt18() {
		return ext18;
	}

	public void setExt18(String ext18) {
		this.ext18 = ext18;
	}

	public String getExt19() {
		return ext19;
	}

	public void setExt19(String ext19) {
		this.ext19 = ext19;
	}

	public Integer getDicTypeId() {
		return dicTypeId;
	}

	public void setDicTypeId(Integer dicTypeId) {
		this.dicTypeId = dicTypeId;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

}

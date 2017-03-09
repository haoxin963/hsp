package net.hsp.entity.sys.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import net.sf.json.JSONArray;

@SuppressWarnings("serial")
@Entity
@Table(name = "pubmodule_configitem_tbl")
public class ConfigItem implements Serializable {

	@Id
	@Column(name = "itemKey", length = 100)
	private String itemKey;

	@Column(name = "itemName", length = 100)
	private String itemName;

	@Column(name = "itemType", length = 10, nullable = false)
	private String itemType;

	@Column(name = "itemValue")
	private String itemValue;

	@Column(name = "itemGroup")
	private String itemGroup;

	@Transient
	private List<ConfigItemValue> itemValues = new ArrayList<ConfigItemValue>();

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public void setItemValue(List<ConfigItemValue> itemValues) {
		if (null != itemValues) {
			this.itemValue = JSONArray.fromObject(itemValues).toString();
		}
	}

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	@SuppressWarnings("unchecked")
	public List<ConfigItemValue> getItemValues() {
		if (null != this.itemValue && !this.itemValue.isEmpty()) {
			JSONArray jsonArray = JSONArray.fromObject(this.itemValue);
			this.itemValues = (List<ConfigItemValue>) JSONArray.toCollection(
					jsonArray, ConfigItemValue.class);
		}
		return this.itemValues;
	}
}

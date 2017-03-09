package net.hsp.entity.sys.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pubmodule_configvalue_tbl")
public class ConfigVaule implements Serializable {

	@Column(name = "key", length = 100)
	private String key;

	@Column(name = "value")
	private String value;

	private ConfigItem configItem;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String[] getValues() {
		String[] values = null;
		if (null != value) {
			values = value.split(",");
		} else {
			values = new String[] {};
		}
		return values;
	}

	public List<String> getValueList() {
		return Arrays.asList(getValues());
	}

	public ConfigItem getConfigItem() {
		return configItem;
	}

	public void setConfigItem(ConfigItem configItem) {
		this.configItem = configItem;
	}

	public ConfigVaule(String key, String... values) {
		super();
		this.key = key;
		this.value = "";
		if (null != values) {
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					this.value += ",";
				}
				this.value += values[i];
			}
		}
	}

	public ConfigVaule() {
		super();
	}
}

package net.hsp.entity.sys.config;

import java.io.Serializable;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
public class ConfigItemGroup implements Serializable {
	private String itemGroup;

	private String itemGroupName;

	private Integer isComman = 0;

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public String getItemGroupName() {
		return itemGroupName;
	}

	public void setItemGroupName(String itemGroupName) {
		this.itemGroupName = itemGroupName;
	}

	public Integer getIsComman() {
		return isComman;
	}

	public void setIsComman(Integer isComman) {
		this.isComman = isComman;
	}

	public ConfigItemGroup(String itemGroup, String itemGroupName,
			Integer isComman) {
		super();
		this.itemGroup = itemGroup;
		this.itemGroupName = itemGroupName;
		this.isComman = isComman;
	}

}

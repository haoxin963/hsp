package net.hsp.service.sys.config;

import java.util.LinkedHashMap;
import java.util.List;

import net.hsp.entity.sys.config.ConfigItem;
import net.hsp.entity.sys.config.ConfigItemGroup;
import net.hsp.entity.sys.config.ConfigVaule;

public interface ConfigService {
	public LinkedHashMap<String, ConfigItemGroup> getItemGroups();

	public ConfigItemGroup getItemGroup(String itemGroup);

	public List<ConfigItem> listConfigItem(String custId);

	public List<ConfigVaule> listConfigVaule(String custId);

	public void batchUpdateConifg(String custId, List<ConfigVaule> configValues);

	public void setConfig(String custId, String key, String... values);

	public ConfigVaule getConfig(String custId, String key);

	public void saveConfigItem(String custId, ConfigItem configItem);
}
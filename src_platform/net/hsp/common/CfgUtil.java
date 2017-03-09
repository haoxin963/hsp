package net.hsp.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.config.ConfigItem;
import net.hsp.entity.sys.config.ConfigVaule;
import net.hsp.service.sys.config.ConfigService;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.SpringCtx;

@SuppressWarnings("serial")
public class CfgUtil {

	private static Map<String, LinkedHashMap<String, ConfigVaule>> configs = null;

	public static ConfigService getService() {
		return (ConfigService) SpringCtx.getSpringContext().getBean(
				"configServiceImpl");
	}

	public static LinkedHashMap<String, ConfigVaule> getConfigs(String custId) {
		if (null == configs) {
			configs = new HashMap<String, LinkedHashMap<String, ConfigVaule>>();
		}
		LinkedHashMap<String, ConfigVaule> valueMap = configs.get(custId);
		if (null == valueMap || valueMap.isEmpty()) {
			valueMap = new LinkedHashMap<String, ConfigVaule>();
			List<ConfigVaule> configValues = getService().listConfigVaule(
					custId);
			List<ConfigItem> configItems = getService().listConfigItem(custId);
			if (null != configItems) {
				Map<String, ConfigVaule> configVauleMap = new HashMap<String, ConfigVaule>();
				for (ConfigVaule configValue : configValues) {
					configVauleMap.put(configValue.getKey(), configValue);
				}
				for (ConfigItem configItem : configItems) {
					String key = configItem.getItemKey();
					ConfigVaule configVaule = null;
					if (configVauleMap.containsKey(key)) {
						configVaule = configVauleMap.get(key);
					} else {
						configVaule = new ConfigVaule();
						configVaule.setKey(key);
					}
					configVaule.setConfigItem(configItem);
					valueMap.put(key, configVaule);
				}
				configs.put(custId, valueMap);
			}

		}
		return configs.get(custId);
	}

	public static String get(String key) {
		String custId = HttpSessionFactory.getCustId(ActionUtil.getCtx()
				.getRequest());
		return get(key, custId);
	}

	public static String get(String key, String custId) {
		String value = null;
		Map<String, ConfigVaule> map = getConfigs(custId);
		if (null != map && map.containsKey(key)) {
			ConfigVaule configVaule = map.get(key);
			value = configVaule.getValue();
		}
		return value;
	}

	public static String[] getValues(String key) {
		String custId = HttpSessionFactory.getCustId(ActionUtil.getCtx()
				.getRequest());
		return getValues(key, custId);
	}

	public static String[] getValues(String custId, String key) {
		String[] values = null;
		Map<String, ConfigVaule> map = getConfigs(custId);
		if (null != map && map.containsKey(key)) {
			ConfigVaule configVaule = map.get(key);
			values = configVaule.getValues();
		}
		return values;
	}

	public static List<String> getList(String custId, String key) {
		return Arrays.asList(getValues(custId, key));
	}

	public static void clear(String custId) {
		if (null == configs) {
			configs = new HashMap<String, LinkedHashMap<String, ConfigVaule>>();
		}
		if (null == custId) {
			configs.clear();
		} else {
			configs.remove(custId);
		}
	}
}

package net.hsp.web.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class StationCfgUtil {

	private static Map<String, StationCfgUtil> configs = null;

	@SuppressWarnings("unchecked")
	public static StationCfgUtil getInstance(String custId) {
		if (null == configs) {
			Map<String, StationCfgUtil> configMap = new HashMap<String, StationCfgUtil>();
			Map<String, Map<String, Object>> stations = HttpSessionFactory.getStations();
			for (String _custId : stations.keySet()) {
				Map<String, Object> station = stations.get(_custId);
				String configFile = (String) station.get("configFile");
				if (null != configFile && !configFile.isEmpty()) {
					Map<String, String> properties = JSONObject.fromObject(configFile);
					StationCfgUtil config = new StationCfgUtil();
					config.properties = properties;
					configMap.put(_custId, config);
				}
			}
			// 容器启动时，在站点加载完成前会触发到 此
			if (!configMap.isEmpty()) {
				configs = configMap;
			}
		}
		if(configs == null) {
			return null;
		}
		return configs.get(custId);
	}

	private Map<String, String> properties = null;

	public Map<String, String> getProperties() {
		return properties;
	}

	public String get(String key) {
		return properties.get(key);
	}
}

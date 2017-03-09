package net.hsp.service.sys.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.hsp.common.CfgUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.jdbc.BaseDAO;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.entity.sys.config.ConfigItem;
import net.hsp.entity.sys.config.ConfigItemGroup;
import net.hsp.entity.sys.config.ConfigVaule;
import net.hsp.service.BaseServiceImpl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

@SuppressWarnings("serial")
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {

	private static LinkedHashMap<String, ConfigItemGroup> itemGropus = new LinkedHashMap<String, ConfigItemGroup>() {
		{
			put("common", new ConfigItemGroup("common", "公共属性", 1));
			put("station", new ConfigItemGroup("station", "站点属性", 0));
		}
	};

	@Override
	public LinkedHashMap<String, ConfigItemGroup> getItemGroups() {
		return itemGropus;
	}

	@Override
	public ConfigItemGroup getItemGroup(String itemGroup) {
		return itemGropus.get(itemGroup);
	}

	@Override
	public List<ConfigItem> listConfigItem(String custId) {
		List<ConfigItem> configItems = new ArrayList<ConfigItem>();
		String sql = "SELECT * FROM pubmodule_configitem_tbl";
		try {
			List<ConfigItem> commonItems = configItems = this.getDAO(PlatFormConstant.BASESTATIONID).query(sql, new Object[] {}, new BeanPropertyRowMapper<ConfigItem>(ConfigItem.class));
			List<ConfigItem> custItems = configItems = this.getDAO(custId).query(sql, new Object[] {}, new BeanPropertyRowMapper<ConfigItem>(ConfigItem.class));
			configItems.addAll(commonItems);
			configItems.addAll(custItems);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configItems;
	}

	@Override
	public List<ConfigVaule> listConfigVaule(String custId) {
		List<ConfigVaule> configVaules = new ArrayList<ConfigVaule>();
		String sql = "SELECT * FROM pubmodule_configvalue_tbl";
		try {
			configVaules = this.getDAO(custId).query(sql, new Object[] {}, new BeanPropertyRowMapper<ConfigVaule>(ConfigVaule.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return configVaules;
	}

	@Override
	public void batchUpdateConifg(String custId, List<ConfigVaule> configValues) {
		
		BaseDAO dao = this.getDAO(custId);
		String sql = null;
		String dbType = DbInfo.getDbType(custId);
		if( DbInfo.ORACLE.equals(dbType) )
		{
			try {
				if (null != configValues) {
					for (ConfigVaule configValue : configValues) {
						sql = " merge into pubmodule_configvalue_tbl t1  " +
								" using (select '"+configValue.getKey()+"' key,'"+configValue.getValue()+"' value from dual) t2  " + 
								" on (t1.key = t2.key)  " +
								" when matched then  " +
								"     update set t1.value = t2.value  " +
								" when not matched then  " + 
								"     insert values (t2.key, t2.value)";
						dao.execute(sql);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			sql = " REPLACE INTO `pubmodule_configvalue_tbl` (`key`,`value`)  VALUES (:key,:value)";
			try {
				if (null != configValues) {
					for (ConfigVaule configValue : configValues) {
						this.getDAO(custId).executeUpdate(sql, configValue);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setConfig(String custId, String key, String... values) {
		ConfigVaule configValue = new ConfigVaule(key, values);

		String sql = " REPLACE INTO `pubmodule_configvalue_tbl` (`key`,`value`)  VALUES (:key,:value)";
		try {
			this.getDAO(custId).executeUpdate(sql, configValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public ConfigVaule getConfig(String custId, String key) {

		ConfigVaule value = null;
		String sql = "SELECT * FROM pubmodule_configvalue_tbl WHERE key=?";
		try {
			List<ConfigVaule> values = this.getDAO(custId).query(sql, new Object[] { key }, new BeanPropertyRowMapper(ConfigVaule.class));
			if (null != values && values.size() > 0) {
				value = values.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public void saveConfigItem(String custId, ConfigItem configItem) {
		String itemGroup = configItem.getItemGroup();
		ConfigItemGroup configItemGroup = this.getItemGroup(itemGroup);
		boolean isCommon = 1 == configItemGroup.getIsComman();
		if (isCommon) {
			custId = PlatFormConstant.BASESTATIONID;
		}
		BaseDAO dao = this.getDAO(custId);
		String sql = null;
		String dbType = DbInfo.getDbType(custId);
		if( DbInfo.ORACLE.equals(dbType) )
		{
			sql = " merge into pubmodule_configitem_tbl t1  " +
					" using (select '"+configItem.getItemKey()+"' itemKey,'"+configItem.getItemName()+"' itemName,'"+configItem.getItemGroup()+"' itemGroup, '"+configItem.getItemType()+"' itemType, '"+configItem.getItemValue()+"' itemValue from dual) t2  " + 
					" on (t1.itemKey = t2.itemKey)  " +
					" when matched then  " +
					"     update set t1.itemName = t2.itemName, t1.itemGroup = t2.itemGroup , t1.itemType = t2.itemType, t1.itemValue = t2.itemValue  " +
					" when not matched then  " + 
					"     insert values (t2.itemKey, t2.itemName, t2.itemGroup, t2.itemType, t2.itemValue)";
			try {
				dao.execute(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			sql = " REPLACE INTO `pubmodule_configitem_tbl` (`itemKey`,`itemName`,`itemGroup`,`itemType`,`itemValue`)  VALUES (:itemKey,:itemName,:itemGroup,:itemType,:itemValue)";
			try {
				dao.executeUpdate(sql, configItem);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (isCommon) {
			CfgUtil.clear(null);
		} else {
			CfgUtil.clear(custId);
		}
	}

}

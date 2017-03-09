package net.hsp.dao;

import java.util.Map;

import javax.sql.DataSource;

import net.hsp.web.util.SpringCtx;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private static ThreadLocal<String> local = new ThreadLocal<String>();
	
	private static ThreadLocal<String> localDomain = new ThreadLocal<String>();
	
	private static Map<String, String> dsTypeMap;
	
	public void setDsTypeMap(Map<String, String> value)
	{
		dsTypeMap = value;
	}
	public Map<String, String> getDsTypeMap() { return dsTypeMap; }
	
	public static Map<String, String> retrieveDsTypeMap() { return dsTypeMap; }

	/**
	 * 父类从resolvedDataSources查找，有找不到时使用default ,当动态追加了一个新的数据源时，resolvedDataSources没有更新。
	protected DataSource determineTargetDataSource() {
		Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = (DataSource) this.resolvedDataSources.get(lookupKey);
		if (dataSource == null) {
			dataSource = this.resolvedDefaultDataSource;
		}
		if (dataSource == null) {
			throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
		}
		return dataSource;
	}
	*/
	
	protected DataSource determineTargetDataSource() {
		//父类从resolvedDataSources查找，有找不到时使用default ,当动态追加了一个新的数据源时，resolvedDataSources没有更新,此时则从容器里直接取
		DataSource ds = null;
		try {
			ds = super.determineTargetDataSource();
		} catch (Exception e) {
			
		}
		if(ds== null){
			Object obj = determineCurrentLookupKey();
			ds= (DataSource) SpringCtx.getBean("ds_" + obj);
			if (ds == null) {
				throw new IllegalStateException("未知的 DataSource for lookup key [" + obj + "]");
			}
		}
		return ds;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return local.get();
	}

	/**
	 * 设置数据源路径
	 */
	public static void setCustId(String custId) {
		local.set(custId);
	}

	/**
	 * 获得当前数据源custId
	 */
	public static String getCustId() {
		return local.get();
	}
	
	public static void setDomain(String domain) {
		localDomain.set(domain);
	}
	
	public static String getDomain() {
		return localDomain.get();
	}
}

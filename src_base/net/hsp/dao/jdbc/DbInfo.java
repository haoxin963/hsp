package net.hsp.dao.jdbc;

import java.util.Map;

import net.hsp.dao.DynamicDataSource;

public class DbInfo
{
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";
	public static final String SQL_SERVER = "sqlServer";
	public static final String DB2 = "db2";
	public static final String POSTGRE_SQL = "postgreSql";
	public static final String MANGO_DB = "mangoDb";
	
	public static String getDbType()
	{
		String custId = DynamicDataSource.getCustId();
		return getDbType(custId);
	}
	
	public static String getDbType(String custId)
	{
		Map<String, String> dsTypeMap = DynamicDataSource.retrieveDsTypeMap();
		if( dsTypeMap == null ) { return MYSQL; }
		String type = dsTypeMap.get(custId);
		if( type == null )
		{
			type = MYSQL; 
		}
		return type;
	}
	
	/** CAST(xxxx as CHAR) */
	public static String StrCaster(int charLen, String dbType)
	{
		if(MYSQL.equals(dbType))
		{
			return "CHAR";
		}
		else if(ORACLE.equals(dbType))
		{
			return "varchar2("+charLen+")";
		}
		else
		{
			return "CHAR";
		}
	}
	
	public static String castString(String sql, int charLen, String dbType)
	{
		final String SC = StrCaster(charLen, dbType);
		sql = sql.replaceAll("DBSTRING", SC);
		return sql;
	}
	
	public static String TIME_PARAM()
	{
		String dbType = getDbType();
		if( ORACLE.equals(dbType) )
		{
			return "to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		}
		return "?";
	}
	
	public static String TIME(String timeStr)
	{
		return TIME(timeStr, getDbType());
	}
	
	public static String TIME(String timeStr, String dbType)
	{
		//to_date('2015-08-17 14:28:33','yyyy-mm-dd hh24:mi:ss')
		if( MYSQL.equals(dbType) )
		{
			return timeStr;
		}
		else if( ORACLE.equals(dbType) )
		{
			return "to_date('"+timeStr+"','yyyy-mm-dd hh24:mi:ss')";
		}
		return timeStr;
	}
	
	/** 格式化引号
	 * 标识符是 {'} */
	public static String quote(String sql)
	{
		return quote(sql, getDbType());
	}
	
	/** 格式化引号 */
	public static String quote(String sql, String dbType)
	{
		final String MARK = "\\{'\\}";//即{'}
		String qm = "";
		if( MYSQL.equals(dbType) ) { qm = "`"; }
		else if( ORACLE.equals(dbType) ) { qm = "\""; }
		String result = sql.replaceAll(MARK, qm);
		return result;
	}
	
}

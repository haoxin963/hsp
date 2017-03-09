package net.hsp.web.util;

import java.sql.Connection;
import java.sql.SQLException;

import net.hsp.common.SystemContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ConnectionListenerIF;

public class SqlListener implements ConnectionListenerIF {

	/**
	 * 限制执行毫秒数
	 */
	private static final int _LIMIT_MILLISECONDS = 100;

	private final static Logger log = Logger.getLogger(SqlListener.class);

	private final static Log logWarn = LogFactory.getLog("SpecialPurpose");

	public void onFail(String sql, Exception e) {
		log.error("onFail:" + sql + ":" + e.getMessage());
	}

	/**
	 * 执行sql语句时，超过指定毫秒数即记录下来。
	 * 
	 * @param sql
	 *            SQL语句
	 * @param elapsedTime
	 *            执行时间（毫秒）
	 * @return 中间库信息
	 */
	public void onExecute(String sql, long elapsedTime) {
		sql = SystemContext.getUserName() + " SQL:" + elapsedTime + "ms," + sql.replaceAll("\t", " ").replaceAll("\r", " ").replaceAll("\n", " ");
		log.info(sql);
		if (elapsedTime > _LIMIT_MILLISECONDS) {
			logWarn.warn(sql);
		}
	}

	public void onDeath(Connection connection, int arg1) throws SQLException {
	}

	public void onBirth(Connection connection) throws SQLException {
	}
}
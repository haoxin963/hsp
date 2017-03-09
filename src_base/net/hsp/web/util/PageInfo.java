package net.hsp.web.util;

import net.hsp.dao.jdbc.DbInfo;

public class PageInfo implements java.io.Serializable {
	public static String ROWS = "rows";

	private int page = 1;
	private int rows = 15;
	private long totalRows = 0;
	private int all = 0;// 全部显示

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		if (this.getAll() > 0) {
			return 0;
		}
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getFirstResult() {
		if (rows <= 0) {
			return 0;
		}
		if (page < 1) {
			page = 1;
		}
		return rows * (page - 1);
	}

	public int getMaxResult() {
		if (rows <= 0) {
			return 0;
		}
		if (page < 1) {
			page = 1;
		}
		return rows * page;
	}

	public static int pageParamRows(PageInfo p) {
		if (p == null) {
			return 0;
		}
		return p.getRows();
	}

	public static int pageParamFrom(PageInfo p) {
		if (p == null) {
			return 0;
		}
		return p.getRows() * (p.getPage() - 1);
	}

	public static int pageParamTo(PageInfo p) {
		if (p == null) {
			return 0;
		}
		return p.getRows() * p.getPage();
	}

	/** 对SQL语句进行分页处理 */
	public static String pageSQL(String sql, PageInfo pi) {
		String limitSql = "";
		int min = PageInfo.pageParamFrom(pi);
		int max = 0;
		String dbType = DbInfo.getDbType();
		if (DbInfo.MYSQL.equals(dbType)) {
			max = PageInfo.pageParamRows(pi);
			limitSql = "SELECT * FROM (" + sql + ") _A ";
			limitSql += "LIMIT " + min + "," + max;
		} else if (DbInfo.ORACLE.equals(dbType)) {
			max = PageInfo.pageParamTo(pi);
			limitSql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ")  A ";
			limitSql += "WHERE ROWNUM < " + (max + 1) + ")  WHERE RN >= " + (min + 1);
		} else {
			max = PageInfo.pageParamRows(pi);
			limitSql = sql;
		}
		return limitSql;
	}

}

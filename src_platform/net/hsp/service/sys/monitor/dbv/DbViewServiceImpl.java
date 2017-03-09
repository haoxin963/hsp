package net.hsp.service.sys.monitor.dbv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.dao.DynamicDataSource;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

@Service
@ServiceLogUtil(name = "数据表浏览器")
@Lazy(true)
public class DbViewServiceImpl extends BaseServiceImpl implements DbViewService {

	@MethodLogUtil(type="",value="查询数据表列表")
	@Override
	public Map<String, Object> queryTableList(FilterMap filter, PageInfo pi)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		
		Long total = null;
		if(DbInfo.MYSQL.equals(DbInfo.getDbType()))
		{
			total = queryTableList_mysql(items, filter, pi);
		}
		else if(DbInfo.ORACLE.equals(DbInfo.getDbType()))
		{
			total = queryTableList_oracle(items, filter, pi);
		}
	
		map.put("total", total);
		map.put("rows", items);
		return map;
	}
	
	private Long queryTableList_mysql(List<Map<String, Object>> items, FilterMap filter, PageInfo pi)
	{
		String searchTN = filter.get("tablename");
		boolean needSearch = !StringUtil.isEmpty( searchTN );
		String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA in (select database()) ";
		if( needSearch )
		{
			searchTN = "%" + searchTN + "%";
			sql += " AND TABLE_NAME like ? ";
		}
		
		Long total = this.baseDAO.queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", needSearch?new Object[]{searchTN} : new Object[]{}, Long.class);
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = this.baseDAO.getDataSource().getConnection();
			sql = PageInfo.pageSQL(sql, pi);
			st = conn.prepareStatement(sql);
			if( needSearch )
			{
				st.setString(1, searchTN);
			}
			rs =st.executeQuery();
			while(rs.next())
			{
				String TABLE_NAME = rs.getString("TABLE_NAME");
				String TABLE_COMMENT = rs.getString("TABLE_COMMENT");
				String ENGINE = rs.getString("ENGINE");
				String TABLE_ROWS = rs.getString("TABLE_ROWS");
				String VERSION = rs.getString("VERSION");
				String AUTO_INCREMENT = rs.getString("AUTO_INCREMENT");
				String CREATE_TIME = rs.getString("CREATE_TIME");
				
				String tinfo = "{"+ENGINE+","+VERSION+"} AI="+AUTO_INCREMENT;
				items.add(buildListItem(TABLE_NAME, TABLE_COMMENT, TABLE_ROWS, CREATE_TIME, tinfo));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(rs, null, conn);
		}
		return total;
	}
	
	private Long queryTableList_oracle(List<Map<String, Object>> items, FilterMap filter, PageInfo pi)
	{
		String searchTN = filter.get("tablename");
		boolean needSearch = !StringUtil.isEmpty( searchTN );
		//String sql = "select * from user_tab_comments";
		String sql = "select * from user_tab_comments ut"
				+ " inner join dba_tables dt on ut.TABLE_NAME = dt.TABLE_NAME";
		if( needSearch )
		{
			searchTN = "%" + searchTN + "%";
			sql += " WHERE lower(ut.TABLE_NAME) LIKE lower(?)";
		}
		
		Long total = this.baseDAO.queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", needSearch?new Object[]{searchTN} : new Object[]{}, Long.class);
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = this.baseDAO.getDataSource().getConnection();
			sql = PageInfo.pageSQL(sql, pi);
			st = conn.prepareStatement(sql);
			if( needSearch )
			{
				st.setString(1, searchTN);
			}
			rs =st.executeQuery();
			while(rs.next())
			{
				String TABLE_NAME = rs.getString("TABLE_NAME");
				String COMMENTS = rs.getString("COMMENTS");
				String TABLE_TYPE = rs.getString("TABLE_TYPE");
				String NUM_ROWS = rs.getString("NUM_ROWS");
				String LAST_ANALYZED = rs.getString("LAST_ANALYZED");
				
				String tinfo = "TABLE_TYPE:" + TABLE_TYPE;
				items.add(buildListItem(TABLE_NAME, COMMENTS, NUM_ROWS, LAST_ANALYZED, tinfo));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(rs, st, conn);
		}
		return total;
	}
	
	private Map<String, Object> buildListItem(String tname, String tcomment, String trows, String ttime, String tinfo)
	{
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("tname", tname);
		item.put("tcomment", tcomment);
		item.put("trows", trows);
		item.put("tinfo", tinfo);
		item.put("ttime", ttime); 
		return item;
	}
	
	
	@MethodLogUtil(type="",value="查询数据表结构")
	@Override
	public Map<String, Object> queryTableStruct(String tname)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		
		Long total = 0L;
		if(DbInfo.MYSQL.equals(DbInfo.getDbType()))
		{
			total = queryTableStruct_mysql(tname, items);
		}
		else if(DbInfo.ORACLE.equals(DbInfo.getDbType()))
		{
			total = queryTableStruct_oracle(tname, items);
		}
		
		map.put("total", total);
		map.put("rows", items);
		return map;
	}
	
	private Long queryTableStruct_mysql(String tname, List<Map<String, Object>> items)
	{
		String sql = " select * from information_schema.columns where table_schema = (select database()) and table_name = ?";
		
		Long total = this.baseDAO.queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", new Object[]{tname}, Long.class);
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = this.baseDAO.getDataSource().getConnection();
			st = conn.prepareStatement(sql);
			st.setString(1, tname);
			rs =st.executeQuery();
			while(rs.next())
			{
				String ORDINAL_POSITION = rs.getString("ORDINAL_POSITION");
				String COLUMN_NAME = rs.getString("COLUMN_NAME");
				String COLUMN_DEFAULT = rs.getString("COLUMN_DEFAULT");
				String DATA_TYPE = rs.getString("DATA_TYPE");
				String IS_NULLABLE = rs.getString("IS_NULLABLE");
				String COLUMN_TYPE = rs.getString("COLUMN_TYPE");//varchar(20)
				String COLUMN_COMMENT = rs.getString("COLUMN_COMMENT");
				String COLUMN_KEY = rs.getString("COLUMN_KEY");//pri
				String EXTRA = rs.getString("EXTRA");//auto_increment
				String CHARACTER_MAXIMUM_LENGTH = rs.getString("CHARACTER_MAXIMUM_LENGTH");
				String NUMERIC_PRECISION = rs.getString("NUMERIC_PRECISION");
				String note = "";
				List<String> noteEles = new ArrayList<String>(); 
				
				if(!StringUtil.isJsonNull(COLUMN_KEY))
				{
					noteEles.add("约束:"+COLUMN_KEY+" "+EXTRA);
				}
				if(!StringUtil.isJsonNull(COLUMN_DEFAULT))
				{
					noteEles.add("默认值:"+COLUMN_DEFAULT);
				}
				if(!StringUtil.isJsonNull(NUMERIC_PRECISION))
				{
					noteEles.add("数字精度："+NUMERIC_PRECISION);
				}
				note = StringUtils.join(noteEles, "<br>");
				items.add(buildStructItem(ORDINAL_POSITION, COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_COMMENT, note));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(rs, null, conn);
		}
		return total;
	}
	
	private Long queryTableStruct_oracle(String tname, List<Map<String, Object>> items)
	{
		String sql = "select tl.TABLE_NAME,tl.COLUMN_NAME,tl.DATA_TYPE,tl.DATA_LENGTH,"
				+ "tl.NULLABLE,tl.COLUMN_ID,tl.CHAR_LENGTH,tc.COMMENTS from user_tab_columns tl "
				+ " inner join user_col_comments tc on tl.COLUMN_NAME = tc.COLUMN_NAME "
				+ " where tl.TABLE_NAME=? and tc.TABLE_NAME=?";
		
		Long total = this.baseDAO.queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", new Object[]{tname,tname}, Long.class);
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = this.baseDAO.getDataSource().getConnection();
			st = conn.prepareStatement(sql);
			st.setString(1, tname);
			st.setString(2, tname);
			rs =st.executeQuery();
			while(rs.next())
			{
				String COLUMN_ID = rs.getString("COLUMN_ID");
				String COLUMN_NAME = rs.getString("COLUMN_NAME");
				String DATA_TYPE = rs.getString("DATA_TYPE");
				String DATA_LENGTH = rs.getString("DATA_LENGTH");
				String NULLABLE = rs.getString("NULLABLE");
				String CHAR_LENGTH = rs.getString("CHAR_LENGTH");
				String COMMENTS = rs.getString("COMMENTS");
				
				String datatype = DATA_TYPE + "(" + DATA_LENGTH + ")";
				String note = "CHAR_LENGTH="+CHAR_LENGTH;
				items.add(buildStructItem(COLUMN_ID, COLUMN_NAME, datatype, NULLABLE, COMMENTS, note));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(rs, st, conn);
		}
		return total;
	}
	
	private Map<String, Object> buildStructItem(String index, String colname, String datatype, 
			String nullable, String comments, String note)
	{
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("index", index);
		item.put("colname", colname);
		item.put("datatype", datatype);
		item.put("nullable", nullable);
		item.put("comments", comments);
		item.put("note", note);
		return item;
	}
	
	@MethodLogUtil(type="",value="查询数据表数据")
	@Override
	public Map<String, Object> queryTableData(String tname, PageInfo pi)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		
		int total = queryTableData_exe(tname, columns, items, pi);
		
		map.put("total", total);
		map.put("rows", items);
		map.put("columns", columns);
		return map;
	}
	
	private int queryTableData_exe(String tname, List<Map<String, Object>> columns, List<Map<String, Object>> items, PageInfo pi)
	{
		int total = 0;
		String countsql = "SELECT COUNT(0) as count FROM( select * from "+tname+" )A";
		String sql = " select * from "+tname;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = this.baseDAO.getDataSource().getConnection();
			
			st = conn.prepareStatement(countsql);
			rs =st.executeQuery();
			while(rs.next())
			{
				total = rs.getInt("count");
			}
			
			sql = PageInfo.pageSQL(sql, pi);
			
			st = conn.prepareStatement(sql);
			rs =st.executeQuery();
			int colNum = rs.getMetaData().getColumnCount();
			String[] CNS = new String[colNum];
			for(int i = 0; i < colNum; i++)
			{
				String colname = rs.getMetaData().getColumnName(i+1);
				columns.add(buildColumnItem(colname));
				CNS[i] = colname;
			}
			while(rs.next())
			{
				Map<String, Object> item = new HashMap<String, Object>();
				for(int i = 0; i < colNum; i++)
				{
					String cn = CNS[i];
					String cval = rs.getString(cn);
					item.put(cn, cval);
				}
				items.add(item);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close(rs, null, conn);
		}
		return total;
	}
	
	private Map<String, Object> buildColumnItem(String colname)
	{
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("field", colname);
		item.put("title", colname);
		//item.put("width", 100);
		item.put("align", "right");
		return item;
	}
	
	/** 关闭并释放资源 */
	public static void close(ResultSet rs, Statement ps, Connection con)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
			if (ps != null)
			{
				ps.close();
			}
			if (con != null && !con.getAutoCommit())
			{
				con.commit();
			}
			if (con != null && !con.isClosed())
			{
				con.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}

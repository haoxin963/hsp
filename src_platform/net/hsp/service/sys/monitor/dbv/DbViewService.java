package net.hsp.service.sys.monitor.dbv;

import java.util.Map;

import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

public interface DbViewService {
	
	/** 查询数据表列表 */
	Map<String, Object> queryTableList(FilterMap filter, PageInfo pi);
	
	/** 查询数据表结构 */
	Map<String, Object> queryTableStruct(String tname);
	
	/** 查询数据表数据 */
	Map<String, Object> queryTableData(String tname, PageInfo pi);
	
}

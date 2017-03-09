package net.hsp.service.sys.monitor.log;

import java.util.Map;

import net.hsp.web.util.PageInfo;

public interface SqlLogService {
	
	Map<String, Object> querySqlWarn(PageInfo pageInfo);
	
	Map<String, Object> querySqlInfo(PageInfo pageInfo);
}

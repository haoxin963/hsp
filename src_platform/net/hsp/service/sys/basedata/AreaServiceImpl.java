package net.hsp.service.sys.basedata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.basedata.Area;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 区域业务类
 * @author lk0182
 */
@Service
@ServiceLogUtil(name = "区域业务类")
@Lazy(true)
public class AreaServiceImpl extends BaseServiceImpl implements AreaService { 

 
	
	public Map query(Map param, PageInfo p) {
		String sql = "select areaName,areaId,parentId from pubmodule_area_tbl where 1=1 ";
 		String[] args = new String[2];
 		List list = new ArrayList();
 		if (param!=null) {
 			String areaId = (String)param .get("areaId"); 
 			if (StringUtils.isNotBlank(areaId)) {
 				sql +=" and parentId = ? "; 
 				list.add(areaId);
			}
 			String areaName = (String)param.get("areaName"); 
 			if (StringUtils.isNotBlank(areaName)) { 
 				sql +=" and areaName like ? "; 
 				list.add("%"+areaName+"%");
			}
		} 
		return this.findPageInfo(sql, list.toArray(), p); 
	}
	
	@Override
	public <T> T save(T entity) throws ServiceException {
		Area obj = (Area) entity; 
		String sql = "select  concat('000',max(areaid)+1) as areaid from pubmodule_area_tbl where areaid like ? and length(areaid) = ?";
		String areaId = this.getDAO().queryForObject(sql, String.class,obj.getParentId()+"%",obj.getParentId().length()+4);
		obj.setAreaId(areaId);
		return (T)super.save(obj);
	}


	@Override
	@MethodLogUtil(type="",value="区域树")
	public List buildTree(Map map, PageInfo p) {
		String id = (String)map.get("id");
		String sql =  "select areaid as id, areaname as name,parentId,case when haschild=1 then true else false end isParent from pubmodule_area_tbl where 1 = 1";
		if (StringUtils.isBlank(id)) {
			id = "00010001";
		}
		sql+=" and parentid =  ? ";
		return baseDAO.queryForList(sql,id);
	}
	
	
}


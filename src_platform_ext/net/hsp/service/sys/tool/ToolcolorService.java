package net.hsp.service.sys.tool;

import java.util.List;
import java.util.Map;

import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_toolcolor_tbl业务接口
 * @author lk0513
 */
public interface ToolcolorService extends BaseService  {
	
	 /**
	 * 多条件查询pubmodule_toolcolor_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);

	public Map<String,Object> find(Integer toolid,String userid,String styleid);
	
	public Map<String,Object>  findbyUsername(String username);
	
	public int delete(String userid,Integer toolid);
	
    public List<Map<String,Object>> list();
  
    public Map<String,Object> findbyUserid(String userid);
    
    public int insertusertool(String userid,String filepath);
		
    public int updateusertool(String userid,String filepath);
}
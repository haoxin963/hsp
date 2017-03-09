package net.hsp.service.sys.manage;

import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.entity.sys.manage.Station;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.service.BaseService;
import net.hsp.service.ServiceException;
import net.hsp.web.util.PageInfo;

/**
 * 站点业务接口
 * 
 * @author smart
 */
public interface StationService extends BaseService {

	public boolean enabled(String[] domains);

	public boolean disable(String[] domains);

	public int create(Station entity) throws ServiceException;

	public Map<String, Object> query(Map param, PageInfo pageInfo) throws ServiceException;

	public List query() throws ServiceException;

	List<Map<String, Object>> buildTree(Map map, PageInfo p);
	
	public int update(Station obj) throws ServiceException ;
	
	/**
	 * ----------------------站点菜单功能-----------------------------------------------------------------------------------------------------
	 */
	public Map queryFunction(Map param, PageInfo p);
	
	public Function addFunction(Function function, String custDomain);
	
	public Function insertFunction(Function function, String custDomain);
	
	public int modifyFunction(Function function, String custDomain);
	/**
	 * 检查所选节点是否包含下级
	 * @param ids
	 * @return 1.包含 0.不包含
	 */
	public int checkFuncHasChild(String[] ids, String custDomain);
	
	public int deleteFunction(Function obj, String custDomain);
	
	public int[] batchDeleteFunction(String[] funIds, String custDomain);
	
	public void deleteFunctionAndChildren(String funIds, String custDomain);
	
	public Function findFunById(Function obj, String custDomain);
	
	public List<Map<String, Object>> buildMenuTree(String custDomain);
	

	/**
	 * 单个调整菜单层级及顺序
	 * @param id 要调整的菜单ID
	 * @param type 类型 1.调整层级和顺序  0.调整当前级别的顺序
	 * @param parentId 父级节点ID.
	 * @param sortNo 顺序号
	 * @param sortList 需调整其他相关的节点的【ID及顺序号】 
	 * @return 1.操作成功 0.操作失败
	 */
	public int changeFunctionLevel(String id,String type,String parentId,String sortNo,String []sortList, String custDomain);

}
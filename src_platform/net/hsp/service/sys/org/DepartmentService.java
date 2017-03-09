package net.hsp.service.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.org.Department;
import net.hsp.service.BaseService;
import net.hsp.service.ServiceException;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

/**
 * pubmodule_department_tbl业务接口
 * @author lk0508
 */
public interface DepartmentService extends BaseService  {
	
	 /**
	 * 多条件查询pubmodule_department_tbl
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map map,PageInfo p);
	
	/**
	 * 部门基本树
	 * @return
	 */
	public List<Map<String,Object>> buildTree(Map map,PageInfo p);
	
	
	/**
	 * 部门用户树
	 * @return
	 */
	public List<Map<String,Object>> buildDeptUserTree(Map map,PageInfo p);
	
	
	/**
	 * 添加新部门的接口方法
	 * @param deparment 部门实体信息
	 * @return 包含主键的部门实体信息
	 */
	public Department addDepartment(Department department);
	
	/**
	 * 修改部门信息的接口方法
	 * @param department 部门实体信息
	 * @return 1.操作成功 0。操作失败
	 */
	public int modifyDepartment(Department department);
	
	/**
	 * 根据主键删除部门信息的接口方法(逻辑删除)
	 * @param id 部门主键
	 * @return 1.操作成功 0。操作失败
	 */
	public int delDepartmentById(String id);
	
	/**
	 * 根据主键批量删除部门信息的方法(逻辑删除)
	 * @param ids 部门主键数组
	 * @return 1.操作成功 0.操作失败
	 */
	public int batchDelDepartmentByIds(String[] ids);
	
	
	/**
	 * 根据部门ID获得所有的下级部门集合
	 * @param id 部门ID
	 * @return 
	 */
	public List<Map<String,Object>> getAllChildDepartmentById(String id);
	
	/**
	 * 单个调整部门层级及顺序
	 * @param id 要调整的部门ID
	 * @param type 类型 1.调整层级和顺序  0.调整当前级别的顺序
	 * @param parentId 父级节点ID.
	 * @param sortNo 顺序号
	 * @param sortList 需调整其他相关的部门的【ID及顺序号】 
	 * @return 1.操作成功 0.操作失败
	 */
	public int changeDepartmentLevel(String id,String type,String parentId,String []sortList);
	
	
	/**
	 * 得到正在保存数据的排序号
	 * @param post 
	 * @return
	 */
	public int getSavingDataSortNo(Department department);
	
	/**
	 * 更新其他关联数据的排序号（使序号连续）
	 * @param sortNo 序号节点
	 * @param isAdd 是否是增加数据. true:增加数据其序号+1  false:删除数据序号-1
	 */
	public void updateOthersSortNo(int sortNo,boolean isAdd);
	
	/**
	 * 得到正在删除的数据的行号
	 * @param id
	 * @return
	 */
	public int getDeletingDataSortNo(String id);
	
	/**
	 * 更新是否有子节点的数据（包含有子节点和没有子节点）
	 */
	public void updateHasChild();
	
	/**
	 * 根据部门编号获取得到部门信息
	 * @param code
	 * @return
	 */
	public Department getDeptByCode(String code);
	
	/**
	 * 根据上级部门ID得到所有直接下级部门
	 * @param parentId
	 * @return
	 */
	public List<Map<String,Object>> getChilrenDeptByParentId(String parentId);
	
	/**
	 * 检查所选部门是否包含下级
	 * @param ids
	 * @return 1.包含 0.不包含
	 */
	public int checkDeptHasChild(String[] ids);
	
	/**
	 * 查询部门
	 * @param filterMap
	 * @return
	 * @throws ServiceException
	 */
	public List<Department> queryDept(FilterMap filterMap) throws ServiceException;
	
}
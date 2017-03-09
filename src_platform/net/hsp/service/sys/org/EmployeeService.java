package net.hsp.service.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.org.Employee;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 员工信息业务接口
 * @author lk0508
 */
public interface EmployeeService extends BaseService  {
	
	 /**
	 * 多条件查询员工信息
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map map,PageInfo p);
	
	public Map doUserList(Map map,PageInfo p);

	/**
	 * 构造左侧树结构数据的方法
	 * @return
	 */
	public List<Map<String,Object>> buildTree(Map map,PageInfo p);
	
	/**
	 * 添加新员工的接口方法
	 * @param employee 员工实体信息
	 * @param postids 岗位ID数组（多个岗位以分号;分割，岗位id与是否主岗位间用逗号,分割）
	 * @return 包含主键的员工实体信息
	 */
	public Employee addEmployee(Employee employee,String[] postids);
	
	/**
	 * 修改员工信息的接口方法
	 * @param employee 员工实体信息
	 * @param postids 岗位ID数组（多个岗位以分号;分割，岗位id与是否主岗位间用逗号,分割）
	 * @return 1.操作成功 0。操作失败
	 */
	public int modifyEmployee(Employee employee,String[] postids);
	
	/**
	 * 根据主键删除员工信息的接口方法(逻辑删除)
	 * @param id 员工主键
	 * @return 1.操作成功 0。操作失败
	 */
	public int delEmployeeById(String id);
	
	/**
	 * 根据主键批量删除员工信息的方法(逻辑删除)
	 * @param ids 员工主键数组
	 * @return 1.操作成功 0.操作失败
	 */
	public int batchDelEmployeeByIds(String[] ids);	
	
	/**
	 * 得到正在保存数据的排序号
	 * @param post 
	 * @return
	 */
	public int getSavingDataSortNo(Employee employee);
	
	/**
	 * 更新其他关联数据的排序号（使序号连续）
	 * @param sortNo 序号节点
	 * @param isAdd 是否是增加数据. true:增加数据其序号+1  false:删除数据序号-1
	 */
	public void updateOthersSortNo(int sortNo,boolean isAdd);
	
	
	/**
	 * 根据员工id获取其其他岗信息
	 * @param empid 员工ID
	 * @return 其他岗位信息列表
	 */
	public List<Map<String,Object>> getOtherPostByEmpId(String empid);
	

	/**
	 * 根据员工id获取其主岗信息
	 * @param empid 员工ID
	 * @return 岗位信息
	 */
	public Map<String,Object> getMainPostByEmpId(String empid);
	 
	/**
	 * 得到正在删除的数据的行号
	 * @param id
	 * @return
	 */
	public int getDeletingDataSortNo(String id);
	
	/**
	 * 批量创建用户
	 * @param ids
	 * @param username
	 * @param pwd
	 * @return Map (包含两个值: [succlist 成功列表(List<Map<String,Object>)][faillist = 失败列表List<Map<String,Object>])succlist:<ul>
	 * <li>[key=empid]：员工ID
	 * <li>[key=empno]：员工工号
	 * <li>[key=empname]：员工姓名
	 * <li>[key=username]：生成的用户姓名 
	 * </ul><br/>faillist:<ul>
	 * <li>[key=empid]：员工ID
	 * <li>[key=empno]：员工工号
	 * <li>[key=empname]：员工姓名
	 * <li>[key=errmsg]：失败信息，失败原因 (1.用户已经生成 2.用户已经存在)
	 * </ul>
	 */
	public Map batchCreateUser(String[] ids,String username, String pwd);
	 
	/**
	 * 根据员工ID找到所有的上级（可能多个）
	 * @param empid
	 * @return List中的Map代表一整个employee实体数据的键值对信息
	 */
	public List<Map<String,Object>> getDirectorsByEmpId(String empid); 
	
	/**
	 * 根据岗位id 找到相应人员
	 * @param postId
	 * @return
	 */
	public List<Map<String,Object>> getEmpListByPostId(String postId);
	
	/**
	 * 根据用户id获得对应员工信息
	 * @param userid
	 * @return
	 */
	public Employee getEmpByUserId(String userid);
	
	/**
	 * 根据员工ID获得对应的员工信息
	 * @param empid
	 * @return
	 */
	public User getUserByEmpId(String empid);
	
	/**
	 * 检查员工是否有生成相应的用户信息
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> checkEmpIsCreatedUser(String[] ids);
	
	/**
	 * 手动关联用户
	 * @param UserId
	 * @param empid
	 * @return
	 */
	public int linkToUser(String userId,String empid);
}

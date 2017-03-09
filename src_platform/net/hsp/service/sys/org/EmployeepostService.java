package net.hsp.service.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.org.Employeepost;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 员工岗位信息业务接口
 * @author lk0508
 */
public interface EmployeepostService extends BaseService  {
	
	 /**
	 * 多条件查询员工岗位信息
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map map,PageInfo p);
	
	/**
	 * 添加新员工岗位信息的接口方法
	 * @param employeepost 员工岗位信息实体信息
	 * @return 包含主键的员工岗位信息实体信息
	 */
	public Employeepost addEmployeepost(Employeepost employeepost);
	
	/**
	 * 修改员工岗位信息信息的接口方法
	 * @param employeepost 员工岗位信息实体信息
	 * @return 1.操作成功 0。操作失败
	 */
	public int modifyEmployeepost(Employeepost employeepost);
	
	/**
	 * 根据主键删除员工岗位信息信息的接口方法(逻辑删除)
	 * @param id 员工岗位信息主键
	 * @return 1.操作成功 0。操作失败
	 */
	public int delEmployeepostById(String id);
	
	/**
	 * 根据主键批量删除员工岗位信息信息的方法(逻辑删除)
	 * @param ids 员工岗位信息主键数组
	 * @return 1.操作成功 0.操作失败
	 */
	public int batchEmployeepostByIds(String[] ids);
	

}
package net.hsp.service.sys.rbac;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.entity.sys.rbac.Role;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.BaseService;
import net.hsp.service.ServiceException;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

/**
 * 用户表业务接口
 * @author lk0508
 */
public interface UserService extends BaseService  {
	
	/**
	 * 多条件查询用户表
	 */
	public Map queryUser(User user,FilterMap map,PageInfo p);
	
	/**
	 * 添加新用户的接口方法
	 * @param user 封装新用户数据的实体Bean
	 * @return 成功:返回User数据对象，包含主键ID | 失败：null
	 */
	public User addUser(User user);
			
	/**
	 * 修改用户信息的接口方法
	 * @param user  封装用户修改后数据的实体Bean
	 * @return 成功:返回User数据对象，包含主键ID | 失败：null
	 */
	public int modifyUser(User user);
	
	/**
	 * 启用/禁用 用户的接口方法
	 * @param ids 用户ID的数组
	 * @param status 用户状态码 1：启用  0:禁用
	 * @return 1:操作成功 0:操作失败
	 */
	public int enableUser(String[] ids,String[] status);	
	
	/**
	 * 初始化用户密码的接口方法
	 * @param user 封装用户数据的实体
	 * @return 1:操作成功 0:操作失败
	 */
	public int resetUserPwd(User user);
	
	/**
	 * 修改用户密码的接口方法
	 * @param user 封装用户数据的实体
	 * @return 1:操作成功 0:操作失败
	 */
	public int modifyUserPwd(User user);
	
	/**
	 * 根据主键ID得到用户的接口方法
	 * @param id 主键ID
	 * @return 返回用户数据实体
	 */
	public User getUserById(String id);
	
	/**
	 * 根据用户主键ID得到相关的角色集合
	 * @param id
	 * @return List<Role>
	 */
	public List<Role> getRoleByUserId(String id);
	
	/**
	 * 根据主键ID删除(逻辑删除)用户的接口方法
	 * @param id 主键ID
	 * @return 1:操作成功 0:操作失败
	 */
	public int delUserById(String id);
	
	/**
	 * 批量删除(逻辑删除)用户的接口方法
	 * @param ids 用户ID数组 
	 * @return 1:操作成功 0:操作失败
	 */
	public int batchDelUserByIds(String[] ids);
	 
	/**
	 * 给用户授予多个角色的方法
	 * @param userId 用户ID
	 * @param roleIds 角色ID数组
	 * @return 1:操作成功 0:操作失败
	 */
	public int grantRole(String userId,String[] roleIds);
	
	/**
	 * 用户登录鉴权的接口方法(无验证码)
	 * @param user 仅需属性【<b>userName|password</b>】，且为必填
	 * @return Map (包含两个值: [key=retCode 认证结果返回码][retUser = User对象])<ul>	
	  <li>[retCode=1]:登录成功 | retUser为包含登录用户相关信息的User对象
	  <li>[retCode=2]:用户名不存在 retUser=null
	  <li>[retCode=3]:密码错误  retUser=null
	  <li>[retCode=4]:用户已经被禁用  retUser=null
	  <li>[retCode=0]:登录失败(系统原因) retUser=null
	  </ul>
	 */	
	public Map login(User user);
	
	/**
	 * @param user 仅需属性【<b>userName</b>】，且为必填
	 * @return Map (包含两个值: [key=retCode 认证结果返回码][retUser = User对象])<ul>	
	  <li>[retCode=1]:登录成功 | retUser为包含登录用户相关信息的User对象
	  <li>[retCode=2]:用户名不存在 retUser=null
	  <li>[retCode=3]:密码错误  retUser=null
	  <li>[retCode=4]:用户已经被禁用  retUser=null
	  <li>[retCode=0]:登录失败(系统原因) retUser=null
	  </ul>
	 * @return
	 */
	public Map loginByUserName(User user);
	
 
	/**
	 * 根据用户id返回用户对象
	 * @param id
	 * @return Map
	 */
	public Map findById2Map(Serializable id);

	/**
	 * 取出当前用户的所在部门
	 * @param id
	 * @return
	 */
	public Map getDeptByUserId(Long id);
	
	/**
	 * 取出当前用户的直接上级
	 * @param id
	 * @return
	 */
	public User getDirectorByUserId(Long id);
	
	/**
	 * 取得所有用户李列表
	 * @return
	 */
	public List<User> getAllUser();	
	
	/**
	 * 根据用户ID或许当钱用户上级列表
	 * @param id
	 * @return List中的Map为User实体数据的键值对
	 */
	public List<User> getDirectorsByUserId(Long id);
	

	/**
	 * 根据用户ID得到用户拥有的所有权限菜单的链接地址（包括内联地址）
	 * @param userid
	 * @return
	 */
	public Set<String> getFunctionSetByUserId(String userid);
	

	/**
	 * 用户权限视图
	 * @param roleIds
	 * @return
	 */
	public List<Map<String, Object>> buildMenuTreeByRoleId(String roleIds);
	
	/**
	 * 根据指定角色ID获取相关用户集合
	 * @param rid 角色ID
	 * @return 用户集合
	 * */
	public Map getUsersByRoleId(String rid,PageInfo p);
	
	/**
	 * 根据用户名查找相关角色
	 * @param userName
	 * @return
	 */
	public List<Role> getRoleByUserName(String userName);
	
	/**
	 * 根据角色查找对应用户
	 * @param roleCode
	 * @return
	 */
	public List<User> getUsersByRoleCode(String... roleCode);
	
	/**
	 * 获取所有url
	 * @return
	 */
	public Set<String> getAllFunctionSet();
	
	/**
	 * 查询用户
	 * @param filterMap
	 * @return
	 * @throws ServiceException
	 */
	public List<User> queryUserII(FilterMap filterMap) throws ServiceException;
}
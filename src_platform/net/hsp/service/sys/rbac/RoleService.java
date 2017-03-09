package net.hsp.service.sys.rbac;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.rbac.Role;
import net.hsp.service.BaseService;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

/**
 * 角色表业务接口
 * 
 * @author lk0508
 */
public interface RoleService extends BaseService {

	/**
	 * 多条件查询角色表
	 */
	public Map queryRole(Role role, FilterMap map, PageInfo p);

	/**
	 * 获取整个有效的角色列表
	 * 
	 * @return 角色信息集合
	 */
	public List<Role> getRoleList();

	/**
	 * 添加新角色的接口方法
	 * 
	 * @param role
	 *            角色实体对象
	 * @return 成功:返回新角色实体包括主键ID | 失败:null
	 */
	public Role addRole(Role role);

	/**
	 * 修改角色信息的方法
	 * 
	 * @param role
	 *            角色信息实体
	 * @return 1:操作成功 0:操作失败
	 */
	public int modifyRole(Role role);

	/**
	 * 根据ID得到用户角色的方法
	 * 
	 * @param id
	 *            主键
	 * @return 角色实体
	 */
	public Role getRoleById(String id);

	/**
	 * 根据主键ID删除(逻辑删除)角色的接口方法
	 * 
	 * @param id
	 *            角色ID
	 * @return 1:操作成功 0:操作失败
	 */
	public int delRoleById(String id);

	/**
	 * 批量删除角色信息的方法
	 * 
	 * @param ids
	 *            角色ID数组
	 * @return 1:操作成功 0:操作失败
	 */
	public int batchDelRoleByIds(String[] ids);

	/**
	 * 给角色授予权限的接口方法
	 * 
	 * @param roleId
	 *            角色ID
	 * @param funcId
	 *            权限ID数组
	 * @return
	 */
	public int grantFunction(String roleId, String[] funcIds);

	/**
	 * 角色关联某些用户的接口方法
	 * 
	 * @param roleId
	 *            角色ID
	 * @param userIds
	 *            用户ID数组
	 * @return 1:操作成功 0:操作失败
	 */
	public int addRefUser(String roleId, String[] userIds);

	/**
	 * 根据用户ID查询某个用户已经关联的角色列表
	 * 
	 * @param id
	 * @return
	 */
	public List<Role> getRoleListByUserId(String id);

	/**
	 * 将多个角色授权给某个用户
	 * 
	 * @param userid
	 *            某个用户id
	 * @param rids
	 *            角色ID的数组
	 * @return 1:操作成功 0:操作失败
	 */
	public int grantRoles2User(String userid, String[] rids);

	/**
	 * 根据角色代号得到角色信息
	 * 
	 * @param code
	 * @return
	 */
	public Role getRoleByCode(String code);

	/**
	 * 根据关键字模糊搜索得到角色列表
	 * 
	 * @param keyword
	 * @return
	 */
	public List<Role> getRolesByKeyword(String keyword);

	/**
	 * 按角色、用户构建树
	 * 
	 * @param map
	 * @param p
	 * @return
	 */
	public List<Map<String, Object>> buildRoleUserTree(Map map, PageInfo p);

	/**
	 * 根据角色Id字段查询人员列表
	 * 
	 * @param projid
	 * @return
	 */
	public abstract Map queryUsersByRoleId(String roleId, Map param, PageInfo p);
	
	public List<Map<String,Object>> queryUsersByRoleId(String roleId, Map param);

	/** 将一个角色赋予给用户 */
	public int giveRoleToUser(String roleId, String userId);

	/** 将一个角色从用户身上剥离 */
	public int removeRoleFromUser(String roleId, String userId);

	/** 更新一部分用户身上的某一特定角色 */
	public int updateRoleInSomeUsers(String roleId, String[] pageUids, String[] selectUids);
	
	/**
	 * 多个用户分配权限
	 * @param rid
	 * @param userids
	 * @return
	 */
	public int grantUsers2Role(String rid, String[] userids);
}
package net.hsp.service.sys.rbac;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.entity.sys.rbac.Function;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 权限表业务接口
 * @author lk0508
 */
public interface FunctionService extends BaseService  {
	
	 /**
	 * 多条件查询权限表
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map param,PageInfo p);
	
	/**
	 * 构造左侧树结构数据的方法
	 * @return
	 */
	public List<Map<String,Object>> buildTree(Map map,PageInfo p);
	
	
	/**
	 * 获取整个功能权限列表
	 * @return 功能权限集合
	 */
	public List<Function> getFunctionList();
	
	/**
	 * 添加新权限的接口方法
	 * @param role 权限实体对象
	 * @return 成功:返回新权限实体包括主键ID | 失败:null
	 */
	public Function addFunction(Function function);
	
	/**
	 * 修改权限信息的方法
	 * @param role 权限信息实体
	 * @return 1:操作成功 0:操作失败
	 */
	public int modifyFunction(Function function);
	
	/**
	 * 根据ID得到用户权限的方法
	 * @param id 主键
	 * @return 权限实体
	 */
	public Function getFunctionById(String id);
	
	/**
	 * 根据主键ID删除(逻辑删除)权限的接口方法
	 * @param id 权限ID
	 * @return  1:操作成功 0:操作失败
	 */
	public int delFunctionById(String id);
	
	/**
	 * 根据角色Id查询关联的权限列表的接口方法
	 * @param rid 角色ID
	 * @return 权限列表集合
	 */
	public List<Function> getFuncListByRoleId(String rid);
	
		
	/**
	 * 将一个或多个功能模块授权给某个角色
	 * @param roleid 角色ID
	 * @param fids 权限ID的数组
	 * @return  1:操作成功 0:操作失败
	 */
	public int grantFuncs2Role(String roleid,String[] fids);
	
	/**
	 * 根据用户ID得到相应权限的功能菜单数据
	 * 
	 * @param userid
	 *            用户ID
	 * @return Map [ <ul>	
		  <li> funcId:String 功能模块ID 
		  <li> funcName:String 功能模块名称
		  <li> parentId:String 上级模块ID 
		  <li> linkAddr:String 功能链接地址
		  <li> children:List<Map<String,Object>> 包含的子集菜单嵌套map结构
		  <li> buttonId:String 按钮ID 
		  <li> sortNo:String  排序号
		  <li> picAddr:String 图片地址
		  <li> tag:String 按钮/链接 b按钮 f功能链接
	  </ul>  ]
	 *        
	 */
	public List<Map<String, Object>> getMenuDataByUserId(String userid,boolean isAdmin,boolean genBtn);
	
	/**
	 * 根据功能菜单数据生成html页面字符串
	 * @param data List<Map<String,Object>>结构的数据，参见接口方法getMenuDataByUserId
	 * @basePath 系统上下文相对根路径
	 * @return 返回拼装之后的html页面菜单字符串
	 */
	public String generateHtmlByMenuData(List<Map<String, Object>> data,String basePath);
	
	/**
	 * 生成对应用户菜单页面文件的接口
	 * @param userid 用户ID
	 * @param custId 站点名称
	 * @param basePath 系统上下文相对根路径
	 * @return 1成功 0 失败
	 */
	public int generateMenuHtmlFile(String userId,String custId,String basePath);
	
	/**
	 * 检查用户相应的菜单文件是否已经生成
	 * @param userId 用户ID
	 * @param custId 站点名
	 * @return true.存在 false.不存在
	 */
	public boolean checkUserMenuFileExist(String userId,String custId);
	
	/**
	 * 清空某个站点所有用户的菜单文件 
	 * @param custId 站点名
	 */
	public void clearAllMenuFile(String custId);
	
	/**
	 * 清理某个用户的菜单文件
	 * @param custId 站点名
	 * @param userId 用户Id
	 */
	public void clearUserMenuFile(String custId,String userId);
	
	
	/**
	 * 根据节点ID获得所有的下级节点集合
	 * @param id 部门ID
	 * @return 
	 */
	public List<Map<String,Object>> getAllChildFunctionById(String functionId);
	
	
	/**
	 * 单个调整菜单层级及顺序
	 * @param id 要调整的菜单ID
	 * @param type 类型 1.调整层级和顺序  0.调整当前级别的顺序
	 * @param parentId 父级节点ID.
	 * @param sortNo 顺序号
	 * @param sortList 需调整其他相关的节点的【ID及顺序号】 
	 * @return 1.操作成功 0.操作失败
	 */
	public int changeFunctionLevel(String id,String type,String parentId,String sortNo,String []sortList);
	
	 
	/**
	 * 返回当前用户指定URL的可用的按钮集合
	 * @param userId 
	 * @param parentUrl 根据此URL查询下级按钮
	 * @return Map [ <ul>	
		  <li> id:String 按钮ID
		  <li> id:String 按钮小图标 icon  
	  </ul>  ]
	 */
	public List<Map<String,Object>> getButton(boolean isAdmin,String userId,String parentUrl);
	
	/**
	 * 获取一级菜单的数据
	 * @return
	 */
	public List<Map<String,Object>> getFirstLevelMenu(String userId,boolean isAdmin);
	
	/**
	 * 根据菜单id获取下属所有子集菜单
	 * @param parentId
	 * @return
	 */
	public List<Map<String,Object>> getAllChildrenMenuByParentId(String parentId);
	
	/**
	 * 检查所选节点是否包含下级
	 * @param ids
	 * @return 1.包含 0.不包含
	 */
	public int checkFuncHasChild(String[] ids);
	
	
	
	/**
	 * 检查用户相应的菜单json数据文件是否已经生成
	 * @param userId 用户ID
	 * @param custId 站点名
	 * @return true.存在 false.不存在
	 */
	public boolean checkUserMenuJsonFileExist(String userId,String custId);
	
	/**
	 * 生成对应用户菜单json数据文件的接口
	 * @param userid 用户ID
	 * @param custId 站点名称
	 * @param basePath 系统上下文相对根路径
	 * @return 1成功 0 失败
	 */
	public int generateMenuJsonFile(String userId,String custId,String basePath);
	
	
	/**
	 * 按用户生成获取平面数据 
	 * @param userid
	 * @param isAdmin
	 * @param genBtn
	 * @return
	 */
	public List<Function> listMenuDataByUserId(String userid,boolean isAdmin,boolean genBtn);
	
	
	public Map<String,Integer> getPageButton();
	
}
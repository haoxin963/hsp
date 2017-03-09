package net.hsp.service.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.org.Post;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 岗位信息业务接口
 * @author lk0508
 */
public interface PostService extends BaseService  {
	
	 /**
	 * 多条件查询岗位信息
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map map,PageInfo p);
	
	/**
	 * 构造左侧树结构数据的方法
	 * @return
	 */
	public List<Map<String,Object>> buildTree(Map map,PageInfo p);
	
	/**
	 * 添加新岗位的接口方法
	 * @param post 岗位实体信息
	 * @return 包含主键的岗位实体信息
	 */
	public Post addPost(Post post);
	
	/**
	 * 修改岗位信息的接口方法
	 * @param post 岗位实体信息
	 * @return 1.操作成功 0。操作失败
	 */
	public int modifyPost(Post post);
	
	/**
	 * 根据主键删除岗位信息的接口方法(逻辑删除)
	 * @param id 岗位主键
	 * @return 1.操作成功 0。操作失败
	 */
	public int delPostById(String id);
	
	/**
	 * 根据主键批量删除岗位信息的方法(逻辑删除)
	 * @param ids 岗位主键数组
	 * @return 1.操作成功 0.操作失败
	 */
	public int batchDelPostByIds(String[] ids);
	
	/**
	 * 得到正在保存数据的排序号
	 * @param post 
	 * @return
	 */
	public int getSavingDataSortNo(Post post);
	
	/**
	 * 更新其他关联数据的排序号（使序号连续）
	 * @param sortNo 序号节点
	 * @param isAdd 是否是增加数据. true:增加数据其序号+1  false:删除数据序号-1
	 */
	public void updateOthersSortNo(int sortNo,boolean isAdd);
	
	/**
	 * 更新是否有子节点的数据（包含有子节点和没有子节点）
	 */
	public void updateHasChild();
	
	/**
	 * 得到正在删除的数据的行号
	 * @param id
	 * @return
	 */
	public int getDeletingDataSortNo(String id);
	
	
	/**
	 * 单个调整岗位的层级及顺序
	 * @param id 要调整的岗位ID
	 * @param type 类型 0.不调整层级 1.需要调整层级
	 * @param parentId 新的父级节点ID.	
	 * @param sortList 需调整其他相关的岗位的【ID及顺序号】 
	 * @return 1.操作成功 0.操作失败
	 */
	public int changePostLevel(String id,String type,String parentId,String []sortList);
	
	/**
	 * 根据当前岗位ID得到上级岗位信息
	 * @param postId
	 * @return 若Map为空（Null）则当前岗位为顶层
	 */
	public Map<String,Object> getParentPostById(String postId);
	
	/**
	 * 根据岗位编码得到岗位信息
	 * @param code
	 * @return
	 */
	public Post getPostByCode(String code);
	
	/**
	 * 根据部门ID删除对应的岗位信息
	 * @param deptIds
	 * @return
	 */
	public int delPostByDeptId(String[] deptIds);
	
	/**
	 * 根据部门ID得到部门负责岗位信息
	 * @param deptId
	 * @return
	 */
	public Post getDeptHeadPostByDeptId(String deptId);
}
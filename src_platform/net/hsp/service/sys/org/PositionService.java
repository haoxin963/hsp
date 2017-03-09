package net.hsp.service.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.org.Position;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 职位信息业务接口
 * @author lk0508
 */
public interface PositionService extends BaseService  {
	
	 /**
	 * 多条件查询职位信息
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map query(Map map,PageInfo p);
	
	
	/**
	 * 添加新职位的接口方法
	 * @param position 职位实体信息
	 * @return 包含主键的职位实体信息
	 */
	public Position addPosition(Position position);
	
	/**
	 * 修改职位信息的接口方法
	 * @param position 职位实体信息
	 * @return 1.操作成功 0。操作失败
	 */
	public int modifyPosition(Position position);
	
	/**
	 * 根据主键删除职位信息的接口方法(逻辑删除)
	 * @param id 职位主键
	 * @return 1.操作成功 0。操作失败
	 */
	public int delPositionById(String id);
	
	/**
	 * 根据主键批量删除职位信息的方法(逻辑删除)
	 * @param ids 职位主键数组
	 * @return 1.操作成功 0.操作失败
	 */
	public int batchDelPositionByIds(String[] ids);
	
	
	/**
	 * 得到整个职位列表
	 * @return
	 */
	public List<Position> getAllPosition();
	
	/**
	 * 根据职位编号得到职位
	 * @param positionNo
	 * @return
	 */	
	public Position getPositionByNo(String positionNo);
	
}
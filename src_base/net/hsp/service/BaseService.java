package net.hsp.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

public interface BaseService {
	
	/**
	 * 持久化实体 仅持久化属性值不为空的字段
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract <T> T save(T entity) throws ServiceException;

	public abstract Object batchSave(List entitys) throws ServiceException;

	public abstract int delete(Object entity) throws ServiceException;
	
	/**
	 * 按deltag=1 字段逻辑删除
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract int deleteTag(Object entity) throws ServiceException;

	public abstract List batchDelete(List entitys) throws ServiceException;
	
	public abstract int[] batchDelete(Class entity,String[] pks) throws ServiceException;
	
	/**
	 * 按deltag=1 字段逻辑批量删除
	 * @param entity
	 * @param pks
	 * @return
	 * @throws ServiceException
	 */
	public abstract int[] batchDeleteTag(Class entity,String[] pks) throws ServiceException;

	/**
	 * 更新实体 当属性值为空时也将对应字段设置为NULL
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract int update(Object entity) throws ServiceException;
	
	/**
	 * 更新实体 当属性值为空时跳过字段
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract int update2(Object entity) throws ServiceException;
	
	/**
	 * 按实体更新，当属性值为空时则跳过此字段
	 * @param entity 实体
	 * @param columns 强制置空的字段
	 * @return
	 * @throws ServiceException
	 */
	public abstract int update2(Object entity,List<String> columns) throws ServiceException;

	public abstract List batchUpdate(List entitys) throws ServiceException;

	/**
	 * 根据给定的实体类型及id值查找返回实体,没有则返回null
	 * @param <T>
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract  <T> T findById(T entity) throws ServiceException;

	/**
	 * 基于sql分页，有条件，数据条目以map封装
	 * @param sql
	 * @param args
	 * @param p
	 * @return
	 * @throws ServiceException
	 */
	public abstract Map findPageInfo(String sql, Object[] args, PageInfo p) throws ServiceException;
	
	
	/**
	 * 基于sql分页，有条件，数据条目以指定实体封装
	 * @param entity
	 * @param p
	 * @return
	 */
	public abstract Map findPageInfo(String sql, Object[] args, Class c,PageInfo p) throws ServiceException ;
		 
	
	/**
	 * 基于实体分页，无条件，以Map封装
	 * @param entity
	 * @param p
	 * @return
	 */
	public abstract Map findPageInfo2Map(Class entity, PageInfo p)throws ServiceException;
	

	/**
	 * 基于实体分页，无条件，以指定类封装
	 * @param entity
	 * @param p
	 * @return
	 */
	public abstract Map findPageInfo2Entity(Class entity, PageInfo p)throws ServiceException;
	
 
	/**
	 * 基于sql查询，有条件,以指定类型封装
	 * @param sql
	 * @param args
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract List find(String sql, Object[] args,Class entity) throws ServiceException;
	
	/**
	 * 基于sql查询，有条件,以List<Map<String,?>>类型封装
	 * @param sql
	 * @param args
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<Map<String,Object>> find(String sql, Object[] args) throws ServiceException;


	
	/**
	 * 基于entityClass执行对应查询，无条件，以 List<Map<String, Object>>封装
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract List<Map<String, Object>> findAll(Class entity) throws ServiceException; 

	
	/**
	 * 基于entityClass执行对应查询，无条件,以指定实体封装
	 * @param entity
	 * @return
	 * @throws ServiceException
	 */
	public abstract <T> List<T> findAll2Entity(Class<T> entity) throws ServiceException;

	/**
	 * 以指定实体class及ID检索
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return 对应实体
	 * @throws ServiceException
	 */
	public <T> T findById(Class<T> entityClass, Serializable id) throws ServiceException;

	/**
	 *  以指定实体class及ID检索
	 * @param entityClass
	 * @param id
	 * @return Map<列名,object>
	 * @throws ServiceException
	 */
	public Map findById2Map(Class entityClass, Serializable id) throws ServiceException;

	public Map syncValidate(FilterMap map); 
	
	public List autocomplete(FilterMap map);

	/**
	 * 按属性精确查找
	 * @param <T>
	 * @param obj
	 * @param property
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> findByProperty(T obj,String property) throws ServiceException; 
	
	/**
	 * 按属性模糊查找  str%
	 * @param <T>
	 * @param obj
	 * @param property
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> findByPropertyMatchStart(T obj,String property) throws ServiceException;
	
	/**
	 * 按属性模糊查找  %str
	 * @param <T>
	 * @param obj
	 * @param property
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> findByPropertyMatchEnd(T obj,String property) throws ServiceException;
	
	/**
	 * 按属性模糊查找  %str%
	 * @param <T>
	 * @param obj
	 * @param property
	 * @return
	 * @throws ServiceException
	 */
	public <T> List<T> findByPropertyMatchAnywhere(T obj,String property) throws ServiceException;
	
}
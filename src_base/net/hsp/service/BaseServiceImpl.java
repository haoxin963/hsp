package net.hsp.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.hsp.common.ReflectionUtils;
import net.hsp.dao.jdbc.BaseDAO;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.dao.jdbc.MatchMode;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;
import net.hsp.web.util.SpringCtx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


public class BaseServiceImpl implements BaseService  {

	private static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired
	@Qualifier("baseDAO")
	protected BaseDAO baseDAO;

	public BaseDAO getDAO(){
		return baseDAO;
	}
	
	
	public BaseDAO getDAO(String custId){
		BaseDAO dao = new BaseDAO(); 
		dao.setDataSource((DataSource) SpringCtx.getBean("ds_"+custId)); 
		//DynamicDataSource.setCustId(custId);
		return dao;
	}
	
	@Override
	public <T> T save(T entity) throws ServiceException { 
		Object pk = getDAO().save(entity); 
		String id = getDAO().getPk(entity.getClass()).keySet().toArray()[0]+"";
		String type = ReflectionUtils.getAccessibleField(entity, id).getType().getName();
		if ("java.lang.Integer".equals(type) || "int".equals(type) ) {
			pk = Integer.parseInt(pk.toString());
		}else if("java.lang.Long".equals(type) || "long".equals(type)){
			pk = Long.parseLong(pk.toString());
		}
		ReflectionUtils.setFieldValue(entity, id, pk); 
		return entity;
	}
	
	@Override
	public Object batchSave(List entitys) throws ServiceException { 
		for (int i = 0; i < entitys.size(); i++) {
			Object entity = entitys.get(i); 
			Object pk = getDAO().save(entity); 
			String id = getDAO().getPk(entity.getClass()).keySet().toArray()[0]+"";
			String type = ReflectionUtils.getAccessibleField(entity, id).getType().getName();
			if ("java.lang.Integer".equals(type) || "int".equals(type) ) {
				pk = Integer.parseInt(pk.toString());
			}else if("java.lang.Long".equals(type) || "long".equals(type)){
				pk = Long.parseLong(pk.toString());
			}
			ReflectionUtils.setFieldValue(entity, id, pk); 
		}
		return entitys;
	}

 
	@Override
	public int delete(Object entity) throws ServiceException { 
		return getDAO().delete(entity);
	}
	
	
	@Override
	public List batchDelete(List entitys) throws ServiceException { 
		List list = new ArrayList();
		for (int i = 0; i < entitys.size(); i++) {
			list.add(getDAO().delete(entitys.get(i)));
		}
		return list;
	}
	
	
	@Override
	public int update(Object entity) throws ServiceException { 
		return getDAO().update(entity);
	}
	
	@Override
	public List batchUpdate(List entitys) throws ServiceException { 
		List list = new ArrayList();
		for (int i = 0; i < entitys.size(); i++) {
			list.add(getDAO().update(entitys.get(i)));
		}
		return list;
	}
	
	@Override
	public <T> T findById(T entity) throws ServiceException {
		return getDAO().findById(entity); 
	}
	
	@Override
	public <T> T findById(Class<T> entityClass,Serializable id) throws ServiceException {
		return getDAO().findById(entityClass,id);
	}
 
	@Override
	public Map findById2Map(Class entityClass,Serializable id) throws ServiceException {
		
		return getDAO().findById2Map(entityClass,id);
	}
	
	@Override
	public List find(String sql, Object[] args, Class entity) throws ServiceException { 
		return getDAO().query(sql,new BeanPropertyRowMapper(entity),args); 
	}

	@Override
	public List<Map<String,Object>> find(String sql, Object[] args) throws ServiceException {
		return getDAO().queryForList(sql,args); 
	}

	@Override
	public Map findPageInfo(String sql, Object[] args, PageInfo p) throws ServiceException {
		int min = PageInfo.pageParamFrom(p);
		int max = 0;
		String dbType = DbInfo.getDbType();
		if( DbInfo.MYSQL.equals(dbType) )
		{
			max = PageInfo.pageParamRows(p);
		}
		else if( DbInfo.ORACLE.equals(dbType) )
		{
			max = PageInfo.pageParamTo(p);
		}
		else
		{
			max = PageInfo.pageParamRows(p);
		}
		return getDAO().queryBySql2Map(sql, args, min, max);
	}

	@Override
	public Map findPageInfo(String sql, Object[] args, Class c,PageInfo p) throws ServiceException {
		return getDAO().queryBySql2Entity(sql, args, c, p.getRows()*(p.getPage()-1), p.getRows());
	}
	
	@Override
	public Map findPageInfo2Map(Class entity, PageInfo p) {
		String sql = "SELECT  * FROM " + getDAO().getTableName(entity.getClass());
		return getDAO().queryBySql2Map(sql, null,p.getRows()*(p.getPage()-1), p.getRows()); 
	}

	
	@Override
	public Map findPageInfo2Entity(Class entity, PageInfo p) {
		String sql = "SELECT  * FROM " + getDAO().getTableName(entity);
		return getDAO().queryBySql2Entity(sql, null,entity,p.getRows()*(p.getPage()-1), p.getRows()); 
	}
	
	
	@Override
	public List<Map<String, Object>> findAll(Class entity) throws ServiceException {
		String sql = "SELECT  * FROM " + getDAO().getTableName(entity);
		System.out.println(sql);
		return getDAO().queryForList(sql);
	}
  
	@Override
	public <T> List<T> findAll2Entity(Class<T> entity) throws ServiceException {
		String sql = "SELECT  * FROM " + getDAO().getTableName(entity); 
		return getDAO().query(sql, new BeanPropertyRowMapper(entity));
	}

 
	public Map execute(Map param, PageInfo p) throws ServiceException {
		
		 return null;
	}

	@Override
	public int[] batchDelete(Class cls, String[] pks) throws ServiceException {
		return getDAO().batchDelete(cls, pks);
	}

	@Override
	public Map syncValidate(FilterMap map) {
		String tableName = null;
		try {
			 tableName =  getDAO().getTableName(Class.forName(map.getFilter().get("entity")));
			 tableName =  tableName.replaceAll("'", "");
		} catch (Exception e) {
			return null;
		} 
		String fields = map.getFilter().get("field");
		String q = map.getFilter().get("q");
		String sql = "select count(*) from "+tableName+" where "+fields+" = '"+q+"'";
		int total = getDAO().queryForInt(sql);
		Map m = new HashMap();
		m.put("result", total==0);
		return m;
	}
	
	@Override
	public List autocomplete(FilterMap map) { 
		String tableName = null;
		try {
			 tableName =  getDAO().getTableName(Class.forName(map.getFilter().get("entity")));
			 tableName = tableName.replaceAll("'", "");
		} catch (Exception e) {
			return null;
		} 
		String whereField = map.getFilter().get("whereField");
		String fields = map.getFilter().get("field");
		String q = map.getFilter().get("q");
		String sql = "select "+fields+" from "+tableName+" where "+whereField+" like '%"+q+"%' limit 0,50";
		return getDAO().queryForList(sql);  
	}

	@Override
	public int update2(Object entity) throws ServiceException { 
		return getDAO().update2(entity);
	}
	
	@Override
	public int update2(Object entity,List<String> columns) throws ServiceException { 
		return getDAO().update2(entity,columns);
	}
	
	@Override
	public <T> List<T> findByProperty(T obj,String property) throws ServiceException {
		return getDAO().findByProperty(obj, property,MatchMode.EXACT);
	}
	@Override
	public <T> List<T> findByPropertyMatchStart(T obj,String property) throws ServiceException {
		return getDAO().findByProperty(obj, property,MatchMode.START);
	}
	@Override
	public <T> List<T> findByPropertyMatchEnd(T obj,String property) throws ServiceException {
		return getDAO().findByProperty(obj, property,MatchMode.END);
	}
	@Override
	public <T> List<T> findByPropertyMatchAnywhere(T obj,String property) throws ServiceException {
		return getDAO().findByProperty(obj, property,MatchMode.ANYWHERE);
	}

	@Override
	public int[] batchDeleteTag(Class entity, String[] pks) throws ServiceException {
		String sql = "update " + getDAO().getTableName(entity) +" set deltag = 1 where ";
		Map<String, String> pkmap = getDAO().getPk(entity); 
		for (String key : pkmap.keySet()) {
			String pk = pkmap.get(key);
			sql += pk + " = ?"; 
			break;
		}
		for (int i = 0; i < pks.length; i++) {
			getDAO().update(sql,pks[i]);//待优化
		}
		
		return null;
	}

	@Override
	public int deleteTag(Object entity) throws ServiceException {
		Class cls = entity.getClass();
		String sql = "update " + getDAO().getTableName(cls) +" set deltag = 1 where ";
		Map<String, String> pkmap = getDAO().getPk(cls);
		Object id = null;
		for (String key : pkmap.keySet()) {
			String pk = pkmap.get(key);
			sql += pk + " = ?";
			id = ReflectionUtils.getFieldValue(entity, key);
			break;
		}
		return getDAO().update(sql,id);
	}

}

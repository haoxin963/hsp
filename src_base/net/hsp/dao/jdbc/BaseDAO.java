package net.hsp.dao.jdbc;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import net.hsp.common.ReflectionUtils;
import net.hsp.dao.DynamicDataSource;
import net.hsp.dao.ProxoolExtDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class BaseDAO extends JdbcTemplate {

	NamedParameterJdbcTemplate npt = null;
	private static Logger logger = Logger.getLogger(BaseDAO.class);

	private NamedParameterJdbcTemplate getNpt() {
		if (npt == null) {
			npt = new NamedParameterJdbcTemplate(this.getDataSource());
		}
		return npt;
	}

	/**
	 * 持久化对象
	 * 
	 * @param entity
	 * @return 返回带主键的持久化对象
	 */
	public <T> T saveEntity(T entity) {
		String tableName = this.getTableName(entity.getClass());
		Map<String, String> map = getColumns(entity.getClass());
		Set<String> set = map.keySet();
		Map<String, Object> m2 = new HashMap<String, Object>();
		Map<String, ?> pkMap = getPk(entity.getClass());
		for (String key : set) {
			Object obj = ReflectionUtils.getFieldValue(entity, key);
			if (obj != null && obj.getClass().getPackage().getName().indexOf("entity") > 1) {
				m2.put(map.get(key).toString(), ReflectionUtils.getFieldValue(obj, getPk(obj.getClass()).keySet().toArray()[0] + ""));
			} else {
				if (obj != null) {
					// 主键为string类型，误传""时
					if (pkMap.keySet().toArray()[0].equals(key) && StringUtils.isBlank(obj.toString())) {
						continue;
					}
					m2.put(map.get(key).toString(), obj);
				}
			}
		}
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this);
		insert.withTableName(tableName);
		Set<String> keys = m2.keySet();
		String[] o = new String[keys.size()];
		keys.toArray(o);
		insert.usingColumns(o);
		String pk = "";

		String rtn = "";
		if (pkMap == null || pkMap.isEmpty()) {
			// 如无主键直接插入数据
			insert.execute(m2);
		} else {
			String[] pks = new String[pkMap.keySet().size()];
			int i = 0;
			for (String s : pkMap.keySet()) {
				pks[i] = s;
				i++;
			}
			if (pks.length == 1) {
				pk = pks[0];
				try {
					Field pkField = entity.getClass().getDeclaredField(pk);
					// 判断是否有主键生成策略
					if (pkField.isAnnotationPresent(GeneratedValue.class)) {
						insert.setGeneratedKeyName(pk);
						Number id = insert.executeAndReturnKey(m2);
						rtn = id.toString();
					} else {
						insert.execute(m2);
						rtn = String.valueOf(m2.get(pk));
					}

				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {
				}
			} else {
				// 多主键时直接插入
				insert.execute(m2);
			}
		}
		if (StringUtils.isNotBlank(pk)) {
			String type = ReflectionUtils.getAccessibleField(entity, pk).getType().getName();
			Object id = rtn;
			if ("java.lang.Integer".equals(type) || "int".equals(type)) {
				id = Integer.parseInt(rtn.toString());
			} else if ("java.lang.Long".equals(type) || "long".equals(type)) {
				id = Long.parseLong(rtn.toString());
			}
			ReflectionUtils.setFieldValue(entity, pk, id);
		}
		return entity;
	}

	// 兼容双、无主键，无主键策略插入 此方法运行效率低下，需要50毫秒
	public String save(Object entity) {
		String tableName = this.getTableName(entity.getClass());
		Map<String, String> map = getColumns(entity.getClass());
		Set<String> set = map.keySet();
		Map<String, Object> m2 = new HashMap<String, Object>();
		Map<String, ?> pkMap = getPk(entity.getClass());
		for (String key : set) {
			Object obj = ReflectionUtils.getFieldValue(entity, key);
			if (obj != null && obj.getClass().getPackage().getName().indexOf("entity") > 1) {
				m2.put(map.get(key).toString(), ReflectionUtils.getFieldValue(obj, getPk(obj.getClass()).keySet().toArray()[0] + ""));
			} else {
				if (obj != null && pkMap != null) {
					// 主键为string类型，误传""时
					if (pkMap.keySet().toArray()[0].equals(key) && StringUtils.isBlank(obj.toString())) {
						continue;
					}
					m2.put(map.get(key).toString(), obj);
				}
			}
		}
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this);
		insert.withTableName(tableName);
		Set<String> keys = m2.keySet();
		String[] o = new String[keys.size()];
		keys.toArray(o);
		insert.usingColumns(o);
		String pk = "";

		String rtn = "";
		if (pkMap == null || pkMap.isEmpty()) {
			// 如无主键直接插入数据
			insert.execute(m2);
		} else {
			String[] pks = new String[pkMap.keySet().size()];
			int i = 0;
			for (String s : pkMap.keySet()) {
				pks[i] = s;
				i++;
			}
			if (pks.length == 1) {
				pk = pks[0];
				try {
					Field pkField = entity.getClass().getDeclaredField(pk);
					// 判断是否有主键生成策略
					if (pkField.isAnnotationPresent(GeneratedValue.class)) {
						insert.setGeneratedKeyName(pk);
						Number id = insert.executeAndReturnKey(m2);
						rtn = id.toString();
					} else {
						insert.execute(m2);
						rtn = String.valueOf(m2.get(pk));
					}

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			} else {
				// 多主键时直接插入
				insert.execute(m2);
			}
		}
		return rtn;
	}

	// 直接插入，不启用主键策略
	public void save2(Object entity) {
		String tableName = this.getTableName(entity.getClass());
		Map<String, String> map = getColumns(entity.getClass());
		Set<String> set = map.keySet();
		Map<String, Object> m2 = new HashMap<String, Object>();
		for (String key : set) {
			Object obj = ReflectionUtils.getFieldValue(entity, key);
			if (obj != null && obj.getClass().getPackage().getName().indexOf("entity") > 1) {
				m2.put(map.get(key).toString(), ReflectionUtils.getFieldValue(obj, getPk(obj.getClass()).keySet().toArray()[0] + ""));
			} else {
				m2.put(map.get(key).toString(), obj);
			}
		}
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this);
		insert.withTableName(tableName);
		insert.execute(m2);
	}

	// 兼容双、无主键，无主键策略插入
	@Deprecated
	public String save3(Object entity) {
		String tableName = this.getTableName(entity.getClass());
		Map<String, String> map = getColumns(entity.getClass());
		Set<String> set = map.keySet();
		Map<String, Object> m2 = new HashMap<String, Object>();
		for (String key : set) {
			Object obj = ReflectionUtils.getFieldValue(entity, key);
			if (obj != null && obj.getClass().getPackage().getName().indexOf("entity") > 1) {
				m2.put(map.get(key).toString(), ReflectionUtils.getFieldValue(obj, getPk(obj.getClass()).keySet().toArray()[0] + ""));
			} else {
				m2.put(map.get(key).toString(), obj);
			}
		}
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this);
		insert.withTableName(tableName);

		String pk = "";

		Map<String, ?> pkMap = getPk(entity.getClass());

		String rtn = "";
		if (pkMap == null || pkMap.isEmpty()) {
			// 如无主键直接插入数据
			insert.execute(m2);
		} else {
			String[] pks = new String[pkMap.keySet().size()];
			int i = 0;
			for (String s : pkMap.keySet()) {
				pks[i] = s;
				i++;
			}
			if (pks.length == 1) {
				pk = pks[0];
				try {
					Field pkField = entity.getClass().getDeclaredField(pk);
					System.out.println(pkField);
					// 判断是否有主键生成策略
					if (pkField.isAnnotationPresent(GeneratedValue.class)) {
						insert.setGeneratedKeyName(pk);
						Number id = insert.executeAndReturnKey(m2);
						rtn = id.toString();
					} else {
						insert.execute(m2);
						rtn = String.valueOf(m2.get(pk));
					}

				} catch (SecurityException e) {
				} catch (NoSuchFieldException e) {
				}
			} else {
				// 多主键时直接插入
				insert.execute(m2);
			}
		}
		return rtn;
	}

	public int delete(Object entity) {
		Map pkmap = getPk(entity.getClass());
		String tableName = this.getTableName(entity.getClass());
		StringBuffer sql = new StringBuffer("delete from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s = "";
		String s2 = "";
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s2 += pkmap.get(key) + " = :" + pkmap.get(key) + " and ";
			m.put(pkmap.get(key) + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(s2.substring(0, s2.length() - 4));
		return getNpt().update(sql.toString(), m);
	}

	public int[] batchDelete(Class cls, final String[] pks) {
		Map pkmap = getPk(cls);
		String tableName = this.getTableName(cls);
		StringBuffer sql = new StringBuffer("delete from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s2 = "";
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s2 += pkmap.get(key) + " = ? and ";
		}
		sql.append(s2.substring(0, s2.length() - 4));
		List param = new ArrayList();
		for (int i = 0; i < pks.length; i++) {
			String[] o = new String[] { pks[i] };
			param.add(o);
		}
		return this.batchUpdate(sql.toString(), param);
	}

	/** 按实体更新，当属性值为空时则将对应字段置空 */
	public int update(Object entity) {
		String tableName = this.getTableName(entity.getClass());
		Map map = this.getColumns(entity.getClass());
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " set ");
		Set<String> set = map.keySet();
		Map<String, Object> m = new HashMap<String, Object>();
		String s = "";
		String s2 = "";
		int count = set.size();
		for (String key : set) {
			s2 += map.get(key) + "= :" + map.get(key) + ",";
			m.put(map.get(key) + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(s2.substring(0, s2.length() - 1));
		sql.append(" where ");
		Map pkmap = getPk(entity.getClass());
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s += pkmap.get(key) + " = :" + pkmap.get(key) + " and ";
			m.put(pkmap.get(key) + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(s.substring(0, s.length() - 4));
		return getNpt().update(sql.toString(), m);
	}

	/** 按实体更新，当属性值为空时则跳过此字段 */
	public int update2(Object entity) {
		Class<?> entityClass = entity.getClass();
		String tableName = this.getTableName(entityClass);
		Map<String, String> columnMap = getColumns(entityClass);
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
		Map<String, Object> m = new HashMap<String, Object>();
		String whereSql = "";
		String setSql = "";
		for (String key : columnMap.keySet()) {
			Object value = ReflectionUtils.getFieldValue(entity, key);
			if (null != value) {
				String column = columnMap.get(key);
				setSql += column + "= :" + column + ",";
				m.put(column + "", value);
			}
		}
		sql.append(setSql.substring(0, setSql.length() - 1));
		sql.append(" WHERE ");
		Map<String, String> pkmap = getPk(entityClass);
		for (String key : pkmap.keySet()) {
			String pk = pkmap.get(key);
			whereSql += pk + " = :" + pk + " AND ";
			m.put(pk + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(whereSql.substring(0, whereSql.length() - 4));
		return getNpt().update(sql.toString(), m);
	}

	/**
	 * 按实体更新，当属性值为空时则跳过此字段
	 * 
	 * @param entity
	 *            实体
	 * @param columns
	 *            强制置空的字段
	 * @return
	 */
	public int update2(Object entity, List<String> columns) {
		Class<?> entityClass = entity.getClass();
		String tableName = this.getTableName(entityClass);
		Map<String, String> columnMap = getColumns(entityClass);
		StringBuffer sql = new StringBuffer("UPDATE " + tableName + " SET ");
		Map<String, Object> m = new HashMap<String, Object>();
		String whereSql = "";
		String setSql = "";
		for (String key : columnMap.keySet()) {
			Object value = ReflectionUtils.getFieldValue(entity, key);
			String column = columnMap.get(key);
			if (columns.contains(column)) {
				setSql += column + "= :" + column + ",";
				m.put(column + "", null);
			} else if (null != value) {
				setSql += column + "= :" + column + ",";
				m.put(column + "", value);
			}
		}
		sql.append(setSql.substring(0, setSql.length() - 1));
		sql.append(" WHERE ");
		Map<String, String> pkmap = getPk(entityClass);
		for (String key : pkmap.keySet()) {
			String pk = pkmap.get(key);
			whereSql += pk + " = :" + pk + " AND ";
			m.put(pk + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(whereSql.substring(0, whereSql.length() - 4));
		return getNpt().update(sql.toString(), m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.hsp.dao.jdbc.IBaseDAO#executeSave(java.lang.String,
	 * java.lang.Object)
	 */
	public Object executeSave(String sql, Object entity) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(entity);
		KeyHolder keyholder = new GeneratedKeyHolder();
		getNpt().update(sql, param, keyholder);
		return keyholder.getKey().intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.hsp.dao.jdbc.IBaseDAO#executeUpdate(java.lang.String,
	 * java.lang.Object)
	 */
	public Object executeUpdate(String sql, Object entity) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(entity);
		return getNpt().update(sql, param);
	}

	public Map<String, ?> queryByClass2Entity(Class entity, int firstResult, int maxResults) {
		String sql = "SELECT * FROM " + this.getTableName(entity);
		return queryBySql2Entity(sql, null, entity, firstResult, maxResults);
	}

	public Map<String, ?> queryByClass2Map(Class entity, int firstResult, int maxResults) {
		String sql = "SELECT * FROM " + this.getTableName(entity);
		return queryBySql2Map(sql, null, firstResult, maxResults);
	}

	public Map<String, ?> queryBySql2Entity(String sql, Object[] args, Class entity, int firstResult, int maxResults) {
		Map map = new HashMap();
		map.put("total", queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", args, Long.class));

		String limitSql = "SELECT * FROM (" + sql + ") _A ";
		if (maxResults >= 50) {
			logger.warn("查询每页超过50条!" + (limitSql + "LIMIT " + firstResult + "," + maxResults));
		}
		if (maxResults > 0) {
			limitSql += "LIMIT " + firstResult + "," + maxResults;
		}

		map.put("rows", query(limitSql, new BeanPropertyRowMapper(entity), args));
		int page = 1;
		if (maxResults > 0) {
			page = (firstResult / maxResults) + 1;
		}
		map.put("page", page);
		return map;
	}

	public <T> List<T> queryBySql2Entity(String sql, Object[] args, Class<T> entityClass) {
		Map map = new HashMap();
		List<T> list = query(sql, new BeanPropertyRowMapper(entityClass), args);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list;
		}
	}

	public Map<String, ?> queryBySql2Map(String sql, Object[] args, int firstResult, int maxResults) {
		String dbType = null;
		if (this.getDataSource() instanceof DynamicDataSource) {
			dbType = DbInfo.getDbType();
		} else if (this.getDataSource() instanceof ProxoolExtDataSource) {
			ProxoolExtDataSource ds = (ProxoolExtDataSource) this.getDataSource();
			if (ds.getDriverUrl().indexOf("mysql") > -1) {
				dbType = DbInfo.MYSQL;
			} else {
				dbType = DbInfo.ORACLE;
			}
		}
		/**
		 * else if(this.getDataSource() instanceof AtomikosDataSourceBean){
		 * AtomikosDataSourceBean ds = (AtomikosDataSourceBean)
		 * this.getDataSource();
		 * if(ds.getXaProperties().getProperty("URL").indexOf("mysql")>-1){
		 * dbType = DbInfo.MYSQL; }else{ dbType = DbInfo.ORACLE; } }
		 */

		if (DbInfo.ORACLE.equals(dbType)) {
			return queryBySql2Map_oracle(sql, args, firstResult, maxResults);
		} else {
			return queryBySql2Map_mysql(sql, args, firstResult, maxResults);
		}
	}

	private Map<String, ?> queryBySql2Map_mysql(String sql, Object[] args, int firstResult, int maxResults) {
		Map map = new HashMap();
		map.put("total", queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", args, Long.class));
		String limitSql = "SELECT * FROM (" + sql + ") _A ";
		if (maxResults > 0) {
			limitSql += "LIMIT " + firstResult + "," + maxResults;
		}
		map.put("rows", queryForList(limitSql, args));
		int page = 1;
		if (maxResults > 0) {
			page = (firstResult / maxResults) + 1;
		}
		map.put("page", page);
		return map;
	}

	private Map<String, ?> queryBySql2Map_oracle(String sql, Object[] args, int firstResult, int maxResults) {
		Map map = new HashMap();
		map.put("total", queryForObject("SELECT COUNT(0) FROM (" + sql + ") A", args, Long.class));
		String limitSql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (" + sql + ")  A ";
		if (maxResults > 0) {
			limitSql += "WHERE ROWNUM < " + (maxResults + 1) + ")  WHERE RN >= " + (firstResult + 1);
		}
		map.put("rows", queryForList(limitSql, args));
		int page = 1;
		if (maxResults > 0) {
			page = (firstResult / maxResults) + 1;
		}
		map.put("page", page);
		return map;
	}

	public <T> T findById(Class<T> entityClass, Serializable id) {
		Map pkmap = getPk(entityClass);
		String tableName = this.getTableName(entityClass);

		StringBuffer sql = new StringBuffer("select * from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s2 = "";
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s2 += pkmap.get(key) + " = :" + pkmap.get(key) + " and ";
			m.put(pkmap.get(key) + "", id);
		}
		sql.append(s2.substring(0, s2.length() - 4));
		RowMapper rowMapper = new BeanPropertyRowMapper(entityClass);
		try {
			return (T) getNpt().queryForObject(sql.toString(), m, rowMapper);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public <T> List<T> findByProperty(T obj, String property, MatchMode matchMode) {
		Class cls = obj.getClass();
		Map pkmap = getPk(cls);
		String tableName = this.getTableName(cls);

		StringBuffer sql = new StringBuffer("select * from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s2 = "";
		Map<String, String> map = getColumns(cls);
		String name = map.get(property);

		sql.append(name + " = ?");
		RowMapper rowMapper = new BeanPropertyRowMapper(cls);
		try {
			return this.query(sql.toString(), rowMapper, ReflectionUtils.getFieldValue(obj, property));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public Map findById2Map(Class entityClass, Serializable id) {
		Map pkmap = getPk(entityClass);
		String tableName = this.getTableName(entityClass);
		StringBuffer sql = new StringBuffer("select * from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s2 = "";
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s2 += pkmap.get(key) + " = :" + pkmap.get(key) + " and ";
			m.put(pkmap.get(key) + "", id);
		}
		sql.append(s2.substring(0, s2.length() - 4));
		try {
			return getNpt().queryForMap(sql.toString(), m);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
		try {
			return super.queryForObject(sql, requiredType, args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		try {
			return super.queryForObject(sql, requiredType);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		try {
			return super.queryForObject(sql, args, requiredType);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
		try {
			return super.queryForMap(sql, args);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		try {
			return super.queryForMap(sql, args, argTypes);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Map<String, Object> queryForMap(String sql) throws DataAccessException {

		try {
			return super.queryForMap(sql);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public <T> T findById(T entity) {
		Map pkmap = getPk(entity.getClass());
		String tableName = this.getTableName(entity.getClass());

		StringBuffer sql = new StringBuffer("select * from " + tableName + " where ");
		Map<String, Object> m = new HashMap<String, Object>();
		String s2 = "";
		Set<String> pkset = pkmap.keySet();
		for (String key : pkset) {
			s2 += pkmap.get(key) + " = :" + pkmap.get(key) + " and ";
			m.put(pkmap.get(key) + "", ReflectionUtils.getFieldValue(entity, key));
		}
		sql.append(s2.substring(0, s2.length() - 4));
		RowMapper<T> rowMapper = new BeanPropertyRowMapper(entity.getClass());
		try {
			return getNpt().queryForObject(sql.toString(), m, rowMapper);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	@SuppressWarnings(value = { "rawtypes" })
	public static Map<String, String> getColumns(Class classtype) {
		Field[] fields = classtype.getDeclaredFields();
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			Annotation[] annotations = fields[i].getAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				if (annotations[j] instanceof Column) {
					Column column = (Column) annotations[j];
					map.put(fields[i].getName(), column.name());
				} else if (annotations[j] instanceof JoinColumn) {
					JoinColumn column = (JoinColumn) annotations[j];
					map.put(fields[i].getName(), column.name());
				}
			}
		}
		Class superClass = classtype.getSuperclass();
		if (superClass != null) {
			fields = superClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Annotation[] annotations = fields[i].getAnnotations();
				for (int j = 0; j < annotations.length; j++) {
					if (annotations[j] instanceof Column) {
						Column column = (Column) annotations[j];
						map.put(fields[i].getName(), column.name());
					}
				}
			}
		}
		return map;
	}

	public static Map<String, String> getPk(Class classtype) {
		if (!classtype.getSuperclass().getName().equals("java.lang.Object")) {
			return getPk(classtype.getSuperclass());
		}

		Field[] fields = classtype.getDeclaredFields();
		Map<String, String> map = null;
		for (int i = 0; i < fields.length; i++) {
			Field filed = fields[i];
			if (filed.isAnnotationPresent(Id.class) && filed.isAnnotationPresent(Column.class)) {
				Column column = filed.getAnnotation(Column.class);
				if (map == null) {
					map = new HashMap<String, String>();
				}
				map.put(fields[i].getName(), column.name());
			}
			// 替换以下代码 by lk0482
			// Annotation[] annotations = fields[i].getAnnotations();
			// boolean b = false;
			// for (int j = 0; j < annotations.length; j++) {
			// if (annotations[j] instanceof Id) {
			// b = true;
			// }
			// if (annotations[j] instanceof Column && b) {
			// if (map == null) {
			// map = new HashMap<String, String>();
			// }
			// Column column = (Column) annotations[j];
			// map.put(fields[i].getName(), column.name());
			// }
			//
			// }
		}
		return map;
	}

	public static String getPkColumn(Class classtype) {
		Field[] fields = classtype.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field filed = fields[i];
			if (filed.isAnnotationPresent(Id.class) && filed.isAnnotationPresent(Column.class)) {
				Column column = filed.getAnnotation(Column.class);
				return column.name();
			}
			// 替换以下代码 by lk0482
			// Annotation[] annotations = fields[i].getAnnotations();
			// boolean b = false;
			// for (int j = 0; j < annotations.length; j++) {
			// if (annotations[j] instanceof Id) {
			// b = true;
			//
			// }
			// if (annotations[j] instanceof Column && b) {
			// Column column = (Column) annotations[j];
			// return column.name();
			// }
			// }
		}
		return "id";
	}

	/**
	 * 获取表名
	 * 
	 * @param classtype
	 * @return
	 */
	@SuppressWarnings(value = { "rawtypes" })
	public String getTableName(Class classtype) {
		Annotation[] anno = classtype.getAnnotations();
		String tableName = "";
		for (int i = 0; i < anno.length; i++) {
			if (anno[i] instanceof Table) {
				Table table = (Table) anno[i];
				tableName = table.name();
			}
		}
		return tableName;
	}

}

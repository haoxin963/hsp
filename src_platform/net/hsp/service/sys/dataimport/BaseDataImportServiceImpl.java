package net.hsp.service.sys.dataimport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

import net.hsp.common.Comment;
import net.hsp.common.Holder;
import net.hsp.common.dataimport.DataImport;
import net.hsp.service.BaseServiceImpl;

@Service(value = "BaseDataImportServiceImpl")
public class BaseDataImportServiceImpl extends BaseServiceImpl implements DataImportService {

	@Override
	public String getTemplate() {
		String template = null;
		if (this.getClass().isAnnotationPresent(DataImport.class)) {
			DataImport dataImport = this.getClass().getAnnotation(DataImport.class);
			template = dataImport.template();
		}
		return template;
	}

	private Class<?> entityClass = null;

	@Override
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public Class<?> getEntityClass() {
		if (null == this.entityClass) {
			if (this.getClass().isAnnotationPresent(DataImport.class)) {
				DataImport dataImport = this.getClass().getAnnotation(DataImport.class);
				this.entityClass = dataImport.entity();
			}
		}
		return this.entityClass;
	}

	@Override
	public Map<String, String> getColumnMap() {
		Map<String, String> columnMap = new HashMap<String, String>();

		Class<?> entityClass = getEntityClass();
		if (null != entityClass && entityClass.isAnnotationPresent(Entity.class)) {
			Field[] fields = entityClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Column.class)) {
					if (field.isAnnotationPresent(GeneratedValue.class)) {
						GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
						if (generatedValue.strategy() == GenerationType.IDENTITY) {
							continue;
						}
					}
					Column column = field.getAnnotation(Column.class);
					String columnName = column.name();
					if (null == columnName || columnName.isEmpty()) {
						columnName = field.getName();
					}
					String columnLabel = "";
					if (field.isAnnotationPresent(Comment.class)) {
						columnLabel = field.getAnnotation(Comment.class).value();
					}
					if (null == columnLabel || columnLabel.isEmpty()) {
						columnLabel = columnName;
					}
					columnMap.put(columnLabel, columnName);
				}
			}
		}

		return columnMap;
	}

	public String getTableName() {
		String tableName = null;
		Class<?> entityClass = getEntityClass();
		if (null != entityClass && entityClass.isAnnotationPresent(Table.class)) {
			Table table = entityClass.getAnnotation(Table.class);
			tableName = table.name();
		}
		return tableName;
	}

	@Override
	public List<Map<String, String>> getDateList(List<Map<String, String>> dataList) {
		return dataList;
	}

	@Override
	public boolean batchImport(List<Map<String, String>> dataList, Holder<String> error) {
		boolean result = false;
		if (null != dataList && !dataList.isEmpty()) {
			try {
				Map<String, String> firstData = dataList.get(0);
				List<String> keyList = new ArrayList<String>();
				for (String key : firstData.keySet()) {
					keyList.add(key);
				}
				String sql = "INSERT INTO " + this.getTableName() + " (";
				String _sql = "";
				for (int i = 0; i < keyList.size(); i++) {
					if (i > 0) {
						sql += ",";
						_sql += ",";
					}
					sql += keyList.get(i);
					_sql += "?";
				}
				sql += ") VALUES (" + _sql + ");";
				List<Object[]> batchArgs = new ArrayList<Object[]>();
				for (Map<String, String> data : dataList) {
					Object[] object = new Object[data.size()];
					for (int i = 0; i < keyList.size(); i++) {
						object[i] = data.get(keyList.get(i));
					}
					batchArgs.add(object);
				}
				this.getDAO().batchUpdate(sql, batchArgs);
				result = afterImport(dataList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public boolean afterImport(List<Map<String, String>> dataList) {
		return true;
	}

}

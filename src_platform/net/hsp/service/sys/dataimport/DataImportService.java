package net.hsp.service.sys.dataimport;

import java.util.List;
import java.util.Map;

import net.hsp.common.Holder;

public interface DataImportService {

	public abstract String getTemplate();

	public void setEntityClass(Class<?> entityClass);

	public abstract Class<?> getEntityClass();

	public abstract Map<String, String> getColumnMap();

	public abstract String getTableName();

	public abstract List<Map<String, String>> getDateList(List<Map<String, String>> dataList);

	public abstract boolean batchImport(List<Map<String, String>> dataList, Holder<String> error);

	public boolean afterImport(List<Map<String, String>> dataList);

}
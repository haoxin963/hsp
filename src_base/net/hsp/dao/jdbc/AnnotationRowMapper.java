package net.hsp.dao.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.Assert;

public class AnnotationRowMapper<T> extends BeanPropertyRowMapper<T> {
	private Class<T> mappedClass;

	public AnnotationRowMapper(Class<T> mappedClass) {
		initialize(mappedClass);
	}

	private boolean primitivesDefaultedForNullValue = false;
	private Map<String, PropertyDescriptor> mappedFields;
	private Set<String> mappedProperties;

	protected void initialize(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
		this.mappedFields = new HashMap<String, PropertyDescriptor>();
		this.mappedProperties = new HashSet<String>();
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null) {
				this.mappedFields.put(pd.getName().toLowerCase(), pd);
				String underscoredName = underscoreName(pd.getName());
				if (!pd.getName().toLowerCase().equals(underscoredName)) {
					this.mappedFields.put(underscoredName, pd);
				}
				this.mappedProperties.add(pd.getName());
			}
		}
	}

	private String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}

	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {

		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		initBeanWrapper(bw);
		Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

		for (Field field : mappedClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class)) {
				Column acolumn = field.getAnnotation(Column.class);
				String fieldName = field.getName();
				String column = acolumn.name();
				if (null == column || column.isEmpty()) {
					column = fieldName;
				}
				int index = 0;
				try {
					index = rs.findColumn(column);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				if (index > 0) {
					PropertyDescriptor pd = this.mappedFields.get(fieldName.replaceAll(" ", "").toLowerCase());
					if (pd != null) {
						try {
							Object value = getColumnValue(rs, index, pd);
							if (logger.isDebugEnabled() && rowNumber == 0) {
								logger.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type " + pd.getPropertyType());
							}
							try {
								bw.setPropertyValue(pd.getName(), value);
							} catch (TypeMismatchException e) {
								if (value == null && primitivesDefaultedForNullValue) {
									logger.debug("Intercepted TypeMismatchException for row " + rowNumber + " and column '" + column + "' with value " + value + " when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() + " on object: " + mappedObject);
								} else {
									throw e;
								}
							}
							if (populatedProperties != null) {
								populatedProperties.add(pd.getName());
							}
						} catch (NotWritablePropertyException ex) {
							throw new DataRetrievalFailureException("Unable to map column " + column + " to property " + pd.getName(), ex);
						}
					}
				}
			}
		}

		if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
			throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " + "necessary to populate object of class [" + this.mappedClass + "]: " + this.mappedProperties);
		}

		return mappedObject;
	}
}

package net.hsp.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.web.util.FilterMap;

import org.apache.commons.lang.StringUtils;

public class SQLBuild {
	public static final String SQL = "sql";
	public static final String ARGS = "args";
	
	public static  Map<String,Object> buildLike(Map filter){
		Map result = new HashMap();
		result.put(SQL,"");
		result.put(ARGS,null);
		StringBuilder sb = new StringBuilder();
		if (filter!=null) { 
			if (filter!=null) {
				Set<String> set = filter.keySet();
				List list = new ArrayList(); 
				int i = 0;
				for (String k : set) {
					if(k.contains(",")) {//多条件模糊查询
						String[] str = k.split(",");
						if(str.length > 0) {
							sb.append(" and (1=2 ");
							for (String wname : str) {
								list.add("%"+filter.get(k)+"%");
								sb.append(" or "+ wname +" like ?");
								i++;
							}
							sb.append(") "); 
						}
					} else if (StringUtils.isNotBlank((String)filter.get(k))) { 
						if(k.contains(":")) {//自定义查询条件(=,>,<.>=.<=,<>,in,not in)
							String[] ks = k.split(":");
							if(ks.length == 2) {
								list.add(filter.get(k));
								if("in".equals(ks[1]) || "not in".equals(ks[1])) {
									sb.append(" and "+ ks[0] +" "+ks[1]+" (?)");
								} else {
									sb.append(" and "+ ks[0] +" "+ks[1]+ " ?");
								}
							}
							i++;
						} else {
							list.add("%"+filter.get(k)+"%");
							sb.append(" and "+ k +" like ?");
							i++;
						}
					} 
				}
				if (list.size()>0) {
					Object[] args = new Object[i];
					list.toArray(args);
					result.put(SQL,sb.toString());
					result.put(ARGS,args);
				} 
			} 
		}
		return result;
	}
	
	public static  Map<String,Object> buildLike(FilterMap map){
		Map result = new HashMap();
		result.put(SQL,"");
		result.put(ARGS,null);
		StringBuilder sb = new StringBuilder();
		if (map!=null) {
			Map filter = map.getFilter();
			if (filter!=null) {
				Set<String> set = filter.keySet();
				List list = new ArrayList(); 
				int i = 0;
				for (String k : set) {
					if(k.contains(",")) {
						String[] str = k.split(",");
						if(str.length > 0) {
							sb.append(" and (1=2 ");
							for (String wname : str) {
								list.add("%"+filter.get(k)+"%");
								sb.append(" or "+ wname +" like ?");
								i++;
							}
							sb.append(") "); 
						}
					} else if (StringUtils.isNotBlank((String)filter.get(k))) { 
						if(k.contains(":")) {
							String[] ks = k.split(":");
							if(ks.length == 2) {
								list.add(filter.get(k));
								if("in".equals(ks[1]) || "not in".equals(ks[1])) {
									sb.append(" and "+ ks[0] +" in (?)");
								} else {
									sb.append(" and "+ ks[0] +" "+ks[1]+ " ?");
								}
								i++;
							}
						} else {
							list.add("%"+filter.get(k)+"%");
							sb.append(" and "+ k +" like ?");
							i++;
						}
					} 
				}
				if (list.size()>0) {
					Object[] args = new Object[i];
					list.toArray(args);
					result.put(SQL,sb.toString());
					result.put(ARGS,args);
				} 
			} 
		}
		return result;
	}
	
	public static  Map<String,Object> buildEquals(Map filter){
		Map result = new HashMap();
		result.put(SQL,"");
		result.put(ARGS,null);
		StringBuilder sb = new StringBuilder(); 
		if (filter!=null) {
			Set<String> set = filter.keySet();
			List list = new ArrayList(); 
			int i = 0;
			for (String k : set) {
				if(k.contains(",")) {//多条件模糊查询
					String[] str = k.split(",");
					if(str.length > 0) {
						sb.append(" and (1=2 ");
						for (String wname : str) {
							list.add(filter.get(k));
							sb.append(" or "+ wname +" = ?");
							i++;
						}
						sb.append(") "); 
					}
				}else if (StringUtils.isNotBlank((String)filter.get(k))) {
					if(k.contains(":")) {//自定义查询条件(=,>,<.>=.<=,<>,in,not in)
						String[] ks = k.split(":");
						if(ks.length == 2) {
							list.add(filter.get(k));
							if("in".equals(ks[1]) || "not in".equals(ks[1])) {
								sb.append(" and "+ ks[0] +" "+ks[1]+" (?)");
							} else {
								sb.append(" and "+ ks[0] +" "+ks[1]+ " ?");
							}
						}
						i++;
					} else {
						list.add(filter.get(k));
						sb.append(" and "+ k +" = ? ");
						i++;
					}
				} 
			}
			if (list.size()>0) {
				Object[] args = new Object[i];
				list.toArray(args);
				result.put(SQL,sb.toString());
				result.put(ARGS,args);
			} 
		} 
		return result;
	}
	
	public static  Map<String,Object> buildEquals(FilterMap map){
		Map result = new HashMap();
		result.put(SQL,"");
		result.put(ARGS,null);
		StringBuilder sb = new StringBuilder();
		Map filter = map.getFilter();
		if (filter!=null) {
			Set<String> set = filter.keySet();
			List list = new ArrayList(); 
			int i = 0;
			for (String k : set) {
				if(k.contains(",")) {//多条件模糊查询
					String[] str = k.split(",");
					if(str.length > 0) {
						sb.append(" and (1=2 ");
						for (String wname : str) {
							list.add(filter.get(k));
							sb.append(" or "+ wname +" = ?");
							i++;
						}
						sb.append(") "); 
					}
				} else if (StringUtils.isNotBlank((String)filter.get(k))) { 
					if(k.contains(":")) {//自定义查询条件(=,>,<.>=.<=,<>,in,not in)
						String[] ks = k.split(":");
						if(ks.length == 2) {
							list.add(filter.get(k));
							if("in".equals(ks[1]) || "not in".equals(ks[1])) {
								sb.append(" and "+ ks[0] +" "+ks[1]+" (?)");
							} else {
								sb.append(" and "+ ks[0] +" "+ks[1]+ " ?");
							}
						}
						i++;
					} else {
						list.add(filter.get(k));
						sb.append(" and "+ k +" = ? ");
						i++;
					}
				} 
			}
			if (list.size()>0) {
				Object[] args = new Object[i];
				list.toArray(args);
				result.put(SQL,sb.toString());
				result.put(ARGS,args);
			} 
		} 
		return result;
	}
}

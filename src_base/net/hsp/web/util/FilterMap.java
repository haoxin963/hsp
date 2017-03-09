package net.hsp.web.util;

import java.util.HashMap;
import java.util.Map;

public class FilterMap{

	private Map<String, String> filter = new HashMap();

	public Map<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, String> filter) {
		this.filter = filter;
	}
	 
	public String get(String key){
		if(filter ==null){
			return null;
		}else{
			return filter.get(key);
		}
	} 
	
	public void set(String key,String v){
		filter.put(key, v);
	}
	
}

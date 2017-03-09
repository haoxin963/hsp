package net.hsp.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.dao.DynamicDataSource;
import net.hsp.service.sys.dic.DictionaryService;
import net.hsp.web.util.DicValueMap;
import net.hsp.web.util.SpringCtx;

import org.springframework.context.ApplicationContext;

public class DicMap extends HashMap {
	@Override
	public Object get(Object key) {
		String custId = key.toString(); 
		if (super.get(custId)==null) {
			ApplicationContext context = SpringCtx.getSpringContext();
			DictionaryService dicService = (DictionaryService) context.getBean("dictionaryServiceImpl");
			Map<String,Map> custDic = new DicMap();
			Set<String> dicType = new HashSet();
			try { 
					DynamicDataSource.setCustId(custId);
					List<Map<String,Object>> list = dicService.findAllDics(); 
					for (Map<String, Object> map : list) {
						dicType.add((String) map.get("DICTYPENAME")); 
					}
					Map<String,List> m = new HashMap(); 
					for (String s : dicType) {
						List item = new ArrayList();
						for (Map<String, Object> map : list) {
							if (map.get("DICTYPENAME").equals(s)) { 
								if (true) {
									item.add(map.get("DICTNAME"));
								}else{
									//复杂类型 扩展字段 
									DicValueMap dicv = new DicValueMap();
									dicv.put("d",map.get("DICTNAME"));
									item.add(dicv);
								} 
							} 
						}
						m.put(s,item); 
					}
					this.put(custId, m);
					System.out.println("从db装载字典数据.");
			} catch (Exception e1) { 
				e1.printStackTrace();
			}
		}
		return super.get(key);
	}

}

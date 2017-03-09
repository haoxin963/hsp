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

public class DicExtMap extends HashMap {
	@Override
	public Object get(Object key) {
		String custId = key.toString(); 
		if (super.get(custId)==null) {
			ApplicationContext context = SpringCtx.getSpringContext();
			DictionaryService dicService = (DictionaryService) context.getBean("dictionaryServiceImpl");
			Map<String,Map> custDic = new DicExtMap();
			Set<String> dicType = new HashSet();
			try { 
					DynamicDataSource.setCustId(custId);
					List<Map<String,Object>> list = dicService.findAllDics(); 
					for (Map<String, Object> map : list) {
						dicType.add((String) map.get("TYPECODE")); 
					}
					Map<String,List> m = new HashMap(); 
					for (String s : dicType) {
						List item = new ArrayList();
						for (Map<String, Object> map : list) {
							if (map.get("TYPECODE").equals(s)){
								DicValueMap dicv = new DicValueMap();
								dicv.put("i",map.get("DICTID"));
								dicv.put("n",map.get("DICTNAME"));
								if (map.get("EXT0")!=null) {
									dicv.put("0",map.get("EXT0"));
								} 
								if (map.get("EXT1")!=null) {
									dicv.put("1",map.get("EXT1"));
								} 
								if (map.get("EXT2")!=null) {
									dicv.put("2",map.get("EXT2"));
								} 
								if (map.get("EXT3")!=null) {
									dicv.put("3",map.get("EXT3"));
								} ;
								item.add(dicv);
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

package net.hsp.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import net.hsp.common.ReflectionUtils;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.ServiceMethodUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@ServiceLogUtil(name = "业务分发基类")
public class ServiceDispatcher extends BaseServiceImpl {
	public Object execute(String className, String serviceMethod, Object param) throws Exception {
		 
		Class[] c = null;
		if (StringUtils.isBlank(className)) {
			// Object[] objs = {param.get("dataModel")};
			// c[0] = Serializable.class; 
			Object[] objs = { param };
			if (serviceMethod==null) {
				throw new java.lang.IllegalArgumentException("业务方法不能定位.");
			}
			Method m = ReflectionUtils.getAccessibleMethod(this, serviceMethod);
			c = ReflectionUtils.getMethodParameterTypes(m);
			return ReflectionUtils.invokeMethod(this, serviceMethod, c, objs);
		} else {
			BaseServiceImpl object = (BaseServiceImpl) Class.forName(className).newInstance();
			object.baseDAO = this.baseDAO;
			Object[] objs = null;
			Method m = ReflectionUtils.getAccessibleMethod(object, serviceMethod);
			c = ReflectionUtils.getMethodParameterTypes(m); 
			Annotation[] an = m.getAnnotations();
			String[] serviceParamKey = null;
			if (an != null) {
				ServiceMethodUtil t = (ServiceMethodUtil) an[0];
				serviceParamKey = t.name().split(",");
			}
			objs = new Object[c.length];
			for (int i = 0; i < c.length; i++) { 
				Map map = (Map) param;//由BaseAction.execute分发而来
				Object tmp = null;
				System.out.println(c[i].getName());
				if (c[i].getName().equals("java.lang.String")) { 
					String[] p = (String[]) map.get(serviceParamKey[i]);
					if (p!=null) {
						tmp = p[0];
					}
					
				}else if (c[i].getSimpleName().equals("String[]")) { 
					String[] p = (String[]) map.get(serviceParamKey[i]);
					if (p!=null) {
						tmp = p;
					}
					
				}else if (c[i].getName().equals("java.util.Map")) { 
					tmp = map;
					
				}else if (c[i].getName().equals("java.util.List")) { 
					tmp = new ArrayList();
					
				}else if (c[i].getName().equals("java.lang.Integer")) { 
					String[] p = (String[]) map.get(serviceParamKey[i]);
					if (p!=null) { 
						tmp = Integer.parseInt(p[0]); 
					}
					
				}else if (c[i].getName().equals("java.lang.Boolean")) { 
					String[] p = (String[]) map.get(serviceParamKey[i]);
					if (p!=null) { 
						tmp = Boolean.valueOf(p[0]); 
					}
					
				}else if (c[i].getName().equals("boolean")) { 
					String[] p = (String[]) map.get(serviceParamKey[i]);
					if (p!=null) { 
						tmp = Boolean.parseBoolean(p[0]); 
					}else{
						tmp = false;
					}
					
				}else{
					System.out.println("待定的类型:"+c[i].getSimpleName());
					System.out.println("待定的类型:"+c[i].getName());
					Object entity = c[i].newInstance(); 
					
					Set<String> set  = map.keySet();
					for (String key : set) {
						if (key.startsWith(serviceParamKey[i]+".")&&key.split("\\.").length>1) {
							System.out.println("ok.................");
							System.out.println(map.get(key).getClass());
							BeanUtils.setProperty(entity, key.split("\\.")[1], map.get(key));
						}
					} 
					//BeanUtils.populate(entity, map); 
					tmp = entity;
				}
				objs[i] = tmp;
			}
			return ReflectionUtils.invokeMethod(object, serviceMethod, c, objs);
		}

	}

}

package net.hsp.web.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.hsp.dao.DynamicDataSource;
import net.hsp.web.util.SpringCtx;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class ServiceFactory {

	private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

	private static String getCustId() {
		return DynamicDataSource.getCustId();
	}

	private static ApplicationContext context() {
		return SpringCtx.getSpringContext();
	}

	/** cache的key */
	private static String KEY(Class<?> clazz) {
		return clazz.getCanonicalName() + "_" + getCustId();
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<? extends T> clazz) {
		String k = KEY(clazz);
		Object svc = cache.get(k);
		if (svc != null) {
			return (T) svc;
		}
		String custId = getCustId();
		String[] paks = null;
		String custPkg = "net.hsp.service." + custId;
		String platformPkg = "net.hsp.service.sys";
		String abs = HttpSessionFactory.getAbstract(custId);
		if (StringUtils.isNotBlank(abs)) {
			String absPkg = "net.hsp.service."+ abs;
			paks = new String[] { custPkg, absPkg, platformPkg };
		} else {
			paks = new String[] { custPkg, platformPkg };
		}

		Object bean = null;
		Map<String, ? extends T> map = context().getBeansOfType(clazz);
		for (int i = 0; i < paks.length; i++) {
			for (Entry<String, ? extends T> entry : map.entrySet()) {
				String beanName = entry.getKey();
				System.out.println("beanName=>"+beanName);
				if (beanName.startsWith(paks[i])) {
					bean = entry.getValue();
					break;
				}
			}
			if (bean != null) {
				break;
			}
		}

		cache.put(k, bean);
		return (T) bean;
	}

}

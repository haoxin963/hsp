package net.hsp.common;

import net.hsp.web.util.SpringCtx;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;

import org.springframework.cache.ehcache.EhCacheCacheManager;

public class CacheUtil {

	private CacheManager manager;

	private static CacheUtil ehCache;

	private CacheUtil() {
		org.springframework.cache.ehcache.EhCacheCacheManager cache = (EhCacheCacheManager) SpringCtx.getBean("cacheManager");
		manager = cache.getCacheManager();
	}

	public static CacheUtil getInstance() {
		if (ehCache == null) {
			ehCache = new CacheUtil();
		}
		return ehCache;
	}

	/**
	 * 存放在默认的defaultCache内
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value) {
		Cache cache = manager.getCache("defaultCache");
		Element element = new Element(key, value);
		cache.put(element);
		return true;
	}

	/**
	 * 存放在指定的cache内
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String cacheName, String key, Object value) {
		Cache cache = manager.getCache(cacheName);
		Element element = new Element(key, value);
		cache.put(element);
		return true;
	}

	public Object get(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Object get(String key) {
		Cache cache = manager.getCache("defaultCache");
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache getCache(String cacheName) {
		return manager.getCache(cacheName);
	}

	public void remove(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		cache.remove(key);

	}

	public String[] getCacheNames() {
		return manager.getCacheNames();
	}

	/**
	 * 对key模糊查询
	 * @param cacheName
	 * @param key
	 * @return 记录数
	 */
	public int queryKeyCount(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		Query q = cache.createQuery();
		q.includeKeys();
		q.addCriteria(Query.KEY.ilike(key + "*"));
		Results results = q.execute();// 执行查询
		return results.size();
	}
}

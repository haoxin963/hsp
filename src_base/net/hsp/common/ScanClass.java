package net.hsp.common;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
 

public class ScanClass {
	private final ResourcePatternResolver resourcePatternResolver;
	private static final String RESOURCE_PATTERN = "/**/*.class";
	private static final String PACKAGE_INFO_SUFFIX = ".package-info";
	private static final TypeFilter[] ENTITY_TYPE_FILTERS = new TypeFilter[] { new AnnotationTypeFilter(
			Controller.class, false) };

	private static Map<String,Object> actions;

	
	public ScanClass() {
		this.resourcePatternResolver = ResourcePatternUtils
				.getResourcePatternResolver(new PathMatchingResourcePatternResolver()); 
	}
	

	public static void main(String[] args) {
		ScanClass d = new ScanClass();
		try {
			//
			// 此处可以按包名扫描，或者按绝对路径扫描
			System.out
					.println(d.getClassName("file:E:/runtimeExt/WebRoot/WEB-INF/classes/**/*.class"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean matchesEntityTypeFilter(MetadataReader reader,

	MetadataReaderFactory readerFactory) throws IOException {
		for (TypeFilter filter : ENTITY_TYPE_FILTERS) {
			if (filter.match(reader, readerFactory)) {
				return true;
			}
		}
		return false;

	}

	public static Map<String,Object> getActions(String binRealPath){
		try {
			if (actions!=null && actions.keySet().size()>0) {
				return actions;
			}else{
			
				String s = "file:"+binRealPath+"WEB-INF/classes/**/*.class";
				return new ScanClass().getClassName(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toLowerCaseInitial(String srcString, boolean flag) {
	      StringBuilder sb = new StringBuilder();
	      if (flag) {
	           sb.append(Character.toLowerCase(srcString.charAt(0)));
	      } else {
	          sb.append(Character.toUpperCase(srcString.charAt(0)));
	      }
	     sb.append(srcString.substring(1));
	     return sb.toString();
	}

	
	private Map<String,Object> getClassName(String... packagesToScan) throws Exception {
		 
		actions = new Hashtable<String, Object>();
		for (String pkg : packagesToScan) {

			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(pkg)
					+ RESOURCE_PATTERN;

			pattern = pkg;
			System.out.println("pattern:" + pattern);
			Resource[] resources = this.resourcePatternResolver
					.getResources(pattern);
			System.out.println("扫描到资源数:" + resources.length);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
					this.resourcePatternResolver);
			
			for (Resource resource : resources) { 
				if (resource.isReadable()) {

					MetadataReader reader = readerFactory
							.getMetadataReader(resource);
					String className = reader.getClassMetadata().getClassName(); 
					if (matchesEntityTypeFilter(reader, readerFactory)) {
						// this.resourcePatternResolver.getClassLoader().loadClass(className)
						 System.out.println(className);
						String[] tmp = className.split("\\.");  
						
						actions.put(toLowerCaseInitial(tmp[tmp.length-1],true),className);
					} else if (className.endsWith(PACKAGE_INFO_SUFFIX)) {
						// this.resourcePatternResolver.getClassLoader().loadClass(className)

					} else {

						// this.resourcePatternResolver.getClassLoader().loadClass(className)

					}

				}

			}

		}
		return actions;
	}
	
	public static LinkedHashSet<Class<?>>  scanController(String... packagesToScan)  {
		TypeFilter[] filter = new TypeFilter[] { new AnnotationTypeFilter(Controller.class, false) };
		return new ScanClass().scan(filter,packagesToScan);
	}
	
	private static boolean matchesEntityTypeFilter(TypeFilter[] filters,MetadataReader reader,MetadataReaderFactory readerFactory) throws IOException {
		for (TypeFilter filter : filters) {
			if (filter.match(reader, readerFactory)) {
				return true;
			}
		}
		return false;

	}
	
	private LinkedHashSet<Class<?>>  scan(TypeFilter[] filter,String... packagesToScan)  {
		LinkedHashSet<Class<?>> classes = new LinkedHashSet<Class<?>>(); 
		try {
			for (String pkg : packagesToScan) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
				Resource[] resources = resourcePatternResolver.getResources(pattern);
				//Log.info("扫描到资源数 :" + pattern+ resources.length);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (matchesEntityTypeFilter(filter,reader, readerFactory)) {  
							classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
						} else if (className.endsWith(PACKAGE_INFO_SUFFIX)) {
							// this.resourcePatternResolver.getClassLoader().loadClass(className) 
						} else { 
							// this.resourcePatternResolver.getClassLoader().loadClass(className) 
						} 
					} 
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
}

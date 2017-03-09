package net.hsp.common;


public class ClassUtil {
	public static  Class getDynClassByName(String classFullName){
		MyClassLoader m = MyClassLoader.getInstanceII("dev0");
		Thread.currentThread().setContextClassLoader(m);
		Class cls = null;
		try {
			cls = m.findClass(classFullName);

			//注册到spring容器里的bean名称策略,目前是类名首字母小写
			String beanName = cls.getSimpleName();
			System.out.println(beanName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cls;
	}
}

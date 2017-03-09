package net.hsp.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import net.hsp.web.util.PathCon;

import org.springframework.util.ClassUtils;

public class MyClassLoader extends ClassLoader {

	private static MyClassLoader classLoader = null;

	public static final MyClassLoader getInstance(String currentInstance) {
		//System.out.println(ClassUtils.getDefaultClassLoader());
		if (classLoader == null) {
			System.out.println("创建自定义load"+PathCon.BINDIR+"/"+currentInstance+"/"+PathCon.BINFOLDER);
			classLoader = new MyClassLoader(ClassUtils.getDefaultClassLoader(),PathCon.BINDIR+"/"+currentInstance+"/"+PathCon.BINFOLDER);
		}
		return classLoader;
	}
	
	public static final MyClassLoader getInstanceII(String currentInstance) {
		System.out.println("创建自定义load"+PathCon.BINDIR+"/"+PathCon.BINFOLDER);
		return new MyClassLoader(ClassUtils.getDefaultClassLoader(), PathCon.BINDIR+"/"+PathCon.BINFOLDER); 
	}
	

	public static final MyClassLoader reload(String currentInstance) {
		System.out.println("重新loader classLoader");
		classLoader = new MyClassLoader(ClassUtils.getDefaultClassLoader(), PathCon.BINDIR+"/"+currentInstance+"/"+PathCon.BINFOLDER);
		return classLoader;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}

	private String baseDir;

	private MyClassLoader(ClassLoader parent, String baseDir) {
		super(parent);
		this.baseDir = baseDir;
	}

	// 一个已经加载的类是无法被更新的，如果你试图用同一个ClassLoader再次加载同一个类，就会得到异常（java.lang.LinkageError:
	// duplicate class definition），我们只能够重新创建一个新的
	// ClassLoader实例来再次加载新类。至于原来已经加载的类，开发人员不必去管它，因为它可能还有实例正在被使用，只要相关的实例都被内存回收了，那么JVM就会在适当的时候把不会再使用的类卸载
	@Override
	public Class findClass(String name) throws ClassNotFoundException {
		byte[] bytes = loadClassBytes(name);
		Class theClass = null;
		try {
			theClass = defineClass(name, bytes, 0, bytes.length);
		} catch (java.lang.ClassFormatError e) {
			e.printStackTrace();
		} catch (java.lang.LinkageError e2) {
			System.out.println("此classLoader已经加载过此类!" + name);
		}
		return theClass;
	}

	private byte[] loadClassBytes(String className) throws ClassNotFoundException {
		try {
			System.out.println("经过自定义的defineClass(网络查找class)" + className);
			String classFile = getClassFile(className);
			FileInputStream fis = new FileInputStream(classFile);
			FileChannel fileC = fis.getChannel();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			WritableByteChannel outC = Channels.newChannel(baos);
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			try {
				while (true) {
					int i = fileC.read(buffer);
					if (i == 0 || i == -1) {
						break;
					}
					buffer.flip();
					outC.write(buffer);
					buffer.clear();
				}
			} finally {
				fis.close();
			}

			return baos.toByteArray();

		} catch (IOException fnfe) {
			throw new ClassNotFoundException(className);
		}
	}

	private String getClassFile(String name) {
		StringBuffer sb = new StringBuffer(baseDir);
		name = name.replace('.', File.separatorChar) + ".class";
		sb.append(File.separator + name);
		System.out.println(name);
		return sb.toString();
	}
}

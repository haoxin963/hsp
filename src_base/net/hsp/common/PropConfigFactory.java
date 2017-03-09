package net.hsp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

/**
 * 定义properties类配置文件处理的抽象类，统一property配置文件读取方式,
 * 所有的property类的配置文件读取处理都可以继承此类，实现父类相应参数和方法即可在此基础上继续处理.
 * 
 * @author pengyq
 */
public abstract class PropConfigFactory
{
	private Date oldDate;
	private Properties properties = new Properties();
	private String filePath ="";

	protected abstract String getFile();

	/**
	 * 根据key值获取配置文件中的属性值
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		reload();
		return properties.getProperty(key);
	}
	
	/**
	 * 在配置文件中添加key值
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key,String value)
	{
		try
		{
			reload();
			FileOutputStream ops = new FileOutputStream(filePath);
			properties.setProperty(key, value); 
			properties.store(ops, "set "+key); 
			ops.close();
		} catch (Exception e)
		{
			throw new RuntimeException("Add property error: "+getFile(),e);
		} 
	}
	
	/**
	 * 删除配置文件指定的key
	 * 
	 * @param key
	 */
	public void removeProperty(String key)
	{
		try
		{
			reload();
			FileOutputStream ops = new FileOutputStream(filePath);
			properties.remove(key); 
			properties.store(ops, "remove "+key); 
			ops.close();
		} catch (Exception e)
		{
			throw new RuntimeException("Remove property error: "+getFile(),e);
		} 
	}

	private void reload()
	{
		try
		{
			filePath = PathUtil.getRootPath() + getFile();
			Date readNewDate = new java.util.Date(new File(filePath).lastModified());
			if (oldDate == null || oldDate.before(readNewDate))
			{
				FileInputStream input = new FileInputStream(filePath);
				properties.load(input);
				oldDate = readNewDate;
				input.close();
			}
		} catch (Exception e)
		{
			throw new RuntimeException("Reload error: "+getFile(),e);
		}
	}
}

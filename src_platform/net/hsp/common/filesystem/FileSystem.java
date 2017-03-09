package net.hsp.common.filesystem;

import java.util.Date;

public class FileSystem
{
	private String	name;		//文件名
	private long	size;		//文件大小
	private Date	modifyDate;	//修改日期
	private boolean isFile;		//是否文件，false-目录,true-文件

	/**
	 * @return the isFile
	 */
	public boolean isFile()
	{
		return isFile;
	}

	/**
	 * @param isFile the isFile to set
	 */
	public void setFile(boolean isFile)
	{
		this.isFile = isFile;
	}

	/**
	 * 获取文件名
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 存储文件名
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 获取文件大小
	 * @return
	 */
	public long getSize()
	{
		return size;
	}

	/**
	 * 存储文件大小
	 * @param size
	 */
	public void setSize(long size)
	{
		this.size = size;
	}

	/**
	 * 获取修改日期
	 * @return
	 */
	public Date getModifyDate()
	{
		return modifyDate;
	}

	/**
	 * 存储修改日期
	 * @param modifyDate
	 */
	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}

	/**
	 * 获取字符串标识
	 */
	public String toString()
	{
		return this.name;
	}

}

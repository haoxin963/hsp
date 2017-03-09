package net.hsp.common.filesystem;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public interface IFileSystemStream
{

	/**
	 * 读取文件流
	 * @return
	 */
	public InputStream read();

	/**
	 * 对指定的文件写如文件流，默认写在文件开始
	 * @param os
	 * @return
	 */
	public boolean write(InputStream os);

	/**
	 * 对指定的文件写如文件流，isAppend如果为 true，
	 * 则将字节写入文件末尾处，而不是写入文件开始处
	 * @param os
	 * @param isAppend
	 * @return
	 * @throws Exception 
	 */
	public boolean write(InputStream os, boolean isAppend) throws Exception;

	/**
	 * 文件流关闭
	 */
	public void close();

	/**
	 * 文件删除
	 * @return
	 */
	public boolean delete();

	/**
	 * 文件改名
	 * @param dest
	 * @return
	 */
	public boolean ren(String dest);

	/**
	 * 针对目录操作，列出文件夹下的所有FileSystem信息，
	 * 默认只列出文件，不列目录
	 * 
	 * @return
	 */
	public List<FileSystem> list();

	/**
	 * 针对目录操作，列出文件夹下的所有FileSystem信息，
	 * FileSystem类里包含了文件的名称，大小和修改时间
	 * @param 	listDir
	 * 			是否列出目录
	 * @return
	 */
	public List<FileSystem> list(boolean listDir);

	/**
	 * 文件大小
	 * @return
	 */
	public long size();

	/**
	 * 文件修改时间
	 * @return
	 */
	public Date modifyDate();

	/**
	 * 文件是否存在
	 * @return
	 */
	public boolean exists();

	/**
	 * 文件系统是否有效
	 * @return
	 */
	public boolean isValidate();

	/**
	 * 获取站点所使用总空间
	 * @return
	 */
	public long getLength();
	
	/**
	 * 获取文件的绝对路径
	 * @return
	 */
	public String getDir();
	
	/**
	 * 目录拷贝
	 * @param to  新的位置
	 * @return copy成功为true
	 */
	public boolean copy(IFileSystemStream to);

}

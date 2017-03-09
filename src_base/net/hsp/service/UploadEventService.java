package net.hsp.service;

import java.util.Map;

public interface UploadEventService {

	/**
	 * 
	 * 文件写入磁盘前调用此方法，可以动态改变写入的位置,文件名等,根据场景一般异常时继续抛出，可以在前端页面捕获,
	 * 由于上传线程依赖此方法的返回值，在修改参数map后应当保持map的以下参数有值,如果是空实现时直接return 入参
	 * 
	 * @param param
	 *            <li>页面配置：文件写入目录filePath</li>
	 *            <li>文件系统根路径rootPath</li>
	 *            <li>页面配置：fileSize</li>
	 *            <li>页面配置：isDB</li>
	 *            <li>custId</li>
	 *            <li>容器所在真实路径realPath</li>
	 * @return
	 * @throws ServiceException
	 */
	public Map uploadBefore(Map param) throws ServiceException;

	/**
	 * 
	 * 文件写入磁盘后调用此方法，可以根据文件完成其它扩展任务,根据场景一般异常时继续抛出，可以在前端页面捕获
	 * 
	 * @param param
	 *            <li>页面配置：文件写入目录filePath</li>
	 *            <li>文件系统根路径rootPath</li>
	 *            <li>页面配置：fileSize</li>
	 *            <li>页面配置：isDB</li>
	 *            <li>custId</li>
	 *            <li>容器所在真实路径realPath</li>
	 * @return
	 * @throws ServiceException
	 */
	public Map uploadAfter(Map param) throws ServiceException;
}

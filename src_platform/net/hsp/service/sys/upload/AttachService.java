package net.hsp.service.sys.upload;

import java.util.List;

import net.hsp.entity.sys.upload.Attachment;
import net.hsp.service.BaseService;


public interface AttachService extends BaseService  {

	/**
	 * Insert or Update Attachment Object
	 */
	public Attachment saveAttachment (Attachment attachment);
	
	/**
	 * Use to SWFUpload
	 * Delete Attachment by FileName and FilePath
	 */
	public void delAttachmentByNameAndPath (Attachment attachment);
	
	/**
	 * Use to SWFUpload
	 * 根据路径查找目标目录所有文件
	 * @param filePath
	 * @return
	 */
	public List<String> getAttListFromPath(String custId,String filePath);
	
	/**
	 * 获取唯一标识UUID
	 * @param custId
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public String getKeyuuid(String custId,String filePath,String fileName);
}
package net.hsp.service.sys.upload;

import java.util.List;
import java.util.Map;

import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.upload.Attachment;
import net.hsp.service.BaseServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@ServiceLogUtil(name = "用户业务")
public class AttachServiceImpl extends BaseServiceImpl  implements AttachService {

	/**
	 * Insert or Update Attachment Object
	 */

	public Attachment saveAttachment (Attachment attachment){
		
		return this.save(attachment);
	}
	
	/**
	 * Delete Attachment Object
	 */
	
	public void delAttachmentByNameAndPath (Attachment attachment) {
		String sql="delete from pubmodule_attachments_tbl where custId='"+attachment.getCustId()+"' and filename='"+attachment.getFileName()+"' and filepath='"+attachment.getFilePath()+"'";
		baseDAO.execute(sql);
	}
	

	/**
	 * 根据站点及path返回对应文件名
	 */
	public List<String> getAttListFromPath(String custId,String filePath)
	{
		if(StringUtils.isBlank(custId) || StringUtils.isBlank(filePath)){
			return null;
		}
		String sql="select fileName from pubmodule_attachments_tbl where custId=?  and filepath= ?";
		return this.getDAO().queryForList(sql, new String[]{custId,filePath}, String.class); 
	}
	
	/**
	 * 获取keyuuid
	 * @param custId
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public String getKeyuuid(String custId,String filePath,String fileName) {
		if(StringUtils.isBlank(custId) || StringUtils.isBlank(filePath) || StringUtils.isBlank(fileName)){
			return null;
		}
		String sql="select keyUUID from pubmodule_attachments_tbl where custId=?  and filepath= ? and fileName=?";
		List<String> list = this.getDAO().queryForList(sql, new String[]{custId,filePath,fileName}, String.class); 
		if(list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}
}

package net.hsp.web.sys.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.hsp.common.constants.PlatFormConstant;
import net.hsp.common.filesystem.FileSystemUtil;
import net.hsp.entity.sys.upload.Attachment;
import net.hsp.service.sys.upload.AttachService;
import net.hsp.web.BaseAction;
import net.hsp.web.sys.fileupload.utils.SwfuploadUtil;
import net.hsp.web.util.HttpSessionFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

@Controller
@RequestMapping("/pub/upload")
@Scope("prototype")
public class UploadController  extends BaseAction<Attachment>{
	

	@Autowired
	private AttachService attachServiceImpl;
	
	public String defaultType="";
	
	@Override
	protected void init() {}
	
	
	public int addAttachBatch(List attchList){
		return 0;
	}
	
	public void delAttachByNameAndPath(Attachment attachment) {
		attachServiceImpl.delAttachmentByNameAndPath(attachment);
	}
	
	public String getAttListFromPath(String module){
		return "";
	}


	
	@RequestMapping("/fileUpload")
	public void fileUpload(MultipartHttpServletRequest request, HttpServletResponse response) {
		try {
			SwfuploadUtil swfuploadUtil = SwfuploadUtil.getInstance();
			defaultType = swfuploadUtil.getValue("default.type");
			String fileSize=swfuploadUtil.getValue("default.fileSize"); 
			String custId = HttpSessionFactory.getCustId(request);
			String rootPath=FileSystemUtil.getInstance().getProperty(custId+".filesystem.root"); 
			String filePath=request.getParameter("module");
			
			if(StringUtils.isNotBlank(filePath)){
				if(!SwfuploadUtil.isFirstSeparator(filePath))
					filePath=File.separator+filePath;
			}else{
				filePath=File.separator+"swfupload";
			}
					
			boolean isDB=false;
			if(StringUtils.isNotBlank(request.getParameter("isDB"))&&"true".equals(request.getParameter("isDB").toLowerCase())){
				isDB=true;
			}
			if(StringUtils.isNotBlank(request.getParameter("fileSize"))){
				fileSize=request.getParameter("fileSize");
			}

			String opt = request.getParameter("opt");
			if(StringUtils.isNotBlank(opt)&&opt.equals("upload"))
				swfUpload(request,response,rootPath,filePath,fileSize,isDB);
			if(StringUtils.isNotBlank(opt)&&opt.equals("delete"))
				delete(request,response,rootPath,filePath,isDB);
			

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
//==============================================UploadServlet=========================================================
	
	public void swfUpload(MultipartHttpServletRequest request, HttpServletResponse response,String rootPath,String filePath,String fileSize,boolean isDB) throws ServletException, IOException {
		String realPath=rootPath+filePath;
		File savePath = new File(realPath);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
		int maxSize = Integer.parseInt(fileSize) * 1024;
		String encoding = "UTF-8";
		
		
		MultipartParser mp = new MultipartParser(request, maxSize, false, false, encoding);
	    Part part;
	    while ((part = mp.readNextPart()) != null) {
		    if (part.isFile()) {
				FilePart filePart = (FilePart) part;
				String fileName = filePart.getFileName();
				String custId = HttpSessionFactory.getCustId(request);
				if (fileName != null && checkType(fileName)) {
					fileName = getNewFileName(realPath,fileName);
					filePart.writeTo(new File(realPath + File.separator + fileName));
					//入库操作
					if(isDB) insertDB(filePath,fileName,custId);
					response.getWriter().print(fileName);
				}else{
					response.getWriter().print("文件格式错误");
				}
		    }
	    }
	}

	public void delete(HttpServletRequest request, HttpServletResponse response,String rootPath,String filePath,boolean isDB) throws ServletException, IOException {
		String realPath=rootPath+filePath;
		String fileName = request.getParameter("filePath");
		PrintWriter out = response.getWriter();
		String custId = HttpSessionFactory.getCustId(request);
		File file = new File(realPath + File.separator + fileName);
		if(file.exists()){
			file.delete();
			out.print("success");
		}
		//删除记录
	    if(isDB) deleteDB(filePath,fileName,custId);
	}
	
	private void insertDB(String filePath,String fileName,String custId) {
		
		Attachment attachment = new Attachment();
		attachment.setCustId(custId);
		attachment.setFilePath(filePath);
		attachment.setFileName(fileName);
		attachment.setStatus(1);
		attachment.setKeyUuid(UUID.randomUUID().toString()); 
	}
	
	private void deleteDB(String filePath,String fileName,String custId) {
		
		Attachment attachment = new Attachment();
		attachment.setCustId(custId);
		attachment.setFilePath(filePath);
		attachment.setFileName(fileName);
		attachment.setStatus(1);
		attachment.setKeyUuid(UUID.randomUUID().toString());
	}
	
	private boolean checkType(String fileName){
		if(StringUtils.isBlank(fileName))
			return false;
		String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		String[] exts = defaultType.toLowerCase().split(";");
		for(int i=0;i<exts.length;i++){
			String ex=exts[i];
			if(ex.indexOf(ext)>0)
				return true;
		}
		return false;
	}
	
	private String getNewFileName(String path,String fileName){
		String newFileName = fileName;
		int i=1;
		File file = new File(path + File.separator + newFileName);
		while(file.isFile()){
			newFileName = fileName.substring(0,fileName.lastIndexOf(".")) +"("+ i++ +")" + fileName.substring(fileName.lastIndexOf("."));
			file = new File(path + File.separator + newFileName);
		}
		return newFileName;
	}
}

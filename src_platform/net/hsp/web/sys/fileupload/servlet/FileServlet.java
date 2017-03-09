package net.hsp.web.sys.fileupload.servlet;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.common.CompressPicUtil;
import net.hsp.common.SystemContext;
import net.hsp.common.filesystem.FileSystemUtil;
import net.hsp.entity.sys.upload.Attachment;
import net.hsp.service.UploadEventService;
import net.hsp.service.sys.upload.AttachService;
import net.hsp.web.sys.fileupload.utils.FilesTransform;
import net.hsp.web.sys.fileupload.utils.SwfuploadUtil;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.SpringCtx;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

/**
 * @author LK0378 尹文强
 * 
 */
public class FileServlet extends HttpServlet {

	private static final long serialVersionUID = -3096800116651263134L;
	public static String defaultType = "";
	static Map<String, String> mime = new HashMap<String, String>();
	static {
		mime.put("csh", "application/x-csh");
		mime.put("wmlc", "application/vnd.wap.wmlc");
		mime.put("asf", "video/x-ms-asf");
		mime.put("body", "text/html");
		mime.put("m3u", "audio/x-mpegurl");
		mime.put("art", "image/x-jg");
		mime.put("odt", "application/vnd.oasis.opendocument.text");
		mime.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		mime.put("zip", "application/zip");
		mime.put("odp", "application/vnd.oasis.opendocument.presentation");
		mime.put("ief", "image/ief");
		mime.put("odm", "application/vnd.oasis.opendocument.text-master");
		mime.put("vxml", "application/voicexml+xml");
		mime.put("odi", "application/vnd.oasis.opendocument.image");
		mime.put("html", "text/html");
		mime.put("cer", "application/x-x509-ca-cert");
		mime.put("odg", "application/vnd.oasis.opendocument.graphics");
		mime.put("odf", "application/vnd.oasis.opendocument.formula");
		mime.put("odc", "application/vnd.oasis.opendocument.chart");
		mime.put("odb", "application/vnd.oasis.opendocument.database");
		mime.put("oda", "application/oda");
		mime.put("xul", "application/vnd.mozilla.xul+xml");
		mime.put("ustar", "application/x-ustar");
		mime.put("xslt", "application/xslt+xml");
		mime.put("ceb", "application/x-ceb");
		mime.put("mpg", "video/mpeg");
		mime.put("mpe", "video/mpeg");
		mime.put("tif", "image/tiff");
		mime.put("nc", "application/x-netcdf");
		mime.put("xht", "application/xhtml+xml");
		mime.put("mpa", "audio/x-mpeg");
		mime.put("au", "audio/basic");
		mime.put("eps", "application/postscript");
		mime.put("mov", "video/quicktime");
		mime.put("ai", "application/postscript");
		mime.put("pic", "image/pict");
		mime.put("ms", "application/x-wais-source");
		mime.put("cdf", "application/x-cdf");
		mime.put("pict", "image/pict");
		mime.put("jpeg", "image/jpeg");
		mime.put("wav", "audio/x-wav");
		mime.put("rtx", "text/richtext");
		mime.put("me", "application/x-troff-me");
		mime.put("dvi", "application/x-dvi");
		mime.put("wmv", "video/x-ms-wmv");
		mime.put("snd", "audio/basic");
		mime.put("mp4", "video/mp4");
		mime.put("rtf", "application/rtf");
		mime.put("mp3", "audio/x-mpeg");
		mime.put("mp2", "audio/x-mpeg");
		mime.put("wml", "text/vnd.wap.wml");
		mime.put("mp1", "audio/x-mpeg");
		mime.put("xsl", "application/xml");
		mime.put("bin", "application/octet-stream");
		mime.put("tsv", "text/tab-separated-values");
		mime.put("dib", "image/bmp");
		mime.put("vsd", "application/x-visio");
		mime.put("pgm", "image/x-portable-graymap");
		mime.put("smf", "audio/x-midi");
		mime.put("man", "application/x-troff-man");
		mime.put("rgb", "image/x-rgb");
		mime.put("kar", "audio/x-midi");
		mime.put("htm", "text/html");
		mime.put("ulw", "audio/basic");
		mime.put("abs", "audio/x-mpeg");
		mime.put("cpio", "application/x-cpio");
		mime.put("mac", "image/x-macpaint");
		mime.put("htc", "text/x-component");
		mime.put("psd", "image/x-photoshop");
		mime.put("qtif", "image/x-quicktime");
		mime.put("sv4crc", "application/x-sv4crc");
		mime.put("roff", "application/x-troff");
		mime.put("dtd", "application/xml-dtd");
		mime.put("bcpio", "application/x-bcpio");
		mime.put("latex", "application/x-latex");
		mime.put("tex", "application/x-tex");
		mime.put("jsf", "text/plain");
		mime.put("js", "text/javascript");
		mime.put("texi", "application/x-texinfo");
		mime.put("wbmp", "image/vnd.wap.wbmp");
		mime.put("java", "text/plain");
		mime.put("midi", "audio/x-midi");
		mime.put("swf", "application/x-shockwave-flash");
		mime.put("xpm", "image/x-xpixmap");
		mime.put("shar", "application/x-shar");
		mime.put("mathml", "application/mathml+xml");
		mime.put("hqx", "application/mac-binhex40");
		mime.put("rdf", "application/rdf+xml");
		mime.put("exe", "application/octet-stream");
		mime.put("ppt", "application/powerpoint");
		mime.put("pdf", "application/pdf");
		mime.put("ppm", "image/x-portable-pixmap");
		mime.put("svg", "image/svg+xml");
		mime.put("tcl", "application/x-tcl");
		mime.put("sit", "application/x-stuffit");
		mime.put("z", "application/x-compress");
		mime.put("tr", "application/x-troff");
		mime.put("pct", "image/pict");
		mime.put("mpv2", "video/mpeg2");
		mime.put("t", "application/x-troff");
		mime.put("jnlp", "application/x-java-jnlp-file");
		mime.put("xbm", "image/x-xbitmap");
		mime.put("hdf", "application/x-hdf");
		mime.put("gz", "application/x-gzip");
		mime.put("jpg", "image/jpeg");
		mime.put("wspolicy", "application/wspolicy+xml");
		mime.put("jpe", "image/jpeg");
		mime.put("sv4cpio", "application/x-sv4cpio");
		mime.put("texinfo", "application/x-texinfo");
		mime.put("Z", "application/x-compress");
		mime.put("mif", "application/x-mif");
		mime.put("mid", "audio/x-midi");
		mime.put("avx", "video/x-rad-screenplay");
		mime.put("pbm", "image/x-portable-bitmap");
		mime.put("sh", "application/x-sh");
		mime.put("pnt", "image/x-macpaint");
		mime.put("mht", "text/x-mht");
		mime.put("aiff", "audio/x-aiff");
		mime.put("avi", "video/x-msvideo");
		mime.put("pnm", "image/x-portable-anymap");
		mime.put("tar", "application/x-tar");
		mime.put("aifc", "audio/x-aiff");
		mime.put("gif", "image/gif");
		mime.put("ott", "application/vnd.oasis.opendocument.text-template");
		mime.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template ");
		mime.put("xml", "application/xml");
		mime.put("otp", "application/vnd.oasis.opendocument.presentation-template");
		mime.put("png", "image/png");
		mime.put("qti", "image/x-quicktime");
		mime.put("ras", "image/x-cmu-raster");
		mime.put("oth", "application/vnd.oasis.opendocument.text-web");
		mime.put("otg", "application/vnd.oasis.opendocument.graphics-template");
		mime.put("aim", "application/x-aim");
		mime.put("rm", "application/vnd.rn-realmedia");
		mime.put("jspf", "text/plain");
		mime.put("doc", "application/msword");
		mime.put("aif", "audio/x-aiff");
		mime.put("gtar", "application/x-gtar");
		mime.put("xls", "application/msexcel");
		mime.put("etx", "text/x-setext");
		mime.put("ogg", "application/ogg");
		mime.put("jar", "application/java-archive");
		mime.put("qt", "video/quicktime");
		mime.put("mpeg", "video/mpeg");
		mime.put("tiff", "image/tiff");
		mime.put("wrl", "x-world/x-vrml");
		mime.put("movie", "video/x-sgi-movie");
		mime.put("jad", "text/vnd.sun.j2me.app-descriptor");
		mime.put("txt", "text/plain");
		mime.put("class", "application/java");
		mime.put("pls", "audio/x-scpls");
		mime.put("svgz", "image/svg+xml");
		mime.put("dv", "video/x-dv");
		mime.put("mpega", "audio/x-mpeg");
		mime.put("src", "application/x-wais-source");
		mime.put("xhtml", "application/xhtml+xml");
		mime.put("ps", "application/postscript");
		mime.put("css", "text/css");
		mime.put("bmp", "image/bmp");
		mime.put("asx", "video/x-ms-asf");
		mime.put("wmls", "text/vnd.wap.wmlscript");
		mime.put("xwd", "image/x-xwindowdump");
		mime.put("apk", "application/apk");
	}

	private final Logger log = Logger.getLogger(FileServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String cmd = request.getParameter("cmd");

		if (cmd == null || cmd.trim().equals("")) {
			log.error("cmd为空");
			return;
		} else {
			cmd = cmd.trim().toLowerCase();
		}

		// 命令分类开始。
		if (cmd.equals("upload")) {
			uploadFile(request, response);
			return;
		} else if (cmd.equals("download") || cmd.equals("open") || cmd.equals("preview")) {
			initFile(request, response, cmd);
			return;
		} else if(cmd.equals("list")) {
			listFiles(request, response);
			return;
		} else {
			return;
		}
	}

	private void initFile(HttpServletRequest request, HttpServletResponse response, String cmd) throws ServletException, IOException {

		String fileName = request.getParameter("fileName");
		if (fileName == null) {
			log.error("fileName为空");
			return;
		}
		String filePath = request.getParameter("filePath");
		if (filePath == null) {
			log.error("fileName为空");
			return;
		}
		String custId = HttpSessionFactory.getCustId(request);
		if (StringUtils.isBlank(custId)) {
			custId = request.getParameter("custId");// 未登录的用户直接在链接里携带custId
		}
		String rootPath = FileSystemUtil.getInstance().getProperty(custId + ".filesystem.root") + "/" + custId;
		String realPath = rootPath + File.separator + filePath + File.separator + fileName;

		File file = new File(realPath);
		if (!file.exists()) {
			log.error("文件不存在:" + realPath);
			return;
		}
		long size = 0;
		InputStream fis = new FileInputStream(file);
		size = fis.available();

		if (cmd.equals("download")) {
			downloadFile(response, fis, fileName, size);
		} else if ("preview".equals(cmd)) {
			previewFile(request, response, realPath,filePath, fileName);
		} else {
			openFile(response, fis, fileName, size);
		}
	}

	private void previewFile(HttpServletRequest request, HttpServletResponse response, String realPath,String filePath, String fileName) {
		AttachService attachService = (AttachService) SpringCtx.getSpringContext().getBean("attachServiceImpl");
		String custId = HttpSessionFactory.getCustId(request);
		String keyuuid  = attachService.getKeyuuid(custId, filePath, fileName);
		String swffileName = keyuuid+".swf";
		String pdffileName = keyuuid+".pdf";
		String rootPath = FileSystemUtil.getInstance().getProperty(custId + ".filesystem.root") + "/" + custId;
		String swfFilePath = rootPath + File.separator + filePath + File.separator + swffileName;
		String pdfFilePath = rootPath + File.separator + filePath + File.separator + pdffileName;
		String[] preview_suffix = { "txt", "doc", "docx", "ppt", "pptx", "pdf", "xlsx", "xls", "swf" };
		if (checkSuffix(preview_suffix, fileName)) {
			File sourceFile = new File(realPath);
			File swfFile = new File(swfFilePath);
			File pdfFile = new File(pdfFilePath);
			if (!swfFile.exists()) {
				if (pdfFile.exists()) {
					FilesTransform.pdfToSwf2(pdfFile, swfFile);
				} else {
					if (sourceFile.exists()) {
						String fileType = getFileType(fileName);
						if (fileType.equalsIgnoreCase("txt")) {
							FilesTransform.textToPdf(realPath, pdfFilePath);
						} else {
							try {
								FilesTransform.officeToPdf(sourceFile, pdfFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (pdfFile.exists()) {
							FilesTransform.pdfToSwf(pdfFile, swfFile);
						}
					}
				}
			}
			if (swfFile.exists()) {
				try {
					response.sendRedirect(ActionUtil.getCtx().getServletContext().getAttribute("path") + "/com/flexPaper/resourceRead.jsp?filePath=" + filePath + "&fileName=" + swffileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SwfuploadUtil swfuploadUtil = SwfuploadUtil.getInstance();
		defaultType = swfuploadUtil.getValue("default.type");
		String fileSize = swfuploadUtil.getValue("default.fileSize");
		String custId = HttpSessionFactory.getCustId(request);
		if (StringUtils.isBlank(custId)) {
			log.error("无效的custId");
			return;
		}
		String rootPath = FileSystemUtil.getInstance().getProperty(custId + ".filesystem.root") + "/" + custId;
		String filePath = request.getParameter("filePath");

		if (StringUtils.isNotBlank(filePath)) {
			if (!SwfuploadUtil.isFirstSeparator(filePath))
				filePath = File.separator + filePath;
		} else {
			filePath = File.separator + "swfupload";
		}

		boolean isDB = true;
		if ("false".equals(request.getParameter("isDB"))) {
			isDB = false;
		}

		if (StringUtils.isNotBlank(request.getParameter("fileSize"))) {
			fileSize = request.getParameter("fileSize");
		}
		String opt = request.getParameter("opt");
		if (StringUtils.isNotBlank(opt) && opt.equals("upload")) {
			String interceptor = request.getParameter("interceptor");
			if (StringUtils.isNotBlank(interceptor)) {
				UploadEventService service = (UploadEventService) SpringCtx.getSpringContext().getBean(interceptor);
				Map m = new HashMap();
				m.put("filePath", filePath);
				m.put("rootPath", rootPath);
				m.put("fileSize", fileSize);
				m.put("isDB", isDB);
				m.put("custId", custId);
				m.put("realPath", SystemContext.getProperty("webApp.root"));
				m = service.uploadBefore(m);
				swfUpload(request, response, m.get("rootPath").toString(), m.get("filePath").toString(), m.get("fileSize").toString(), Boolean.valueOf(m.get("isDB").toString()), m.get("custId").toString());
			} else {
				swfUpload(request, response, rootPath, filePath, fileSize, isDB, custId);
			}
		}
		if ("delete".equals(opt)) {
			delete(request, response, rootPath, filePath, isDB, custId);
		}
	}

	private void openFile(HttpServletResponse response, InputStream fis, String fileName, long size) throws ServletException, IOException {
		if (fis != null) {
			BufferedInputStream bufferedInputStream = null;
			ServletOutputStream output = null;
			byte[] buffer = new byte[1024];
			int len = 0;
			try {
				if (!response.isCommitted()) {
					response.reset();
				}
				response.setCharacterEncoding("UTF-8");
				fileName = fileName.toLowerCase();
				String extname = fileName.substring(fileName.lastIndexOf(".") + 1);
				String contentType = mime.get(extname.toLowerCase());
				if (contentType == null) {
					contentType = "application/octet-stream";
				}
				response.setContentType(contentType);
				response.addHeader("Content-length", "" + size);
				fileName = new String(fileName.getBytes("gbk"), "iso-8859-1");
				response.addHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
				response.setBufferSize(8192);
				bufferedInputStream = new BufferedInputStream(fis);
				output = response.getOutputStream();
				while ((len = bufferedInputStream.read(buffer)) != -1) {
					output.write(buffer, 0, len);
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			} finally {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (output != null) {
					output.close();
				}
			}
		}
	}

	private void downloadFile(HttpServletResponse response, InputStream fis, String fileName, long size) throws ServletException, IOException {
		if (fis != null) {
			BufferedInputStream bufferedInputStream = null;
			ServletOutputStream output = null;
			byte[] buffer = new byte[1024];
			int len = 0;
			try {
				if (!response.isCommitted()) {
					response.reset();
				}
				response.setCharacterEncoding("UTF-8");
				fileName = fileName.toLowerCase();
				String extname = fileName.substring(fileName.lastIndexOf(".") + 1);
				String contentType = mime.get(extname.toLowerCase());
				if (contentType == null) {
					contentType = "application/octet-stream";
				}
				response.setContentType(contentType);
				response.addHeader("Content-length", "" + size);
				fileName = new String(fileName.getBytes("gbk"), "ISO8859-1");
				response.addHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
				response.setBufferSize(8192);
				bufferedInputStream = new BufferedInputStream(fis);
				output = response.getOutputStream();
				while ((len = bufferedInputStream.read(buffer)) != -1) {
					output.write(buffer, 0, len);
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			} finally {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (output != null) {
					output.close();
				}
			}
		}
	}

	private void swfUpload(HttpServletRequest request, HttpServletResponse response, String rootPath, String filePath, String fileSize, boolean isDB, String custId) throws ServletException, IOException {
		Map<String, String> result = new HashMap();
		result.put("status", "1");
		String realPath = rootPath + filePath;
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
				String picWidth = request.getParameter("picWidth");
				if (fileName != null && checkType(fileName)) {
					long len = 0;
					String lockFileName = request.getParameter("lockFileName");
					if (StringUtils.isNotBlank(lockFileName)) {
						fileName = lockFileName;
					} else {
						fileName = getNewFileName(realPath, fileName);
					}
					if (StringUtils.isNotBlank(picWidth)) {
						File tempFile = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID() + fileName);
						len = filePart.writeTo(tempFile);
						if (len > 0) {
							BufferedImage bufferedImage = ImageIO.read(tempFile);
							int width = bufferedImage.getWidth();
							int height = bufferedImage.getHeight();
							if (width != Integer.parseInt(picWidth) || height != Integer.parseInt(request.getParameter("picHeight"))) {
								result.put("msg", "图片宽高不正确(" + width + "*" + height + ")");
								result.put("status", "0");
								response.getWriter().print(JSONObject.fromObject(result).toString());
								return;
							} else {
								FileUtils.copyFile(tempFile, new File(realPath + File.separator + fileName));
								tempFile.delete();
							}
						}
					}

					if (len == 0) {
						filePart.writeTo(new File(realPath + File.separator + fileName));
					}
					String compressPic = request.getParameter("compressPic");
					if (StringUtils.isNotBlank(compressPic) && checkPicSuffix(fileName)) {
						String compressPics[] = compressPic.split(",");
						if (compressPics.length == 5) {
							String rname = compressPics[0];
							String w = compressPics[1];
							String h = compressPics[2];
							String proportion = compressPics[3];
							String quality = compressPics[4];
							CompressPicUtil com = new CompressPicUtil();
							String newName = fileName;
							if (!"0".equals(rname)) {
								newName = rname + fileName;
							}
							com.compressPic(realPath, realPath, fileName, newName, Integer.valueOf(w), Integer.valueOf(h), Float.valueOf(proportion), Float.valueOf(quality));
						}
					}
					String interceptor = request.getParameter("interceptor");
					if (StringUtils.isNotBlank(interceptor)) {
						UploadEventService service = (UploadEventService) SpringCtx.getSpringContext().getBean(interceptor);
						Map m = new HashMap();
						m.put("fileName", fileName);
						m.put("filePath", filePath);
						m.put("rootPath", rootPath);
						m.put("fileSize", fileSize);
						m.put("isDB", isDB);
						m.put("custId", custId);
						m.put("realPath", SystemContext.getProperty("webApp.root"));
						service.uploadAfter(m);
					}
					// 入库操作
					if (isDB)
						insertDB(custId, filePath, fileName);
					result.put("name", fileName);
					response.getWriter().print(JSONObject.fromObject(result).toString());
				} else {
					result.put("msg", "文件格式错误");
					result.put("status", "0");
					response.getWriter().print(JSONObject.fromObject(result).toString());
				}
			}
		}
	}

	private void delete(HttpServletRequest request, HttpServletResponse response, String rootPath, String filePath, boolean isDB, String custId) throws ServletException, IOException {
		String realPath = rootPath + filePath;
		String fileName = request.getParameter("fileName");
		PrintWriter out = response.getWriter();
		File file = new File(realPath + File.separator + fileName);
		if (file.exists()) {
			file.delete();
			Map result = new HashMap();
			result.put("status", "1");
			result.put("name", fileName);
			out.print(JSONObject.fromObject(result).toString());
		}
		// 删除记录
		if (isDB)
			deleteDB(custId, filePath, fileName);
	}

	private void insertDB(String custId, String filePath, String fileName) {

		Attachment attachment = new Attachment();
		attachment.setCustId(custId);
		attachment.setFilePath(filePath);
		attachment.setFileName(fileName);
		attachment.setStatus(1);
		attachment.setKeyUuid(UUID.randomUUID().toString());

		AttachService attachService = (AttachService) SpringCtx.getSpringContext().getBean("attachServiceImpl");
		attachService.saveAttachment(attachment);
	}

	private static void deleteDB(String custId, String filePath, String fileName) {

		Attachment attachment = new Attachment();
		attachment.setCustId(custId);
		attachment.setFilePath(filePath);
		attachment.setFileName(fileName);
		AttachService attachService = (AttachService) SpringCtx.getSpringContext().getBean("attachServiceImpl");
		attachService.delAttachmentByNameAndPath(attachment);
	}
	
	private static void listFiles(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String custId = HttpSessionFactory.getCustId(request);
		String filePath = request.getParameter("filePath");
		Map<String,Object> result = new HashMap<String,Object>();
		if(StringUtils.isBlank(filePath)) {
			result.put("msg", "文件路径为空");
			result.put("status", "0");
		} else {
			AttachService attachService = (AttachService) SpringCtx.getSpringContext().getBean("attachServiceImpl");
			List<String> fileNames = attachService.getAttListFromPath(custId, filePath);
			result.put("status", "1");
			result.put("command", fileNames);
		}
		response.getWriter().print(JSONObject.fromObject(result).toString());
	}

	private static boolean checkType(String fileName) {
		if (StringUtils.isBlank(fileName))
			return false;
		String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		String[] exts = defaultType.toLowerCase().split(";");
		for (int i = 0; i < exts.length; i++) {
			String ex = exts[i];
			if (ex.indexOf(ext) > 0)
				return true;
		}
		return false;
	}

	private static String getNewFileName(String path, String fileName) {
		String newFileName = fileName;
		int i = 1;
		File file = new File(path + File.separator + newFileName);
		while (file.isFile()) {
			newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i++ + ")" + fileName.substring(fileName.lastIndexOf("."));
			file = new File(path + File.separator + newFileName);
		}
		return newFileName;
	}

	public static String[] pic_suffix = { "jpg", "jpeg", "gif", "png" };

	public static boolean checkPicSuffix(String ext) {
		String _ext = ext.toLowerCase();
		if (_ext.lastIndexOf(".") >= 0)
			_ext = _ext.substring(_ext.lastIndexOf(".") + 1);
		for (int i = 0; i < pic_suffix.length; i++) {
			if (pic_suffix[i].equals(_ext))
				return true;
		}
		return false;
	}

	public static boolean checkSuffix(String[] suffix, String ext) {
		String _ext = ext.toLowerCase();
		if (_ext.lastIndexOf(".") >= 0)
			_ext = _ext.substring(_ext.lastIndexOf(".") + 1);
		for (int i = 0; i < suffix.length; i++) {
			if (suffix[i].equals(_ext))
				return true;
		}
		return false;
	}

	public static String getNewFile(String oldFile, String newType) {
		int index = oldFile.lastIndexOf(".");
		if (index != -1) {
			String fileName = oldFile.substring(0, oldFile.lastIndexOf(".") + 1);
			String newFile = fileName + newType;
			return newFile;
		} else {
			return null;
		}
	}

	public static String getFileType(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			String suffix = fileName.substring(index + 1);
			return suffix;
		} else {
			return null;
		}
	}
}

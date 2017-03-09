package net.hsp.web.sys.userSettings;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.common.CompressPicUtil;
import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.common.filesystem.FileSystemUtil;
import net.hsp.entity.sys.org.Employee;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.sys.userSettings.UserSettingsService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Decoder;

/**
 * 员工信息控制器
 * 
 * @author 33
 */
@Controller
@WebLogUtil(name = "员工信息")
@RequestMapping("/sys/userSettings")
@Lazy(true)
public class UserSettingsController {

	private String imageURL = "/sys/userSettings/imageForm";
	private String formURL = "/sys/userSettings/employeeForm";

	@Autowired
	private UserSettingsService userSettingsService;

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(String officeTel, String mobile, String email,
			String employeeId,String jid) {
		try {
			Employee obj = new Employee();
			if (employeeId != null) {
				obj.setId(Long.parseLong(employeeId));
			}else{
				if(jid!=null){
					String userId = jid.substring(0, jid.indexOf("."));
					User user=new User();
					user.setId(Long.parseLong(userId));
					user=userSettingsService.findById(user);
					obj.setId(Long.parseLong(user.getIsCreateStaff()));
				}else{
					String msg="jid为空";
					MV mv = new MV(formURL, null);
					mv.addErrorInfo(msg);
					return mv.fwd();
				}
			}
			obj = userSettingsService.findById(obj);
			if (mobile != null) {
				obj.setMobile(mobile);
			}
			if (officeTel != null) {
				obj.setOfficeTel(officeTel);
			}
			if (email != null) {
				obj.setEmail(email);
			}
			userSettingsService.update(obj);
			return new MV(formURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	public String getUserid(String jid) {
		String userId = "";
		if (StringUtils.isNotBlank(jid)) {
			userId = jid.substring(0, jid.indexOf("."));
		} else {
			userId = ActionUtil.getCtx().getSession()
					.getAttribute(PlatFormConstant.CURRENT_USERID).toString();
		}
		return userId;
	}

	@MethodLogUtil(type = "", value = "保存图片")
	@RequestMapping("/savePhoto1")
	public ModelAndView savePhoto1(MultipartHttpServletRequest req, String jid) {
		try {
			String userId = getUserid(jid);
			MultipartFile cmd = req.getFile("upload-file");
			String filename = cmd.getOriginalFilename();
			if (filename != null && filename != "") {
				filename = userId + ".png";
			}

			String custId = HttpSessionFactory.getCustId(req);
			// 获取文件系统根路径
			saveImage(custId, filename, cmd.getBytes());
			return new MV(formURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "保存图片2")
	@RequestMapping("/savePhoto2")
	public ModelAndView savePhoto2(HttpServletRequest req, String jid) {
		try {
			String userId = getUserid(jid);
			String imgStr = req.getParameter("imageStr");
			imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
			BASE64Decoder decoder = new BASE64Decoder();
			String filename = "";
			filename = userId + ".png";
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			String custId = HttpSessionFactory.getCustId(req);
			saveImage(custId, filename, b);
			return new MV(formURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	public void saveImage(String custId, String filename, byte[] b) {

		// 获取文件系统根路径
		String basePath = FileSystemUtil.getInstance().getProperty(
				custId + ".filesystem.root")
				+ "/" + custId + "/photos/";
		File savePath = new File(basePath);
		if (!savePath.exists()) {
			// 创建文件系统目录
			savePath.mkdirs();
		}
		String realPath = basePath + filename;
		File oldFile = new File(realPath);
		if (oldFile.exists()) {
			// 已经存在先删除
			oldFile.delete();
		}
		File uploadFile = new File(realPath);
		try {
			FileCopyUtils.copy(b, uploadFile);
			CompressPicUtil com = new CompressPicUtil();
			com.compressPic(basePath, basePath, filename, filename, 92, 92, 1,
					0.8f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@MethodLogUtil(type = "", value = "图片编辑")
	@RequestMapping("/doImage")
	public ModelAndView doImage() {
		try {
			return new MV(imageURL, null).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "显示图片")
	@RequestMapping("/viewPhoto")
	public ModelAndView viewPhoto(HttpServletResponse response,
			HttpServletRequest request, String jid) {
		ServletOutputStream output = null;
		MV mv = new MV();
		try {
			String userId = getUserid(jid);
			response.setCharacterEncoding("UTF-8");

			String custId = HttpSessionFactory.getCustId(request);
			String rootPath = FileSystemUtil.getInstance().getProperty(
					custId + ".filesystem.root")
					+ "/" + custId;
			String realPath = rootPath + "/photos/" + userId + ".png";
			File file = new File(realPath);
			String msg = "";
			InputStream fis = null;
			BufferedInputStream bufferedInputStream = null;
			if (file.exists()) {
				fis = new FileInputStream(file);
			} else {
				File def = new File( rootPath + "/photos/1.png");
				fis = new FileInputStream(def);
			}
			if (fis != null) {
				bufferedInputStream = new BufferedInputStream(fis);
			}
			int len = 0;
			output = response.getOutputStream();
			byte[] buffer = new byte[1024];
			while ((len = bufferedInputStream.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}

			return new MV().fwd();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (output != null) {
					output.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Employee obj, String jid) {
		try {
			String userId = getUserid(jid);
			User user = new User();
			user.setId(Long.parseLong(userId));
			String msg = null;
			user = (User) userSettingsService.findById(user);
			if (StringUtils.isNotBlank(user.getIsCreateStaff())) {
				obj.setId(Long.parseLong(user.getIsCreateStaff()));
			} else {
				msg = "该用户不存在员工信息";
			}
			if (obj.getId() != null) {
				obj = (Employee) userSettingsService.findById(obj);
			}
			MV mv = new MV(formURL, WebConstant.COMMAND,
					userSettingsService.queryI(userId));

			if (msg != null) {
				mv.addObject("errmsg", msg);
			}
			// 相关初始数据加载
			// mv.addObject("x","yy");

			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}
}

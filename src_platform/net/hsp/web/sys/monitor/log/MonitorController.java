package net.hsp.web.sys.monitor.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.monitor.log.UserActionLog;
import net.hsp.entity.sys.monitor.log.UserLog;
import net.hsp.service.sys.monitor.log.SqlLogService;
import net.hsp.service.sys.monitor.log.UserLogService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.sys.monitor.pool.ProxoolServlet;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.hsp.web.util.WebUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录日志控制器
 * 
 * @author lk0182
 */
@Controller
@WebLogUtil(name = "系统监控")
@RequestMapping("/sys/monitor")
@Lazy(true)
public class MonitorController {

	private String listURL = "/sys/monitor/log/userLogList";
	private String actionLoglistURL = "/sys/monitor/log/userActionLogList";

	@Autowired
	private UserLogService userLogService;
	@Autowired
	private SqlLogService sqlLogService;

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(UserLog obj, @RequestParam(value = "ids", required = false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				userLogService.batchDelete(obj.getClass(), ids.split(","));
			} else {
				userLogService.delete(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/log/userLog/doList")
	public ModelAndView doList(UserLog userLog, FilterMap filter, PageInfo pageInfo) {
		try {
			Map<String, Object> map = userLogService.queryUserLog(userLog, filter, pageInfo);
			return new MV(listURL, WebConstant.COMMAND, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/log/userLog/doActionLogList")
	public ModelAndView doActionLogList(UserActionLog userActionLog, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(actionLoglistURL, WebConstant.COMMAND, userLogService.queryUserActionLog(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "1", value = "连接池状态查询")
	@RequestMapping("/pool/proxoolList")
	public void proxoolList(HttpServletRequest req, HttpServletResponse res) {

		ProxoolServlet p = new ProxoolServlet();
		try {
			req.setCharacterEncoding("utf-8");
			res.setCharacterEncoding("utf-8");
			p.doGet(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/log/sql/warnLogList")
	public ModelAndView doWarnLogList(FilterMap filter, PageInfo pageInfo) {
		try {
			Map<String, Object> map = sqlLogService.querySqlWarn(pageInfo);
			return new MV("/sys/monitor/log/sqlWarn", WebConstant.COMMAND, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/log/sql/infoLogList")
	public ModelAndView doInfoLogList(FilterMap filter, PageInfo pageInfo) {
		try {
			Map<String, Object> map = sqlLogService.querySqlInfo(pageInfo);
			return new MV("/sys/monitor/log/sqlInfo", WebConstant.COMMAND, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "下载")
	@RequestMapping("/log/sql/downloadLog")
	public ModelAndView doDownloadLog(HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "filename", required = true) String  filename) {
		try {
			WebUtil.setDownloadableHeader(resp, filename);
			String defaultPath = ActionUtil.getCtx().getServletContext().getRealPath("/page");
			String realPath = defaultPath + "/logs/" + filename;
			FileInputStream in = new FileInputStream(realPath);
			int len = 0;
			byte buffer[] = new byte[1024];
			OutputStream out = resp.getOutputStream();
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			return null;
		} catch (Exception e) {
			return new MV().processException(e);
		}
	}
	
	@MethodLogUtil(type = "1", value = "容器日志")
	@RequestMapping("/log/container/doList")
	public ModelAndView containerLogList(FilterMap filter, PageInfo pageInfo,@RequestParam(value = "path", required = false) String  path) {
		MV mv =  new MV("/sys/monitor/log/containerList");
		try {
			Map<String, Object> map = new HashMap();
			
			List rows = new ArrayList();
			if(StringUtils.isBlank(path)){
				path = "/opt/tomcat/logs";
			}
			File file = new File(path);
			if(file.exists()){
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					String name = files[i].getName();
					if (name.endsWith(".tar.gz") || name.endsWith(".out") || name.endsWith(".log")) {
						Map item = new HashMap();
						item.put("name",name); 
						item.put("length", (double) files[i].length() / 1024);
						item.put("modified", new Date(files[i].lastModified()));
						rows.add(item);
					}
				}
			}
			map.put("total",rows.size());
			map.put("rows",rows); 
			mv.addObject("logPath",path);
			mv.addObject(WebConstant.COMMAND, map);
			return mv.fwd();
		} catch (Exception e) {
			return mv.processException(e);
		}
	}
	
	
	@MethodLogUtil(type = "1", value = "容器日志下载")
	@RequestMapping("/log/container/downloadLog")
	public ModelAndView downloadLog(HttpServletResponse resp, @RequestParam(value = "filename", required = true) String  filename,@RequestParam(value = "path", required = false) String  path) {
		try {
			WebUtil.setDownloadableHeader(resp, filename);
			if(path == null){
				path = "/opt/tomcat/logs";
			}
			String realPath = path + "/" + filename;
			FileInputStream in = new FileInputStream(realPath);
			int len = 0;
			byte buffer[] = new byte[1024];
			OutputStream out = resp.getOutputStream();
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			return null;
		} catch (Exception e) {
			return new MV().processException(e);
		}
	}

}

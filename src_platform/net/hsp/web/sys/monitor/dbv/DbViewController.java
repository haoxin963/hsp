package net.hsp.web.sys.monitor.dbv;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.service.sys.monitor.dbv.DbViewService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * DBView控制器
 * 
 * @author lk0564
 */
@Controller
@WebLogUtil(name = "登录日志")
@RequestMapping("/sys/monitor")
@Lazy(true)
public class DbViewController {
	
	@Autowired
	private DbViewService dbViewService;
	
	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/dbv/doList")
	public ModelAndView doList(FilterMap filter, PageInfo pi) {
		try {
			Map<String, Object> cmd = dbViewService.queryTableList(filter, pi);
			MV mv = new MV("/sys/monitor/dbv/dbvList", WebConstant.COMMAND, cmd);
			mv.addObject("dbtype", DbInfo.getDbType());
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e);
		}
	}
	
	@MethodLogUtil(type = "", value = "显示数据表结构")
	@RequestMapping("/dbv/showStruct")
	public ModelAndView showStruct(@RequestParam(value="tname",required=true)String tname){
		try {
			Map<String, Object> cmd = dbViewService.queryTableStruct(tname);
			MV mv = new MV("/sys/monitor/dbv/dbvStruct", WebConstant.COMMAND, cmd);
			mv.addObject("tname", tname);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e);
		}
	}
	
	@MethodLogUtil(type = "", value = "显示数据表数据")
	@RequestMapping("/dbv/showData")
	public ModelAndView showData(@RequestParam(value="tname",required=true)String tname, PageInfo pi){
		try {
			Map<String, Object> cmd = dbViewService.queryTableData(tname, pi);
			MV mv = new MV("/sys/monitor/dbv/dbvData", WebConstant.COMMAND, cmd);
			mv.addObject("tname", tname);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e);
		}
	}
	
	
}

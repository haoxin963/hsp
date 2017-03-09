package net.hsp.web.sys.weixin;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.dao.DynamicDataSource;
import net.hsp.entity.sys.weixin.WeixinApp;
import net.hsp.service.sys.weixin.WeixinService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 微信控制器
 * @author aa
 */
@Controller
@WebLogUtil(name = "微信")
@RequestMapping("/sys/weixin/weixinApp")
@Lazy(true)
public class WeixinAppController  {

	private String listURL = "/sys/weixin/weixinAppList";
	private String formURL = "/sys/weixin/weixinAppForm";
	
	@Autowired
	private WeixinService weixinService; 
 
	private final Logger log = Logger.getLogger(WeixinAppController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(WeixinApp obj) {
		DynamicDataSource.setCustId(PlatFormConstant.BASESTATIONID);
		try {
			if (obj.getId()!=null) {
				weixinService.update(obj); 
			}else{
				weixinService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(WeixinApp obj,@RequestParam(value="ids",required=false) String ids) {
		DynamicDataSource.setCustId(PlatFormConstant.BASESTATIONID);
		try {
			if (StringUtils.isNotBlank(ids)) {
				weixinService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				weixinService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(WeixinApp obj){
		DynamicDataSource.setCustId(PlatFormConstant.BASESTATIONID);
		try{  
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (WeixinApp) weixinService.findById(obj); 
			}
			MV mv = new MV(formURL,WebConstant.COMMAND,obj); 
			
			//相关初始数据加载 
			//mv.addObject("x","yy");

			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(WeixinApp weixinApp, FilterMap filter, PageInfo pageInfo) {
		try {
			DynamicDataSource.setCustId(PlatFormConstant.BASESTATIONID);
			return new MV(listURL, WebConstant.COMMAND, weixinService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

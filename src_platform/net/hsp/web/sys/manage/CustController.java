package net.hsp.web.sys.manage;

import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.manage.Customer;
import net.hsp.service.sys.manage.CustomerService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 客户控制器
 * @author aa
 */
@Controller
@WebLogUtil(name = "客户")
@RequestMapping("/sys/manage/customer")
@Lazy(true)
public class CustController  {

	private String listURL = "/sys/manage/customerList";
	private String formURL = "/sys/manage/customerForm";
	
	@Autowired
	@Qualifier("custServiceImpl")
	private CustomerService customerService; 
 
	private final Logger log = Logger.getLogger(CustController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Customer obj) {
		try {
			if (obj.getCustId()!=null) {
				customerService.update(obj); 
			}else{
				customerService.save(obj); 
			}
			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Customer obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				customerService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				customerService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Customer obj){
		try{  
			//根据id(pk)从DB加载 
			if (obj.getCustId()!=null) {
				obj = (Customer) customerService.findById(obj); 
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
	public ModelAndView doList(Customer customer, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, customerService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

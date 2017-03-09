package net.hsp.web.sys.manage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.manage.Station;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.service.sys.manage.StationService;
import net.hsp.service.sys.rbac.FunctionService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 站点控制器
 * 
 * @author jsmart
 */
@Controller
@WebLogUtil(name = "站点")
@RequestMapping("/sys/manage/station")
@Lazy(true)
public class StationController {

	private String listURL = "/sys/manage/stationList";
	private String formURL = "/sys/manage/stationForm";
	private String menuConfigURL = "/sys/manage/menuConfig";
	private String menuConfigFormURL = "/sys/manage/menuConfigForm";

	@Autowired
	private StationService stationService;

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Station obj) {
		try { 
			if (StringUtils.isNotBlank(obj.getStatId())) {
				//暂时只允许修改站点名称
				Station s = stationService.findById(obj);
				s.setStatName(obj.getStatName());
				stationService.update(s);
			} else {
				obj.setStatId(java.util.UUID.randomUUID().toString());
				obj.setCreateDate(new Date());
				obj.setDelTag("0");
				obj.setLanguage("zh_cn");
				obj.setStatus("1");// 1启用 2禁用
				stationService.create(obj);
			}

			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "禁用")
	@RequestMapping("/disable")
	public ModelAndView disable(@RequestParam(value = "domains", required = false)
	String domains) {
		try {
			String[] domain = domains.split(",");
			stationService.disable(domain);
			return new MV(listURL, WebConstant.COMMAND, null).fwd();
		} catch (Exception e) {
			e.printStackTrace();
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "启用")
	@RequestMapping("/enabled")
	public ModelAndView enabled(@RequestParam(value = "domains", required = false)
	String domains) {
		try {
			String[] domain = domains.split(",");
			stationService.enabled(domain);
			return new MV(listURL, WebConstant.COMMAND, null).fwd();
		} catch (Exception e) {
			e.printStackTrace();
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(Station obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				stationService.batchDeleteTag(obj.getClass(), ids.split(","));
			} else {
				stationService.deleteTag(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Station obj) {
		try {
			if (obj.getStatId() != null) {
				obj = (Station) stationService.findById(obj);
			}
			MV mv = new MV(formURL, WebConstant.COMMAND, obj);
			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Station station, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, stationService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
/**
 * -----------------------------------站点菜单功能---------------------------------------------------------------------------------------------------------------------------
*/	
	@MethodLogUtil(type = "", value = "到站点菜单配置界面")
	@RequestMapping("/toMenuConfig")
	public ModelAndView toMenuConfig(@RequestParam(value = "custDomain", required = true)
	String custDomain, PageInfo pageInfo) {
		try {
			if (custDomain != null && !"".equals(custDomain)) {
				Map param = new HashMap();
				param.put("parent_id", "0001");
				param.put("custDomain", custDomain);
			    MV mv = new MV(menuConfigURL, WebConstant.COMMAND, stationService.queryFunction(param, pageInfo));
			    mv.addObject("custDomain", custDomain);
				return mv.fwd();
			} else {
				throw new Exception("对应的站点数据未找到");
			}
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
	
	@MethodLogUtil(type="",value="根据站点域名查询站点菜单")
	@RequestMapping("/doFunctionList")
	public ModelAndView doFunctionList(Function function, FilterMap filter, PageInfo pageInfo) {
		try {
			if(filter.getFilter().get("parent_id") == null || "".equals(filter.getFilter().get("parent_id"))){
				filter.getFilter().put("parent_id", "0001");
			}
			return new MV(menuConfigURL, WebConstant.COMMAND, stationService.queryFunction(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="保存及更新菜单")
	@RequestMapping("/doFunctionSave")
	public ModelAndView doFunctionSave(@RequestParam(value = "custDomain", required = true)
			String custDomain,Function obj) {
		try {
			//检测url，前面默认加斜线
			if(obj.getLinkAddress() != null && !"".equals(obj.getLinkAddress())){
				if(!obj.getLinkAddress().startsWith("/")){
					obj.setLinkAddress("/"+obj.getLinkAddress());
				}
			}
			//替换中文逗号为英文的逗号
			if(obj.getInnerUrl() != null && !"".equals(obj.getInnerUrl())){
				if(obj.getInnerUrl().indexOf("，")>0){
					String innerurl = obj.getInnerUrl();				
					innerurl = innerurl.replace("，",",");
					obj.setInnerUrl(innerurl);
				}
			}			
			
			if (obj.getFunctionId() != null && !"".equals(obj.getFunctionId())) {
				stationService.modifyFunction(obj, custDomain);
			}else{
				stationService.addFunction(obj, custDomain); 
			}	
			return new MV(menuConfigURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doFunctionDelete") 
	public ModelAndView doFunctionDelete(@RequestParam(value = "custDomain", required = true)
			String custDomain,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				stationService.deleteFunctionAndChildren(ids, custDomain);
			}
			return new MV(menuConfigURL).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doFunctionLoad") 
	public ModelAndView doFunctionLoad(@RequestParam(value = "custDomain", required = true)
			String custDomain, Function obj){
		MV mv = null;
		try{  
			//根据id(pk)从DB加载 
			if (obj.getFunctionId() != null && obj.getFunctionName() == null) {  //编辑操作
				Function funObj = stationService.findFunById(obj, custDomain);
				Function parentFun = new Function();
				parentFun.setFunctionId(funObj.getParentId());
				parentFun = stationService.findFunById(parentFun, custDomain);	
				mv = new MV(menuConfigFormURL,WebConstant.COMMAND,funObj);
				mv.addObject("parentFun", parentFun);
			}else{   //新增操作
				mv = new MV(menuConfigFormURL,WebConstant.COMMAND,new Function()); 
				mv.addObject("parentFun", obj);
			}	
			mv.addObject("custDomain", custDomain);
			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}
	
	@MethodLogUtil(type="",value="树结构菜单")
	@RequestMapping("/buildMenuTree")
	@ResponseBody
	public List buildMenuTree(@RequestParam(value = "custDomain", required = true)
			String custDomain) {  
		if (StringUtils.isNotBlank(custDomain)) {
			return  stationService.buildMenuTree(custDomain);
		}else{
			return  stationService.buildMenuTree(PlatFormConstant.TEMPLATEID);
		}
	} 
	
	@MethodLogUtil(type="",value="变更功能层级及序号")
	@RequestMapping("/changeFuncLevel")
	public ModelAndView changeFuncLevel(@RequestParam(value="id",required=true)String id,
			@RequestParam(value="type",required=true)String type,			 
			@RequestParam(value="parentId",required=false)String parentId,
			@RequestParam(value="sortNo",required=false)String sortNo,
			@RequestParam(value="sortList",required=false)String sortList,
			@RequestParam(value="custDomain",required=false)String custDomain){
		try {
			stationService.changeFunctionLevel(id, type, parentId, sortNo, sortList.split(";"),custDomain);
			return new MV(menuConfigURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="保存从源菜单树拖拽的功能节点")
	@RequestMapping("/saveTreeNodes")
	public ModelAndView saveTreeNodes(@RequestParam(value="custDomain",required=false)
			String custDomain,String isAppend,String functionString){
		try {
			JSONObject jsonObj = JSONObject.fromObject(functionString);
			JSONArray jsonArray = jsonObj.getJSONArray("children");
			jsonObj.remove("children");
			Function functionObj = (Function) JSONObject.toBean(jsonObj,Function.class);  
			String functionId = "";
			if("true".equals(isAppend)){
				functionId = stationService.addFunction(functionObj, custDomain).getFunctionId();
			}else{
				functionId = stationService.insertFunction(functionObj,custDomain).getFunctionId();
			}
			if(!"".equals(functionId) && !jsonArray.isEmpty()){
				for(int i = 0;i < jsonArray.size(); i++){
					JSONObject obj = jsonArray.getJSONObject(i);
					saveTreeNodeFunction(functionId,obj,custDomain);
				}
			}
			return new MV(menuConfigURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	public void saveTreeNodeFunction(String parentId,JSONObject jsonObj,String custDomain){
		JSONArray jsonArray = jsonObj.getJSONArray("children");
		jsonObj.remove("children");
		Function functionObj = (Function) JSONObject.toBean(jsonObj,Function.class);
		functionObj.setParentId(parentId);
		String functionId = stationService.addFunction(functionObj, custDomain).getFunctionId();
		if(!jsonArray.isEmpty()){
			for(int i = 0;i < jsonArray.size(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				saveTreeNodeFunction(functionId,obj,custDomain);
			}
		}
	}
}

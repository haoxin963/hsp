package net.hsp.web.sys.widget;

import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.widget.Favorite;
import net.hsp.service.sys.notice.StringUtil;
import net.hsp.service.sys.select.FilterService;
import net.hsp.service.sys.widget.FavoriteService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 菜单收藏控制器
 * @author smartTools
 */
@Controller
@WebLogUtil(name = "菜单收藏")
@RequestMapping("/sys/widget/favorite")
@Lazy(true)
public class FavoriteController  {

	private String listURL = "/sys/widget/favoriteList";
	private String formURL = "/sys/widget/favoriteForm";
	private String selectURL = "/sys/widget/select";
	
	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private FilterService filterService;
	
	private final Logger log = Logger.getLogger(FavoriteController.class);
	
	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Favorite obj) {
		try {
			if (obj.getId()!=0) {//编辑操作
				favoriteService.update(obj);
			}else{//新增操作
				//验证是否已经存在
				int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
				Map<String,Object> map = favoriteService.getFavoriteByFunctionId(obj.getFunctionId(), userId); 
				
				if(map.get("id") == null){//不存在，新增后设置排序值
					obj = (Favorite)favoriteService.save(obj); 
					obj.setSortNo(obj.getId());
				}else{//存在,更新ID和排序值
					obj.setId(Integer.valueOf(map.get("id").toString()));
					obj.setSortNo(Integer.valueOf(map.get("sortNo").toString()));
				}
				favoriteService.update(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Favorite obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				favoriteService.batchDelete(obj.getClass(),ids.split(","));
			}else{
				favoriteService.delete(obj);
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(@RequestParam(value="functionId",required=true)String functionId){
		try{
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			//由菜单ID获取对应信息
			Map<String,Object> map  = favoriteService.getFavoriteByFunctionId(functionId, userId); 
			return new MV(formURL,WebConstant.COMMAND,map).fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Favorite favorite, FilterMap filter, PageInfo pageInfo) {
		try {
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			return new MV(listURL, WebConstant.COMMAND, favoriteService.query(filter.getFilter(), pageInfo,userId)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}


	@MethodLogUtil(type="",value="当前人员获取所有收藏")
	@RequestMapping("/doAll")
	public ModelAndView doAll() {
		try {
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			return new MV("", WebConstant.COMMAND, favoriteService.all(userId)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="当前人员获取所有收藏")
	@RequestMapping("/doSelectPerson")
	public ModelAndView doSelectPerson(Map param) {
		try {		
			String filterId = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("filterId"));
			String isPerson = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("isPerson"));
			String aSync = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("aSync"));
			String parentId = StringUtil.getString(ActionUtil.getCtx().getRequest().getParameter("parentId"));
			
			int userId = Integer.valueOf(HttpSessionFactory.getUserId(ActionUtil.getCtx().getRequest()).toString());
			param.put("isPerson", isPerson);
			param.put("filterId", filterId);
			param.put("aSync", aSync);
			param.put("parentId", parentId);
			List<Map<String,Object>> list = favoriteService.doSelectDepartment(param);
			
			List<Map<String,Object>> filterList = filterService.getFilterUserIdList(Integer.parseInt(filterId));
			for(int i=list.size()-1;i>=0;i--){
				Map<String,Object> map = list.get(i);
				if(!StringUtil.isEmpty(map.get("user"))){
					int id = Integer.parseInt(map.get("id").toString());
					for(int x=0;x<filterList.size();x++){
						Map<String,Object> filterMap = filterList.get(x);
						int fid = Integer.parseInt(filterMap.get("id").toString());
						if(id == fid)
							list.remove(i);
					}
				}
			}
			
			 return new MV(selectURL, WebConstant.COMMAND, list).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	
	

}

package net.hsp.web.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.org.Position;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.service.sys.org.PositionService;
import net.hsp.service.sys.rbac.RoleService;
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
 * 职位信息控制器
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "职位信息")
@RequestMapping("/sys/org/position")
@Lazy(true)
public class PositionController  {

	private String listURL = "/sys/org/positionList";
	private String formURL = "/sys/org/positionForm";
	private String refRoleURL= "/sys/org/position_ref_role";
	
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private RoleService	roleService;
 
	private final Logger log = Logger.getLogger(PositionController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Position obj) {
		try {
			if (obj.getId()!=null) {
				positionService.modifyPosition(obj); 
			}else{
				obj.setDelTag("0");
				positionService.addPosition(obj); 
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Position obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				positionService.batchDelPositionByIds(ids.split(","));
			}else{
				positionService.delPositionById(obj.getId().toString());
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Position obj){
		try{ 
			MV mv = null;
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (Position) positionService.findById(obj);	
				mv = new MV(formURL,WebConstant.COMMAND,obj);
			}else{
				
				mv = new MV(formURL,WebConstant.COMMAND,obj);
			}
			
			//相关初始数据加载 
			//mv.addObject("x","yy");
			List<Role> rolelist = roleService.getRoleList();			 
			mv.addObject("roleList", rolelist);
			
			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Position position, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, positionService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type = "", value = "选择角色页面")
	@RequestMapping("/toChooseRole")
	public ModelAndView toChooseRole() {
		try {
			List<Role> rolelist = roleService.getRoleList();
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("roleList", rolelist);			
			return new MV(refRoleURL, mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
	
	@MethodLogUtil(type = "", value = "检查职位编码是否存在")
	@RequestMapping("/doCheckPositionNo")
	public ModelAndView doCheckPositionNo(@RequestParam(value="positionNo",required=true) String positionNo) {
		try {			 
			Map<String, Object> mp = new HashMap<String, Object>();
			Position position = positionService.getPositionByNo(positionNo);
			if(position!=null){
				mp.put("status","1");	
				mp.put("msg", "职位编码已经存在！");
			}else{
				mp.put("status","0");
			}
			return new MV(formURL, mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
	
	@MethodLogUtil(type = "", value = "根据关键字筛选角色")
	@RequestMapping("/doGetRolesByKeyword")
	public ModelAndView doGetRolesByKeyword(@RequestParam(value="keyword",required=true) String keyword) {
		try {			 
			Map<String, Object> mp = new HashMap<String, Object>();
			List<Role> roleList = roleService.getRolesByKeyword(keyword);
			mp.put("roleList", roleList);
			return new MV(formURL, mp).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
}

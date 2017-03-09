package net.hsp.web.sys.org;

import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.org.Department;
import net.hsp.entity.sys.org.Position;
import net.hsp.entity.sys.org.Post;
import net.hsp.service.sys.org.DepartmentService;
import net.hsp.service.sys.org.PositionService;
import net.hsp.service.sys.org.PostService;
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
 * 岗位信息控制器
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "岗位信息")
@RequestMapping("/sys/org/post")
@Lazy(true)
public class PostController  {

	private String listURL = "/sys/org/postList";
	private String formURL = "/sys/org/postForm";
	
	@Autowired
	private PostService postService; 
	
	@Autowired
	private PositionService positionService;
 
	@Autowired
	private DepartmentService departmentService;
		
	private final Logger log = Logger.getLogger(PostController.class); 

	@MethodLogUtil(type="",value="保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Post obj) {
		try {
			MV mv = new MV(listURL, WebConstant.COMMAND, obj);
			
			Post headPost = postService.getDeptHeadPostByDeptId(obj.getDeptId()+"");			
			if (obj.getId()!=null) {
				//修改时候，原来的部门负责岗位不是当前岗位时候
				if(headPost!=null&&headPost.getId()!=obj.getId()&&"1".equals(obj.getIsDeptHead())){
					mv.addObject("status", "2");
					mv.addObject("msg", "一个部门有且只能存在一个部门负责岗位!");
					return mv.fwd();
				}
				postService.modifyPost(obj); 
			}else{
				if(headPost!=null&&"1".equals(obj.getIsDeptHead())){
					mv.addObject("status", "2");
					mv.addObject("msg", "一个部门有且只能存在一个部门负责岗位!");
					return mv.fwd();
				}
				obj.setDelTag("0");
				obj.setHasChild("0");
				postService.addPost(obj); 
			}			
			return mv.fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}


	@MethodLogUtil(type="",value="删除")
	@RequestMapping("/doDelete") 
	public ModelAndView doDelete(Post obj,@RequestParam(value="ids",required=false) String ids) {
		try {
			if (StringUtils.isNotBlank(ids)) {
				postService.batchDelPostByIds(ids.split(","));
			}else{
				postService.delPostById(obj.getId().toString());
			}			
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) { 
			return new MV().processException(e,null);
		} 
	}

	
	@MethodLogUtil(type="",value="加载")
	@RequestMapping("/doLoad") 
	public ModelAndView doLoad(Post obj){
		try{  
			MV mv = null;
			//根据id(pk)从DB加载 
			if (obj.getId()!=null) {
				obj = (Post) postService.findById(obj); 
				mv = new MV(formURL,WebConstant.COMMAND,obj);
				//部门名称
				Department dept = new Department();
				dept.setDepartmentId(obj.getDeptId());
				Object deptObj = departmentService.findById(dept);
				if(deptObj!=null){
					mv.addObject("deptObj", deptObj);
				}				
				//上级岗位名称
				if(obj.getParentId()!=0){
					Post parentPost = new Post();
					parentPost.setId(obj.getParentId());
					Object postObj = postService.findById(parentPost);
					if(postObj!=null){
						mv.addObject("parentPost", postObj);
					}					
				}				
				//职务名称
				Position position = new Position();
				position.setId(obj.getPositionId());
				Object positionObj = positionService.findById(position);
				if(positionObj!=null){
					mv.addObject("positionObj", positionObj);
				}				
			}else{
				mv = new MV(formURL,WebConstant.COMMAND,obj);
			}
			 
									
			//相关初始数据加载
			List<Position> positionList = positionService.getAllPosition();
			mv.addObject("positionList",positionList);

			return mv.fwd();
		}catch(Exception e){
		    return (new MV()).processException(e, null);
		}
	}


	@MethodLogUtil(type="",value="查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Post post, FilterMap filter, PageInfo pageInfo) {
		try {
			Map model = postService.query(filter.getFilter(), pageInfo);
			return new MV(listURL, WebConstant.COMMAND, model).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	
	@MethodLogUtil(type="",value="变更岗位顺序层级")
	@RequestMapping("/changePostLevel")
	public ModelAndView changePostLevel(@RequestParam(value="id",required=true)String id,
			@RequestParam(value="type",required=true)String type,			 
			@RequestParam(value="parentId",required=true)String parentId,
			@RequestParam(value="sortList",required=true)String sortList){
		try {
			postService.changePostLevel(id, type, parentId,  sortList.split(";"));
			return new MV(listURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
	
	@MethodLogUtil(type="",value="验证岗位代号是否存在")
	@RequestMapping("/checkPosNoExist")
	public ModelAndView checkPosNoExist(@RequestParam(value="postNo",required=true)String postNo){
		try {
			MV mv = new MV(formURL); 
			Post post = postService.getPostByCode(postNo);
			if(post!=null){
				//已经存在
				mv.addObject("status", "1");
			}else{
				mv.addObject("status", "0");
			}
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e,null);
		}
	}
}

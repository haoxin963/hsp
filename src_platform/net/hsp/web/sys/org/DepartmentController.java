package net.hsp.web.sys.org;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.org.Department;
import net.hsp.service.sys.org.DepartmentService;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.ActionUtil;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.HttpSessionFactory;
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
 * pubmodule_department_tbl控制器
 * 
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "pubmodule_department_tbl")
@RequestMapping("/sys/org/department")
@Lazy(true)
public class DepartmentController {

	private String listURL = "/sys/org/departmentList";
	private String formURL = "/sys/org/departmentForm";

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private UserService userService;
	
	private final Logger log = Logger.getLogger(DepartmentController.class);

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Department obj) {
		try {
			HttpServletRequest req = ActionUtil.getCtx().getRequest();
			String custId = HttpSessionFactory.getCustId(req);
			if (StringUtils.isNotBlank(obj.getDepartmentId())) {
				departmentService.modifyDepartment(obj);
			} else {
				obj.setDomain(custId);
				obj.setDelTag("0");
				obj.setChild("0");
				departmentService.addDepartment(obj);
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(Department obj, @RequestParam(value = "ids", required = false)
	String ids) {
		try {
			MV mv = new MV(listURL, WebConstant.COMMAND, obj);

			String[] dds = StringUtils.isNotBlank(ids) ? ids.split(",") : new String[] { obj.getDepartmentId().toString() };
			int haschild = departmentService.checkDeptHasChild(dds);
			if (haschild == 1) {
				mv.addObject("status", "2");
				mv.addObject("msg", "您选择的部门还包含下级部门，不能直接删除！");
				return mv.fwd();
			}
			if (StringUtils.isNotBlank(ids)) {
				departmentService.batchDelDepartmentByIds(ids.split(","));
			} else {
				departmentService.delDepartmentById(obj.getDepartmentId().toString());
			}
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Department obj) {
		try {
			MV mv = null;
			// 根据id(pk)从DB加载
			if (obj.getDepartmentId() != null && obj.getDepartmentName() == null) {
				obj = (Department) departmentService.findById(obj);

				Department parentDept = new Department();
				parentDept.setDepartmentId(obj.getParentId());
				Object pObj = departmentService.findById(parentDept);
				if (pObj != null) {
					parentDept = (Department) pObj;
				}

				mv = new MV(formURL, WebConstant.COMMAND, obj);
				mv.addObject("parentObj", parentDept);
			} else {
				Department p = departmentService.findById(obj);
				obj.setDepartmentId(null);
				obj.setDepartmentName(null);
				mv = new MV(formURL, WebConstant.COMMAND, obj);
				mv.addObject("parentId", p.getDepartmentId());
				mv.addObject("parentName", p.getDepartmentName());
			}
			// 相关初始数据加载
			// mv.addObject("x","yy");
			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Department department, FilterMap filter, PageInfo pageInfo) {
		try {
			if (filter.getFilter().get("parent_Id") == null) {
				filter.getFilter().put("parent_Id", "1");
			}
			return new MV(listURL, WebConstant.COMMAND, departmentService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			e.printStackTrace();
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "变更部门层级")
	@RequestMapping("/changeDeptmentLevel")
	public ModelAndView changeDeptmentLevel(@RequestParam(value = "id", required = true)
	String id, @RequestParam(value = "type", required = true)
	String type, @RequestParam(value = "parentId", required = false)
	String parentId, @RequestParam(value = "sortList", required = false)
	String sortList) {
		try {
			departmentService.changeDepartmentLevel(id, type, parentId, sortList.split(";"));
			return new MV(listURL).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "检查部门编号是否重复")
	@RequestMapping("/checkDeptCodeExists")
	public ModelAndView checkDeptCodeExists(@RequestParam(value = "code", required = true)
	String code) {
		try {
			Map map = new HashMap();
			Department dept = departmentService.getDeptByCode(code);
			if (dept == null) {
				// 编号没有重复，可用
				map.put("status", "0");
			} else {
				// 编号已经重复，不可用
				map.put("status", "1");
			}
			return new MV(formURL, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
	
	@MethodLogUtil(value = "组织结构列表")
	@RequestMapping("/doTree")
	public ModelAndView doTree(FilterMap filter) {
		MV mv = new MV();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("depts", departmentService.queryDept(filter));
		map.put("users", userService.queryUserII(filter));
		mv.addObj(map);
		return mv.fwd();
	}
}

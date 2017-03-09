package net.hsp.web.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.WebLogUtil;
import net.hsp.entity.sys.org.Department;
import net.hsp.entity.sys.org.Employee;
import net.hsp.entity.sys.org.Post;
import net.hsp.service.sys.org.DepartmentService;
import net.hsp.service.sys.org.EmployeeService;
import net.hsp.service.sys.org.PostService;
import net.hsp.service.sys.rbac.UserService;
import net.hsp.web.constants.WebConstant;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.MV;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 员工信息控制器
 * 
 * @author lk0508
 */
@Controller
@WebLogUtil(name = "员工信息")
@RequestMapping("/sys/org/employee")
@Lazy(true)
public class EmployeeController {

	private String listURL = "/sys/org/employeeList";
	private String formURL = "/sys/org/employeeForm";
	private String addressBookURL = "/sys/org/addressbookList";
	private String createUserFormURL = "/sys/org/batchCreateUserForm";
	private String userSelectListURL = "/sys/org/userSelectList";

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@MethodLogUtil(type = "", value = "保存及更新")
	@RequestMapping("/doSave")
	public ModelAndView doSave(Employee obj, @RequestParam(value = "postids", required = false) String postids) {
		try {
			if (obj.getId() != null) {
				employeeService.modifyEmployee(obj, postids.split(";"));
			} else {
				obj.setDelTag("0");
				employeeService.addEmployee(obj, postids.split(";"));
			}
			return new MV(listURL, WebConstant.COMMAND, obj).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "删除")
	@RequestMapping("/doDelete")
	public ModelAndView doDelete(Employee obj, @RequestParam(value = "ids", required = false) String ids) {
		try {
			Map map = new HashMap();
			// 判断员工是否已经有生成用户的
			String[] eids = StringUtils.isNotBlank(ids) ? ids.split(",") : new String[] { obj.getId().toString() };
			if (eids.length > 0) {
				List<Map<String, Object>> list = employeeService.checkEmpIsCreatedUser(eids);
				if (list.size() > 0) {
					map.put("status", "0");
					map.put("msg", "有" + list.size() + "个用户和员工关联,请先删除用户!");
				} else {
					if (StringUtils.isNotBlank(ids)) {
						employeeService.batchDelEmployeeByIds(ids.split(","));
					} else {
						employeeService.delEmployeeById(obj.getId().toString());
					}
				}
			}
			return new MV(listURL, map).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "加载")
	@RequestMapping("/doLoad")
	public ModelAndView doLoad(Employee obj, @RequestParam(value = "postId", required = false) String postId) {

		try {
			// 根据id(pk)从DB加载
			MV mv = null;
			if (obj.getId() != null) {
				obj = (Employee) employeeService.findById(obj);
				mv = new MV(formURL, WebConstant.COMMAND, obj);

				// 部门列表
				Department department = new Department();
				department.setDepartmentId(obj.getDeptId());
				department = (Department) departmentService.findById(department);
				mv.addObject("department", department);

				// 主要岗位
				Map<String, Object> mainpost = employeeService.getMainPostByEmpId(obj.getId().toString());
				mv.addObject("mainpost", mainpost);
				// 其他岗位列表
				List<Map<String, Object>> otherpostlist = employeeService.getOtherPostByEmpId(obj.getId().toString());
				mv.addObject("otherpostlist", otherpostlist);

				String otherpostids = "";
				String ohterpostnames = "";
				for (Map<String, Object> mp : otherpostlist) {
					otherpostids += ";" + mp.get("id").toString() + ",0";
					ohterpostnames += mp.get("postName").toString() + ",";
				}
				ohterpostnames = ohterpostnames.length() > 0 ? ohterpostnames.substring(0, ohterpostnames.length() - 1) : "";

				mv.addObject("otherpostids", otherpostids);
				mv.addObject("ohterpostnames", ohterpostnames);

			} else {
				mv = new MV(formURL, WebConstant.COMMAND, obj);
				// 查询相应的岗位
				if (postId != null && !"".equals(postId)) {
					Post post = new Post();
					post.setId(Integer.parseInt(postId));
					post = (Post) postService.findById(post);
					mv.addObject("mainpost", post);

					Department department = new Department();
					department.setDepartmentId(post.getDeptId());
					department = (Department) departmentService.findById(department);
					mv.addObject("department", department);
				}
			}

			return mv.fwd();
		} catch (Exception e) {
			return (new MV()).processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doList")
	public ModelAndView doList(Employee employee, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(listURL, WebConstant.COMMAND, employeeService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询")
	@RequestMapping("/doAdressBook")
	public ModelAndView doAdressBook(Employee employee, FilterMap filter, PageInfo pageInfo) {
		try {
			return new MV(addressBookURL, WebConstant.COMMAND, employeeService.query(filter.getFilter(), pageInfo)).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "到批量生成用户页面")
	@RequestMapping("/toBatchCreateUser")
	public ModelAndView toBatchCreateUser(@RequestParam(value = "ids", required = true) String ids) {
		try {
			// 批量创建用户
			MV mv = new MV(createUserFormURL);
			mv.addObject("ids", ids);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "批量生成用户")
	@RequestMapping("/doBatchCreateUser")
	public ModelAndView doBatchCreateUser(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "usrType", required = true) String usrType, @RequestParam(value = "pwdType", required = true) String pwdType) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 批量创建用户
			Map<String, Object> map = employeeService.batchCreateUser(ids.split(","), usrType, pwdType);

			// 成功列表，失败列表
			List<Map<String, Object>> succlist = (List<Map<String, Object>>) map.get("succlist");
			List<Map<String, Object>> faillist = (List<Map<String, Object>>) map.get("faillist");

			// paramMap.put("succTotal",succlist.size());
			// paramMap.put("failTotal",faillist.size());
			String failnamelist = "";
			for (int i = 0; i < faillist.size(); i++) {
				Map<String, Object> mp = faillist.get(i);
				String empno = mp.get("empno").toString();
				String empname = mp.get("empname").toString();
				String errmsg = mp.get("errmsg").toString();
				if ("1".equals(errmsg)) {
					errmsg = "用户已经生成";
				} else if ("2".equals(errmsg)) {
					errmsg = "用户名已经存在";
				}
				failnamelist += "[" + empno + "|" + empname + "|" + errmsg + "]<br/>";
			}
			String msg = "成功生成[" + succlist.size() + "]个用户 <br/>失败[" + faillist.size() + "]个  <br/>" + failnamelist;
			paramMap.put("msg", msg);
			return new MV(listURL, paramMap).fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

	@MethodLogUtil(type = "", value = "查询用户")
	@RequestMapping("/doListUser")
	public ModelAndView doListUser(String employeeId, FilterMap filter, PageInfo pageInfo) {
		try {
			MV mv = new MV(userSelectListURL);
			Map map = userService.queryUser(null, filter, pageInfo);
			mv.addObject(WebConstant.COMMAND, map);
			mv.addObject("employeeId", employeeId);
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}
	
	@MethodLogUtil(type = "", value = "手动关联用户")
	@RequestMapping("/doLinkUser")
	public ModelAndView doLinkUser(@RequestParam(value = "employeeId", required = true) String employeeId,@RequestParam(value = "userId", required = true)  String userId) {
		try {
			MV mv = new MV();
			mv.addObject("status", employeeService.linkToUser(userId, employeeId));
			return mv.fwd();
		} catch (Exception e) {
			return new MV().processException(e, null);
		}
	}

}

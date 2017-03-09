package net.hsp.service.sys.rbac;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.common.crypt.MD5;
import net.hsp.entity.sys.org.Employee;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.entity.sys.rbac.User;
import net.hsp.entity.sys.rbac.Userrole;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.sys.org.EmployeeService;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * 用户表业务类
 * 
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "用户业务类")
@Lazy(true)
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired
	private EmployeeService employeeService;
	
	@MethodLogUtil(value = "多条件查询用户")
	public Map queryUser(User user, FilterMap filterMap, PageInfo p) throws ServiceException {
		String sql = "select id,userName,trueName,status,lastLogTime,isCreateStaff from pubmodule_user_tbl where delTag='0' and username<>'" + PlatFormConstant.ADMIN_USERNAME + "' ";
		Map sqlBuild = SQLBuild.buildLike(filterMap);
		sql += sqlBuild.get("sql");
		return this.findPageInfo(sql, (Object[]) sqlBuild.get("args"), p);
	}

	@MethodLogUtil(value = "多条件查询用户")
	public List<User> queryUserII(FilterMap filterMap) throws ServiceException {
		String sql = "select * from pubmodule_user_tbl where delTag='0' and username<>'" + PlatFormConstant.ADMIN_USERNAME + "' ";
		Map sqlBuild = SQLBuild.buildEquals(filterMap);
		sql += sqlBuild.get("sql");
		return this.find(sql, (Object[]) sqlBuild.get("args"),User.class);
	}
	
	@Override
	public User addUser(User user) {
		String sql = "select * from pubmodule_user_tbl u where u.delTag=? and u.userName = ?";
		List<Map<String, Object>> list = this.find(sql, new Object[] { "0", user.getUserName() });
		if (list.size() > 0) {
			return null;
		}
		String pwd = user.getPassword();
		user.setPassword(MD5.md5(pwd));
		this.save(user);
		return user;
	}

	@Override
	public int batchDelUserByIds(String[] ids) {
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		String sql = "update pubmodule_user_tbl set delTag = '1' where id in (" + idsStr + ")";
		this.baseDAO.execute(sql);
		return 1;
	}

	@Override
	public int delUserById(String id) {
		String sql = "update pubmodule_user_tbl set delTag = '1' where id = " + id;
		this.baseDAO.execute(sql);
		return 1;
	}

	@Override
	public int enableUser(String[] ids, String[] status) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			String st = status[i];
			String sql = "update pubmodule_user_tbl set status = '" + st + "' where id = " + id;
			this.baseDAO.execute(sql);
		}
		return 1;
	}

	@Override
	@Cacheable(value = "session1800",key="'user_'+#userid")
	public User getUserById(String userid) {
		User user = new User();
		user.setId(Long.parseLong(userid));
		User retObj = (User) this.findById(user);
		return retObj;
	}

	@Override
	public int grantRole(String userId, String[] roleIds) { 
		for (String rid : roleIds) {
			Userrole userrole = new Userrole();
			// 设置用户ID
			userrole.setUserId(Long.parseLong(userId));
			// 设置角色ID
			userrole.setRoleId(Long.parseLong(rid));
			this.save(userrole);
		}
		return 1;
	}

	@Override
	public Map login(User user) {
		Map retMap = new HashMap();
		// 根据用户名密码进行查询
		StringBuffer sb = new StringBuffer();		
		sb.append("select id, domain, department_id, trueName, userName, password, sex, signature, sortNo,  ");
		sb.append(" spelling,initials,director,email,mobileTelephone,isPublicMobileTelephone,lastLogIp, ");
		sb.append(" lastLogTime,lastLogType,loginCount,loginType,isCreateStaff,status,delTag ");
		sb.append("from pubmodule_user_tbl where delTag = '0' and userName = ? and password = ? ");
		Object[] parms = new Object[] { user.getUserName(), user.getPassword() };
		List<User> userList = (List<User>) this.find(sb.toString(), parms, User.class);
		if (userList.size() == 0) {
			// 查询用户是否存在
			String sql = "select id from pubmodule_user_tbl where delTag = '0' and userName = ? ";
			Object[] args = new Object[] { user.getUserName() };
			List<Map<String, Object>> userExistsList = this.find(sql, args);
			// 如果用户名存在
			if (userExistsList.size() > 0) {
				// 密码错误
				retMap.put("retCode", "3");
			} else {
				// 用户名不存在
				retMap.put("retCode", "2");
			}
		} else {
			// 判断当前用户有没有被禁用
			User loginUser = userList.get(0);
			if (loginUser.getStatus().equals("1")) {
				retMap.put("retCode", "1");
				retMap.put("retUser", userList.get(0));
			} else {
				// 用户已经被禁用
				retMap.put("retCode", "4");
				retMap.put("retUser", userList.get(0));
			}
		}
		return retMap;
	}

	@Override
	public int modifyUser(User user) { 
		return this.update2(user);
	}

	@Override
	public int modifyUserPwd(User user) { 
		String sql = "update pubmodule_user_tbl set password = '" + user.getPassword() + "',isUpdatePass='"+user.getIsUpdatePass()+"' where id = " + user.getId();
		this.baseDAO.execute(sql);
		return 1;
	}

	@Override
	public int resetUserPwd(User user) { 
		String pwd = MD5.md5("123456");
		user.setPassword(pwd);
		String sql = "update pubmodule_user_tbl set password = '" + user.getPassword() + "' where id = " + user.getId();
		this.baseDAO.execute(sql);
		return 1;
	}

	@Override
	public Map<String, Object> findById2Map(Serializable id) {
		String sql = "select * from pubmodule_user_tbl where id=?";
		return baseDAO.queryForMap(sql, id);
	}

	
	@Override
	public Map getDeptByUserId(Long id) {
		Map user = findById2Map(id);
		if (user != null) {
			String deptId = null;
			Object emp = user.get("isCreateStaff");
			String empId =  emp== null ? null : emp.toString();
			/**判断是否有用户-员工-岗位-部门关联,如果无关联，则直接以user表中department_id为准*/
			if (StringUtils.isBlank(empId)) {
					deptId = (String) user.get("department_id");
			}else{
				Map<String,Object> post = employeeService.getMainPostByEmpId(empId);
				if (post!=null) {
					deptId = (String)post.get("deptId");
				}
			}
			if(StringUtils.isNotBlank(deptId)){
				String sql = "select * from pubmodule_department_tbl where departmentid=?";
				return baseDAO.queryForMap(sql, deptId); 
			}
		}
		return null; 
	}

	@Override
	public User getDirectorByUserId(Long id) {
		List<User> userlist = new ArrayList<User>();
		User user = getUserById(id.toString()); 
		if(user==null){
			return null;
		}
		String empId =  user.getIsCreateStaff();
		if (StringUtils.isBlank(empId)) {
			if(StringUtils.isNotBlank(user.getDirector())){
				User director =  getUserById(user.getDirector());
				return director;
			}else{
				return null;
			}
		}else{
			//根据用户找到对应员工
			Employee employee = employeeService.getEmpByUserId(id.toString());
			
			//根据员工找到他的上级员工
			List<Map<String,Object>> emplist =null;
			if(employee!=null){
				String empid = employee.getId().toString();
				emplist = employeeService.getDirectorsByEmpId(empid);					
			}		
			
			//根据员工找到对应的用户
			if(emplist!=null&&emplist.size()>0){					
				for(int i=0;i<emplist.size();i++){
					String employeeid = emplist.get(i).get("id").toString();
					User user1 = employeeService.getUserByEmpId(employeeid);
					userlist.add(user1);
				}
				//设置用户上级字段
				User directUser= userlist.get(0);
				user.setDirector(directUser.getId().toString());
				update(user);
				return userlist.get(0);
			}
			return null;
		}
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		String sql = "select id,userName,trueName,status,lastLogTime from pubmodule_user_tbl where delTag='0' and status='1' and username<>'administrator' ";
		List users = this.baseDAO.query(sql, new Object[] {}, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(Long.parseLong(rs.getString("id")));
				user.setUserName(rs.getString("userName"));
				user.setTrueName(rs.getString("trueName"));
				user.setStatus(rs.getString("status"));
				return user;
			}
		});
		return users;
	}

	@Override
	public List<User> getDirectorsByUserId(Long id) {
		// TODO Auto-generated method stub
		List<User> userlist = new ArrayList<User>();
		User user = getUserById(id.toString());
		if(user!=null){
			//用户直接上级已经生成
			if(user.getDirector()!=null&&!"".equals(user.getDirector())){
				User director =  getUserById(user.getDirector());
				//return director;
			}else{
				//根据用户找到对应员工
				Employee employee = employeeService.getEmpByUserId(id.toString());
				
				//根据员工找到他的上级员工
				List<Map<String,Object>> emplist =null;
				if(employee!=null){
					String empid = employee.getId().toString();
					emplist = employeeService.getDirectorsByEmpId(empid);					
				}		
				
				//根据员工找到对应的用户
				if(emplist!=null&&emplist.size()>0){					
					for(int i=0;i<emplist.size();i++){
						String employeeid = emplist.get(i).get("id").toString();
						User user1 = employeeService.getUserByEmpId(employeeid);
						userlist.add(user1);
					}
				}
			}
		}
		return userlist;
	}
	
	@Override
	//@Cacheable(value = "session1800_sync",key="'fun_'+#userid")
	@MethodLogUtil(type="1",value="查找所有菜单集合")
	public Set<String> getAllFunctionSet() {
		Set<String> set = new HashSet<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("  select f.linkAddress,f.innerUrl from pubmodule_function_tbl f ");	 
		List<Function> funcList = (List<Function>) this.find(sb.toString(), new Object[] { }, Function.class);
		for(int i=0;i<funcList.size();i++){
			Function func = funcList.get(i);
			if(func.getLinkAddress()!=null&&!"".equals(func.getLinkAddress())){				
				String url = func.getLinkAddress();
				int dotIndex = url.indexOf(".");
				if(dotIndex>0){
					url = url.substring(0, dotIndex);
				}
				if(!url.startsWith("http:") && !url.startsWith("/")){
					url = "/"+url;
				}
				set.add(url);
			}
			if(func.getInnerUrl()!=null&&!"".equals(func.getInnerUrl())){
				String[] urlArr = func.getInnerUrl().split(",");
				for(String url : urlArr)
				{
					if(url.length()>0){
						int dotIndex = url.indexOf(".");
						if(dotIndex>0){
							url = url.substring(0, dotIndex);
						}
						if(!url.startsWith("http:") && !url.startsWith("/")){
							url = "/"+url;
						}
						set.add(url);						
					}
				}
			}			
		}		
		return set;
	}
	
	@Override
	//@Cacheable(value = "session1800_sync",key="'fun_'+#userid")
	@MethodLogUtil(type="1",value="查找当前用户菜单集合")
	public Set<String> getFunctionSetByUserId(String userid) { 
		Set<String> set = new HashSet<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("  select f.linkAddress,f.innerUrl from pubmodule_function_tbl f where exists ( ");
		sb.append("     select rf.functionId from pubmodule_rolefunction_tbl rf where f.functionId = rf.functionId and  exists ( ");
		sb.append("        select ur.role_Id from pubmodule_userrole_tbl ur where rf .role_Id = ur.role_Id ");
		if(StringUtils.isNotBlank(userid)) {
			sb.append(" and ur.user_id = "+userid);
		}
		sb.append("    )  ");
		sb.append(" ) order by f.sortNo asc");		 
		List<Function> funcList = (List<Function>) this.find(sb.toString(), new Object[] { }, Function.class);
		for(int i=0;i<funcList.size();i++){
			Function func = funcList.get(i);
			if(func.getLinkAddress()!=null&&!"".equals(func.getLinkAddress())){				
				String url = func.getLinkAddress();
				int dotIndex = url.indexOf(".");
				if(dotIndex>0){
					url = url.substring(0, dotIndex);
				}
				if(!url.startsWith("http:") && !url.startsWith("/")){
					url = "/"+url;
				}
				set.add(url);
			}
			if(func.getInnerUrl()!=null&&!"".equals(func.getInnerUrl())){
				String[] urlArr = func.getInnerUrl().split(",");
				for(String url : urlArr)
				{
					if(url.length()>0){
						int dotIndex = url.indexOf(".");
						if(dotIndex>0){
							url = url.substring(0, dotIndex);
						}
						if(!url.startsWith("http:") && !url.startsWith("/")){
							url = "/"+url;
						}
						set.add(url);						
					}
				}
			}			
		}		
		return set;
	}
	
	@Override
	public List<Map<String, Object>> buildMenuTreeByRoleId(String roleIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT distinct f.* FROM pubmodule_function_tbl f ")
			.append(" join  pubmodule_rolefunction_tbl rf on f.functionId=rf.functionId ")
			.append("  join pubmodule_role_tbl r on rf.role_id=r.id ")
			.append(" where r.id in("+roleIds+") order by parent_id,sortNo ");
		return this.getDAO().queryForList(sb.toString());
	}

	@Override
	public List<Role> getRoleByUserId(String id) {
		String sql = "select r.* from pubmodule_userrole_tbl u inner join pubmodule_role_tbl r on u.role_id = r.id and u.user_id = ?";
		return this.find(sql, new String[]{id}, Role.class);
	}
	
	@Override
	public Map getUsersByRoleId(String rid,PageInfo p)
	{
		String sql = "select u.id,u.trueName,u.userName from pubmodule_user_tbl u where delTag = '0' and exists (select ur.id from pubmodule_userrole_tbl ur where ur.user_id=u.id and ur.role_id= ?)";
		return this.findPageInfo(sql, new String[]{rid}, p);
		 
	}

	
	@Override
	public List<Role> getRoleByUserName(String userName) { 
		String sql = "select * from pubmodule_role_tbl  r where   exists( select ur.user_id from pubmodule_userrole_tbl ur where ur.role_id = r.id  and exists (select id from pubmodule_user_tbl u where u.id=ur.user_id and u.userName = ? ))";
		return this.getDAO().queryBySql2Entity(sql, new String[]{userName},Role.class);
	}

	@Override
	public List<User> getUsersByRoleCode(String... roleCode) { 
		List<String> in = new ArrayList<String>();
		for (int i = 0; i < roleCode.length; i++) {
			in.add("?");
		}
		String ins = StringUtils.join(in,","); 
		String sql = "select id,userName,trueName from pubmodule_user_tbl  u where   exists( select ur.user_id from pubmodule_userrole_tbl ur where ur.user_id = u.id  and exists (select id from pubmodule_role_tbl r where r.id=ur.role_id and r.role in( "+ins+" ) ))";
		return this.getDAO().queryBySql2Entity(sql, roleCode,User.class);
	}

	@Override
	public Map loginByUserName(User user) {
		Map retMap = new HashMap();
		// 根据用户名密码进行查询
		StringBuffer sb = new StringBuffer();		
		sb.append("select id, domain, department_id, trueName, userName, password, sex, signature, sortNo,  ");
		sb.append(" spelling,initials,director,email,mobileTelephone,isPublicMobileTelephone,lastLogIp, ");
		sb.append(" lastLogTime,lastLogType,loginCount,loginType,isCreateStaff,status,delTag ");
		sb.append("from pubmodule_user_tbl where delTag = '0' and userName = ?");
		Object[] parms = new Object[] { user.getUserName()};
		List<User> userList = (List<User>) this.find(sb.toString(), parms, User.class);
		if (userList.size() == 0) {
			// 用户名不存在
			retMap.put("retCode", "2");
		} else {
			// 判断当前用户有没有被禁用
			User loginUser = userList.get(0);
			if (loginUser.getStatus().equals("1")) {
				retMap.put("retCode", "1");
				retMap.put("retUser", userList.get(0));
			} else {
				// 用户已经被禁用
				retMap.put("retCode", "4");
				retMap.put("retUser", userList.get(0));
			}
		}
		return retMap;
	}

}

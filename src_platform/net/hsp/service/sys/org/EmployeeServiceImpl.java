package net.hsp.service.sys.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.Pinyin;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.crypt.MD5;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.entity.sys.org.Employee;
import net.hsp.entity.sys.org.Employeepost;
import net.hsp.entity.sys.org.Position;
import net.hsp.entity.sys.rbac.User;
import net.hsp.entity.sys.rbac.Userrole;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 员工信息业务类
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "员工信息业务类")
@Lazy(true)
public class EmployeeServiceImpl extends BaseServiceImpl implements EmployeeService { 

	@Autowired
	private PostService postService;
	
	@Autowired
	private EmployeepostService employeepostService;
	
	@MethodLogUtil(type="",value="多条件查询员工信息")
	public Map query(Map param, PageInfo p) {
//		StringBuilder sb = new StringBuilder("select id,empno,empname,sex,status,idCard,entryDate,departureDate,reusername,sortNo,remark,mobile,mobile2,officeTel,homeTel,qq,email,address,delTag from pubmodule_employee_tbl where delTag='0' "); 
		StringBuilder sb = new StringBuilder();
		sb.append(" select e.id as id,e.empno as empno,e.empname as empname, d.departmentName as deptName,  ");
		sb.append("       e.sex as sex,e.status as status,e.idCard as idCard,e.entryDate as entryDate, ");
		sb.append("       e.departureDate as departureDate,e.sysUserId as sysUserId,u.userName as userName,e.reusername as reusername,  ");
		sb.append("       e.sortNo as sortNo,e.remark as remark,e.mobile as mobile,p.postName as postName,   ");
		sb.append("    e.mobile2 as mobile2,e.officeTel as officeTel,e.homeTel as homeTel,  ");
		sb.append("   e.qq as qq,e.email as email,e.address as address,e.delTag as delTag  ");
		sb.append("  from pubmodule_employee_tbl e  ");
		sb.append(" LEFT JOIN pubmodule_department_tbl d on e.deptId = d.departmentId   ");
		sb.append(" LEFT JOIN pubmodule_employeepost_tbl ep on e.id = ep.empid  ");
		sb.append(" LEFT JOIN pubmodule_post_tbl p on ep.postId = p.id  and p.delTag = '0'");
		sb.append(" LEFT JOIN pubmodule_user_tbl u on u.id = e.sysUserId  ");
		sb.append(" where e.delTag = '0'   ");
		sb.append(" and ep.isMainPost='1' ");
		if(param != null) {//去除所有部门
			if("1".equals(param.get("e.deptId"))) {
				param.remove("e.deptId");
			}
			if("1".equals(param.get("p.deptId"))) {
				param.remove("p.deptId");
			}
		}

		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql")==null ? "" : sqlBuild.get("sql"));
 		if(p==null){
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args!=null) {
				list = this.find(sb.toString(), (Object[])args);
			}else{
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows",list);
			map.put("total",list.size());
 			return map;
	 	}else{
	 		return this.findPageInfo(sb.toString(), (Object[])sqlBuild.get("args"), p); 
	 	} 
	}


	@Override
	public List<Map<String, Object>> getOtherPostByEmpId(String empid) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select p.* from pubmodule_post_tbl p where p.delTag = ? ");
		sb.append("	and exists (select ep.id from pubmodule_employeepost_tbl ep "); 
		sb.append("  where ep.postId=p.id and  ep.isMainPost=? and ep.empid=?) ");
		List<Map<String,Object>> list = find(sb.toString(), new Object[]{"0","0",empid});		 
		return list;
	}
	
	@Override
	public Map<String,Object> getMainPostByEmpId(String empid) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from pubmodule_post_tbl p where p.delTag = ? ");
		sb.append("	and exists (select ep.id from pubmodule_employeepost_tbl ep "); 
		sb.append("  where ep.postId=p.id and  ep.isMainPost=? and ep.empid=?) ");
		List<Map<String,Object>> list = find(sb.toString(), new Object[]{"0","1",empid});
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Employee addEmployee(Employee employee,String[] postids) {
		// TODO Auto-generated method stub
		//得到排序号
		int sortNo = getSavingDataSortNo(employee);
		//更新受影响数据的排序号
		updateOthersSortNo(sortNo, true);
		//设置要保存数据的排序号
		employee.setSortNo(sortNo);
		this.save(employee);
		
		//保存员工岗位信息
		for(int i=0;i<postids.length;i++){			
			Employeepost employeepost = new Employeepost();
			employeepost.setEmpid(employee.getId());
			String[] postStr =  postids[i].split(",");
			employeepost.setPostId(Integer.parseInt(postStr[0]));
			employeepost.setDelTag("0");
			employeepost.setIsMainPost(postStr[1]);
			this.save(employeepost);
		}
		return employee;
	}

	@Override
	public int batchDelEmployeeByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		
		//得到需要删除数据的ID以及序号
		String sql1 = "select id,sortNo from pubmodule_employee_tbl where delTag = '0' and id in ("+ idsStr + ")";
		List<Map<String,Object>> list = this.find(sql1, new Object[]{});
		for(int i=0;i<list.size();i++){
			Map<String,Object> mp = list.get(i);
			//String id = mp.get("id").toString();
			String sno = mp.get("sortNo").toString();
			updateOthersSortNo(Integer.parseInt(sno), false);
		}
		
		//删除对应岗位表里的数据
		String sql2 = " delete from pubmodule_employeepost_tbl where empid in ("+ idsStr + ")";
		this.getDAO().execute(sql2);
		
		String sql = "update pubmodule_employee_tbl set delTag = '1' where id in ("
				+ idsStr + ")";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int delEmployeeById(String id) {
		// TODO Auto-generated method stub
		int sno = getDeletingDataSortNo(id);
		//更新其他受影响的数据的排序号
		updateOthersSortNo(sno, false);	
		String sql = "update pubmodule_employee_tbl set delTag = '1' where id = " + id;
		this.getDAO().execute(sql);
		
		String sql2 = " delete from pubmodule_employeepost_tbl where empid = " + id;
		this.getDAO().execute(sql2);
		
		return 1;
	}

	@Override
	public int modifyEmployee(Employee employee,String[] postids) { 
		
		
		
		//保存员工新的岗位信息
		//删除之前的岗位信息
		/**
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<postids.length;i++){
			String[] pst = postids[i].split(",");
			if(i==postids.length-1){
				sb.append(pst[0]);
			}else{
				sb.append(pst[0]+",");
			}
		}*/
		String sql = "delete from pubmodule_employeepost_tbl where empid="+employee.getId();
		getDAO().execute(sql);
		
		String editPostId = "";
		Set<String> set = new HashSet();
		for(int i=0;i<postids.length;i++){
			set.add(postids[i]);
		}
		for (String str : set) {
			Employeepost employeepost = new Employeepost();
			employeepost.setEmpid(employee.getId());
			String[] postStr =  str.split(",");
			employeepost.setPostId(Integer.parseInt(postStr[0]));
			employeepost.setDelTag("0");
			employeepost.setIsMainPost(postStr[1]);
			if("1".equals(postStr[1])){
				editPostId = postStr[0];
			}
			this.save(employeepost);
		} 
		
		Map<String,Object> post = this.getMainPostByEmpId(employee.getId().toString());
		//当前员工岗位发生变化
		String deptId = (String)post.get("deptId");
		employee.setDeptId(deptId);
		this.update(employee);
		
		//如果当前员工被用户关联 同步用户表里冗余查询字段department_id
		String sql2 = "update pubmodule_user_tbl set department_id = ? where isCreateStaff = ?";
		this.getDAO().update(sql2, deptId,employee.getId());
		return 1;
	}

	@Override
	public List<Map<String, Object>> buildTree(Map map, PageInfo p) { 
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.cls, t.id,t.parentId,t.name,t.sortNo from  ( ");
		sb.append("  	select 'dept' as cls,  CAST(d.departmentId as DBSTRING) as id,d.parent_Id as parentId,d.departmentName as name,d.sortNo as sortNo from pubmodule_department_tbl d where d.delTag = ? ");
		sb.append(" union ");
		sb.append(" 	select 'post' as cls,  CAST(p.id as DBSTRING) as id, CAST(p.deptId as DBSTRING) as parentId,p.postName as name, p.sortNo as sortNo from pubmodule_post_tbl p where p.delTag = ? ");
		sb.append(" ) t order by t.cls,t.sortNo asc ");
		String sql = DbInfo.castString(sb.toString(), 255, DbInfo.getDbType());
		list = (List<Map<String, Object>>)this.find(sql, new Object[]{"0","0"});
		
		for(int i=0;i<list.size();i++){
			Map<String,Object> mp = list.get(i);
			String cls = mp.get("cls").toString();
			String id = mp.get("id").toString();
			String parentId = mp.get("parentId").toString();
			if("dept".equals(cls)){
				mp.put("id","dept"+id);
				mp.put("parentId","dept"+parentId);				
			}else{
				mp.put("id","post"+id);
				mp.put("parentId","dept"+parentId);				
			}
		}	
		 
		return list;
	}

	@Override
	public int getDeletingDataSortNo(String id) {
		// TODO Auto-generated method stub
		int sortNo = 0;
		String sql = "select sortNo from pubmodule_post_tbl where delTag = 0 and id = ? ";
		List<Map<String,Object>> list = this.find(sql, new Object[]{id});
		if(list.size()>0){
			sortNo = Integer.parseInt(list.get(0).get("sortNo").toString());
		}
		return sortNo;
	}

	@Override
	public int getSavingDataSortNo(Employee employee) {
		// TODO Auto-generated method stub
		int sNo = 0;
		//新增数据
		if(employee.getId()==null||"".equals(employee.getId())){
			//post.getParentid();
			String sql = "select sortNo from pubmodule_employee_tbl where delTag = '0' order by sortNo desc";
			List<Map<String,Object>> list = this.find(sql, new Object[]{});
			if(list.size()==0){
				sNo = 1;
			}else{
				Map<String,Object> mp = list.get(0);
				String stno = mp.get("sortNo").toString();
				sNo = Integer.parseInt(stno)+1;
			}
		}else{
			//更新数据
			sNo = employee.getSortNo();
		}
		return sNo;
	}
 

	@Override
	public void updateOthersSortNo(int sortNo, boolean isAdd) {
		// TODO Auto-generated method stub
		String sql = "";
		if(isAdd){
			sql = "update pubmodule_employee_tbl set sortNo = sortNo+1 where sortNo>? ";			
		}else{
			sql = "update pubmodule_employee_tbl set sortNo = sortNo-1 where sortNo>? ";
		}
		this.getDAO().update(sql, sortNo);
	}

	@Override
	public Map batchCreateUser(String[] ids,String username,String pwd) {
		// TODO Auto-generated method stub
		//成功列表
		List<Map<String,Object>> succList = new ArrayList<Map<String,Object>>();
		//失败列表
		List<Map<String,Object>> failList = new ArrayList<Map<String,Object>>();
				
		//根据ID取得员工数据
		List<Employee> list = new ArrayList<Employee>();
		int len1 = ids.length;
		if(len1>0){
			StringBuffer mark = new StringBuffer();
			for(int i=0;i<len1;i++){
				mark.append("?");
				if(i<len1-1){
					mark.append(",");
				}
			}
			String sql1 = "select e.* from pubmodule_employee_tbl e where e.id in ("+mark+") ";			
			String[] args = new String[len1];			
			list = this.find(sql1, ids,Employee.class);
		}		
		//根据条件封装用户数据
		for(int i=0;i<list.size();i++){
			Employee emp = list.get(i);			
			//查询是否已经生成了用户
			String sql2 = "select id from pubmodule_user_tbl u where u.isCreateStaff = ? and delTag='0' ";
			List<Map<String,Object>> userlist = this.find(sql2, new Object[]{emp.getId().toString()});
			if(userlist.size()>0){
				//如果当前员工已经生成了用户则跳过
				Map<String,Object> fmp =new HashMap<String,Object>();
				fmp.put("empid", emp.getId());
				fmp.put("empno", emp.getEmpno());
				fmp.put("empname", emp.getEmpname());				
				fmp.put("errmsg", "1");
				failList.add(fmp);
				 
				continue;
			}else{
				//用户名
				String usrnm = "";
				if("1".equals(username)){
					usrnm = emp.getEmpno();					
				}else if("2".equals(username)){
					usrnm = emp.getEmpname();
				}else if("3".equals(username)){
					usrnm = emp.getMobile()!=null&&!"".equals(emp.getMobile())?emp.getMobile():emp.getEmpno();				
				}else if("4".equals(username)){
					usrnm = Pinyin.getPinYin(emp.getEmpname());
				}else{
					usrnm = emp.getEmpno();
				}
				//密码
				String passwd = "";
				if("PID".equals(pwd)){
					passwd = emp.getIdCard()!=null&&!"".equals(emp.getIdCard())?emp.getIdCard():"123456";					
				}else{
					passwd = pwd;
				}				
				//查询用户名是否已经存在
				String sql3 = "select id from pubmodule_user_tbl u where u.delTag='0' and u.userName= ?";
				List ulist = this.find(sql3, new Object[]{usrnm});
				if(ulist.size()>0){
					//用户名已经存在，跳过
					Map<String,Object> fmp =new HashMap<String,Object>();
					fmp.put("empid", emp.getId());
					fmp.put("empno", emp.getEmpno());
					fmp.put("empname", emp.getEmpname());				
					fmp.put("errmsg", "2");
					failList.add(fmp);					
					continue;					
				}
				
				//保存用户数据
				Map<String,Object> mainpost = getMainPostByEmpId(emp.getId().toString());
				User user = new User();
				user.setUserName(usrnm);
				user.setPassword(MD5.md5(passwd));
				user.setTrueName(emp.getEmpname());
				user.setIsCreateStaff(emp.getId().toString());				
				user.setDelTag("0");
				user.setStatus("1");
				user.setDepartmentId(mainpost.get("deptId")+"");
				this.save(user);
				
				//根据主岗位对应职位给予相应的角色				
				
				if(mainpost.get("positionId")!=null){
					Position position = new Position();
					position.setId(Integer.parseInt(mainpost.get("positionId").toString()));
					position = this.findById(position);
					if(position.getRefrole()!=null&&!"".equals(position.getRefrole())){
						Userrole userrole = new Userrole();
						userrole.setRoleId(Long.parseLong(position.getRefrole()));
						userrole.setUserId(user.getId());
						this.save(userrole);
					}
				}
				//回写员工对应的用户ID
				emp.setSysUserId(Integer.parseInt(user.getId().toString()));//设置生成后的用户名
				update(emp);
				
				//添加到成功列表
				Map<String,Object> smp =new HashMap<String,Object>();
				smp.put("empid", emp.getId());
				smp.put("empno", emp.getEmpno());
				smp.put("empname", emp.getEmpname());				
				smp.put("username", user.getUserName());				
				succList.add(smp);
			}
		}		
		
		Map mp = new HashMap();
		mp.put("succlist",succList);
		mp.put("faillist",failList);
		return mp;
	}	 

	@Override
	public List<Map<String, Object>> getDirectorsByEmpId(String empid) {
		// TODO Auto-generated method stub
		//根据人员得到岗位
		Map<String,Object> postMap = getMainPostByEmpId(empid);
		
		//根据岗位得到上级岗位
		Map<String,Object> parentPostMap = null;
		if(postMap!=null){
			String postId = postMap.get("id").toString();
			parentPostMap = postService.getParentPostById(postId) ;
			
		}else{
			//员工主岗信息为空			
		}
		
		//根据上级岗位得到相应的员工
		if(parentPostMap!=null){
			return getEmpListByPostId(parentPostMap.get("id").toString());			
		}else{
			//当前人员的上级岗位为空
		}		
		return null;
	}

	@Override
	public List<Map<String, Object>> getEmpListByPostId(String postId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select e.* from pubmodule_employee_tbl e where e.delTag = ? "); 
		sb.append(" 	  and exists (select id from pubmodule_employeepost_tbl ep ");
		sb.append(" 	   where ep.delTag=? and e.id=ep.empid  ");
		sb.append(" 	   and ep.isMainPost=? and ep.postId=? ) ");
		List<Map<String,Object>> list = find(sb.toString(), new Object[]{"0","0","1",postId});		 
		return list;
	}

	@Override
	public Employee getEmpByUserId(String userid) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();		 
		sb.append(" select e.* from pubmodule_employee_tbl e where e.delTag=? ");
		sb.append(" 	 and exists (select u.id from pubmodule_user_tbl u where u.delTag = ? ");
		sb.append(" 	  and e.id = u.isCreateStaff and u.id = ?) ");		
		List<Employee> list = find(sb.toString(), new Object[]{"0","0",userid},Employee.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public User getUserByEmpId(String empid) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();		 
		sb.append("select u.* from pubmodule_user_tbl u where u.delTag = ? ");
		sb.append("	 and exists (select e.id from pubmodule_employee_tbl e ");
		sb.append("where e.delTag = ? and e.id = u.isCreateStaff and e.id = ?) ");
		List<User> list = find(sb.toString(), new Object[]{"0","0",empid},User.class);
		if(list.size()>0){
			return list.get(0);
		}		
		return null;
	}

	@Override
	public List<Map<String, Object>> checkEmpIsCreatedUser(String[] ids) { 
		StringBuffer idstr = new StringBuffer();
		for(int i=0;i<ids.length;i++){
			if(i==ids.length-1){
				idstr.append("'"+ids[i]+"'");
			}else{
				idstr.append("'"+ids[i]+"',");
			}
		}
		String sql = "select u.* from pubmodule_user_tbl u where u.delTag='0' and u.isCreateStaff in ("+idstr.toString()+")";
		List<Map<String, Object>> list = this.find(sql, new Object[]{});
		return list;
	}


	@Override
	public Map doUserList(Map param, PageInfo p) { 
		StringBuilder sb = new StringBuilder("select username name,truename,u.id from pubmodule_user_tbl u inner join pubmodule_employee_tbl e on u.id = e.sysUserId  inner join pubmodule_employeepost_tbl p on e.id = p.empid  where p.ismainpost=1 and u.deltag =0 and e.deltag=0 ");
		Map sqlBuild = SQLBuild.buildEquals(param);
		sb.append(sqlBuild.get("sql")==null ? "" : sqlBuild.get("sql"));
		return this.findPageInfo(sb.toString(), (Object[])sqlBuild.get("args"), p);  
	}


	@Override
	public int linkToUser(String userId, String empid) {
		if(StringUtils.isBlank(empid) || StringUtils.isBlank(userId)) return 0;//参数不能为空
		String sql1 = "select e.* from pubmodule_employee_tbl e where e.id=? ";			
		List<Employee> list = this.find(sql1, new String[]{empid},Employee.class);
		if(list != null && list.size() > 0) {
			Employee emp = list.get(0);
			//查询是否已经生成了用户
			String sql2 = "select id from pubmodule_user_tbl u where u.isCreateStaff = ? ";
			List<User> userlist = this.find(sql2, new Object[]{emp.getId().toString()},User.class);
			if(userlist.size()>0){
				return 3;//当前员工已经生成了用户
			}else{
				String sql3 = "select u.* from pubmodule_user_tbl u where u.id = ? ";
				List<User> userlist2 = this.find(sql3, new Object[]{userId},User.class);
				if(userlist2 != null && userlist2.size() > 0) {
					User user = userlist2.get(0);
					if(StringUtils.isBlank(user.getIsCreateStaff())) {
						user.setIsCreateStaff(emp.getId().toString());	
						Map<String,Object> mainpost = getMainPostByEmpId(emp.getId().toString());
						user.setDepartmentId(mainpost.get("deptId")+"");
						update(user);	
						
						if(mainpost.get("positionId")!=null){
							Position position = new Position();
							position.setId(Integer.parseInt(mainpost.get("positionId").toString()));
							position = this.findById(position);
							if(position.getRefrole()!=null&&!"".equals(position.getRefrole())){
								Userrole userrole = new Userrole();
								userrole.setRoleId(Long.parseLong(position.getRefrole()));
								userrole.setUserId(user.getId());
								this.save(userrole);
							}
						}	
						
						emp.setSysUserId(Integer.parseInt(user.getId().toString()));
						update(emp);					
						return 1;//关联成功
					} else {
						return 5;//用户已经被关联
					}
				} else {
					return 4;//用户不存在
				}
				
			}
		} else {
			return 2;//员工不存在
		}
	}
}


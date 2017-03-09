package net.hsp.service.sys.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.dao.jdbc.DbInfo;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.entity.sys.rbac.Rolefunction;
import net.hsp.entity.sys.rbac.Userrole;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.FilterMap;
import net.hsp.web.util.PageInfo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;

/**
 * 角色业务类
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "角色业务类")
@Lazy(true)
public class RoleServiceImpl extends BaseServiceImpl implements RoleService { 

	
	@Autowired
	private FunctionService functionService;

	@MethodLogUtil(type="",value="多条件查询角色")
	public Map queryRole(Role role, FilterMap filterMap, PageInfo p) throws ServiceException{
 		String sql = "select id,delTag,parentPostNo,postNo,role,roleName,wayMark from pubmodule_role_tbl where delTag='0' "; 
		Map sqlBuild = SQLBuild.buildLike(filterMap);
 		sql += sqlBuild.get("sql"); 
		return this.findPageInfo(sql, (Object[])sqlBuild.get("args"), p); 
	}

	@Override
	public Role addRole(Role role) {
		// TODO Auto-generated method stub
		String sql = "select r.* from pubmodule_role_tbl r where r.delTag = ? and r.roleName= ? ";
		List list = find(sql,new Object[]{"0",role.getRoleName()});
		if(list.size()>0){
			return null;
		}else{
			this.save(role);
		}
		return role;
	}

	@Override
	public int addRefUser(String roleId, String[] userIds) {
		// TODO Auto-generated method stub
		for(String userid : userIds){
			Userrole userrole = new Userrole();
			// 设置用户ID
			userrole.setUserId(Long.parseLong(userid));
			// 设置角色ID
			userrole.setRoleId(Long.parseLong(roleId));
			this.save(userrole);
		}
		return 1;
	}

	@Override
	public int batchDelRoleByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		String sql = "update pubmodule_role_tbl set delTag = '1' where id in ("
				+ idsStr + ")";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int delRoleById(String id) {
		// TODO Auto-generated method stub
		String sql = "update pubmodule_role_tbl set delTag = '1' where id = " + id;
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public Role getRoleById(String id) {
		// TODO Auto-generated method stub
		Role role = new Role();
		role.setId(Long.parseLong(id));
		Role retRole = (Role)this.findById(role);		
		return retRole;
	}

	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		String sql = "select * from pubmodule_role_tbl where delTag= ? ";
		List<Role> roleLsit = (List<Role>)this.find(sql, new Object[]{"0"}, Role.class);
		return roleLsit;
	}

	@Override
	public int grantFunction(String roleId, String[] funcIds) {
		// TODO Auto-generated method stub
		for(String funcid : funcIds){
			Rolefunction rolefunc = new Rolefunction();
			// 设置用户ID
			rolefunc.setFunctionId(funcid);
			rolefunc.setRoleId(Long.parseLong(roleId));
			rolefunc.setType("1");
			this.save(rolefunc);
		}
		return 1;
	}

	@Override
	public int modifyRole(Role role) {
		// TODO Auto-generated method stub
		this.update(role);
		return 1;
	}

	@Override
	public List<Role> getRoleListByUserId(String id) {
		// TODO Auto-generated method stub
		String sql = "select r.id,r.role,r.roleName from pubmodule_role_tbl r where delTag = '0' and exists (select ur.id from pubmodule_userrole_tbl ur where ur.role_id=r.id and ur.user_id= ?)";
		List<Role> roleLsit = (List<Role>)this.find(sql, new Object[]{id}, Role.class);
		return roleLsit;
	}

	@Override
	public int grantRoles2User(String userid, String[] rids) { 
		//清空当前用户所有的角色授权
		String sql1 = "delete from pubmodule_userrole_tbl where user_id = "+userid;
		this.getDAO().execute(sql1);	
		
		//新增用户的角色授权
		for(String rid :rids){
			Userrole userrole = new Userrole();
			userrole.setUserId(Long.parseLong(userid));
			userrole.setRoleId(Long.parseLong(rid));
			save(userrole);
		}
		return 1;
	}

	@Override
	public Role getRoleByCode(String code) { 
		String sql = "select r.* from pubmodule_role_tbl r where delTag='0' and r.role = ? ";
		List<Role> list = (List<Role>)find(sql,new Object[]{code},Role.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Role> getRolesByKeyword(String keyword) { 
		String sql = "select r.* from pubmodule_role_tbl r where delTag='0' and ( r.role like ? or r.roleName like ? ) ";
		List<Role> list = (List<Role>)find(sql,new Object[]{"%"+keyword+"%","%"+keyword+"%"},Role.class);
		return list;
	}

	@Override
	public List<Map<String, Object>> buildRoleUserTree(Map map, PageInfo p) {
		String sql = "select t.cls, t.id,t.parentId as pid,t.name,t.did,t.truename from  (select 'd' as cls, CAST(d.id as DBSTRING) as id,'-1' as did,'0' as parentId,d.rolename as name,rolename as truename from pubmodule_role_tbl d where d.delTag = 0  union  	(select 'u' as cls, '-1' as id,CAST(p.user_id as DBSTRING) as did, CAST(p.role_id as DBSTRING)  as parentId,u.username name,u.truename as truename  from pubmodule_userrole_tbl p inner join pubmodule_user_tbl u on u.id = p.user_id where u.deltag=0)) t order by t.cls asc";
		sql = DbInfo.castString(sql, 20, DbInfo.getDbType());
		return getDAO().queryForList(sql);
	}
	
	public Map queryUsersByRoleId(String roleId, Map param, PageInfo p){
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.id,u.userName,u.trueName from pubmodule_user_tbl u ")
			.append(" join pubmodule_userrole_tbl ur on ur.user_id = u.id ")
			.append(" join pubmodule_role_tbl r on r.id = ur.role_id and r.id = ? and r.delTag ='0' ")
			.append(" where u.delTag='0' and u.status='1' ");	
		Set keySet = param.keySet();
		Iterator ketIt = keySet.iterator();
		while(ketIt.hasNext()){
			String key = ketIt.next().toString();
			if(!"".equals(param.get(key)) && param.get(key) != null){
				sb.append(" and u."+key+" like '%"+param.get(key)+"%' ");
			}
		}
		if(p == null){
			Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> list = null;
			list = this.getDAO().queryForList(sb.toString(), new Object[]{roleId});
			map.put("rows",list);
			map.put("total",list.size());
			return map;
	 	}else{
	 		return this.findPageInfo(sb.toString(), new Object[]{roleId}, p); 
	 	} 
	}
	
	public List<Map<String,Object>> queryUsersByRoleId(String roleId, Map param)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.id,u.userName,u.trueName from pubmodule_user_tbl u ")
			.append(" join pubmodule_userrole_tbl ur on ur.user_id = u.id ")
			.append(" join pubmodule_role_tbl r on r.id = ur.role_id and r.id = ? and r.delTag ='0' ")
			.append(" where u.delTag='0' and u.status='1' ");	
		Set keySet = param.keySet();
		Iterator ketIt = keySet.iterator();
		while(ketIt.hasNext()){
			String key = ketIt.next().toString();
			if(!"".equals(param.get(key)) && param.get(key) != null){
				sb.append(" and u."+key+" like '%"+param.get(key)+"%' ");
			}
		}
		List<Map<String,Object>> list = this.getDAO().queryForList(sb.toString(), new Object[]{roleId});
		return list;
	}
	
	
	public int giveRoleToUser(String roleId, String userId)
	{
		String sql = "select * from pubmodule_userrole_tbl where role_id = ? and user_id = ?";
		List<Map<String,Object>> list = this.getDAO().queryForList(sql, new Object[]{roleId, userId});
		if(list.size() <= 0)
		{
			Userrole userrole = new Userrole();
			// 设置用户ID
			userrole.setUserId(Long.parseLong(userId));
			// 设置角色ID
			userrole.setRoleId(Long.parseLong(roleId));
			this.save(userrole);
		}
		else
		{
			System.out.println("表中已经有roleId="+roleId+" userId="+userId);
		}
		return 1;
	}
	
	public int removeRoleFromUser(String roleId, String userId)
	{
		String dsql = "delete from pubmodule_userrole_tbl where role_id = ? and user_id = ?";
		this.getDAO().update(dsql, new Object[]{ roleId,userId });
		return 1;
	}
	
	/** 更新一部分用户身上的某一特定角色 */
	public int updateRoleInSomeUsers(String roleId, String[] pageUids, String[] selectUids)
	{
		List<String> selects = new ArrayList<String>();
		CollectionUtils.addAll(selects, selectUids);
		for(int i = 0; i < pageUids.length; i++)
		{
			String puid = pageUids[i];
			if(selects.indexOf(puid) != -1)
			{
				giveRoleToUser(roleId, puid);
			}
			else
			{
				removeRoleFromUser(roleId, puid);
			}
		}
		return 1;
	}
	
	@Override
	public int grantUsers2Role(String rid, String[] userids) {
		//清空当前用户所有的角色授权
		String sql1 = "delete from pubmodule_userrole_tbl where role_id = "+rid;
		this.getDAO().execute(sql1); 
		if(userids.length > 0){
			//新增用户的角色授权
			for(String userid : userids){
				if(StringUtils.isBlank(userid)) {
					continue;
				}
				Userrole userrole = new Userrole();
				userrole.setUserId(Long.parseLong(userid));
				userrole.setRoleId(Long.parseLong(rid));
				save(userrole);
			}
		}
		return 1;
	}
}


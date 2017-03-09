package net.hsp.service.sys.org;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.org.Position;
import net.hsp.entity.sys.rbac.Role;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.sys.rbac.RoleService;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 职位信息业务类
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "职位信息业务类")
@Lazy(true)
public class PositionServiceImpl extends BaseServiceImpl implements PositionService { 

	@Autowired
	private RoleService roleService;
	
	@MethodLogUtil(type="",value="多条件查询职位信息")
	public Map query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select p.id,p.positionNo,p.positionName,p.delTag,p.remark ,p.refrole,r.role,r.roleName ");
		sb.append(" from pubmodule_position_tbl p ");
		sb.append(" left join  pubmodule_role_tbl r on r.id = p.refrole ");
		sb.append(" where p.delTag='0' ");		
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
	public Position addPosition(Position position) {
		// TODO Auto-generated method stub		
		if(position.getRefrole()!=null&&!"".equals(position.getRefrole())){
			if("@CREATEROLE".equals(position.getRefrole())){
				//查询角色是否存在
				Role roleObj = roleService.getRoleByCode(position.getPositionNo());
				if(roleObj ==null){
					//创建角色
					Role role = new Role();
					role.setRole(position.getPositionNo());
					role.setRoleName(position.getPositionName());
					role.setDelTag("0");
					roleService.addRole(role);
					position.setRefrole(role.getId().toString());					
				}else{
					position.setRefrole(roleObj.getId().toString());
				}
			}
		}		
		this.save(position);		
		return position;
	}

	@Override
	public int batchDelPositionByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		String sql = "update pubmodule_position_tbl set delTag = '1' where id in ("
				+ idsStr + ")";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int delPositionById(String id) {
		// TODO Auto-generated method stub
		String sql = "update pubmodule_position_tbl set delTag = '1' where id = " + id;
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public int modifyPosition(Position position) {
		// TODO Auto-generated method stub
		if(position.getRefrole()!=null&&!"".equals(position.getRefrole())){
			if("@CREATEROLE".equals(position.getRefrole())){
				Role roleObj = roleService.getRoleByCode(position.getPositionNo());
				if(roleObj ==null){
					//创建角色
					Role role = new Role();
					role.setRole(position.getPositionNo());
					role.setRoleName(position.getPositionName());
					role.setDelTag("0");
					roleService.addRole(role);
					position.setRefrole(role.getId().toString());
				}else{
					position.setRefrole(roleObj.getId().toString());					
				}
			}
		}
		this.update(position);
		return 1;
	}

	@Override
	public List<Position> getAllPosition() {
		// TODO Auto-generated method stub
		String sql = "select id,positionNo,positionName,delTag,remark from pubmodule_position_tbl where delTag = ?";		
		List<Position> list = (List<Position>)this.find(sql, new Object[]{"0"}, Position.class);
		return list;
	}

	@Override
	public Position getPositionByNo(String positionNo) {
		// TODO Auto-generated method stub
		String sql = "select p.* from pubmodule_position_tbl p where p.delTag='0' and p.positionNo = ?";
		List<Position> list = find(sql,new Object[]{positionNo}, Position.class);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}


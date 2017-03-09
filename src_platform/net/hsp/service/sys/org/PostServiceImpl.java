package net.hsp.service.sys.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.org.Post;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 岗位信息业务类
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "岗位信息业务类")
@Lazy(true)
public class PostServiceImpl extends BaseServiceImpl implements PostService { 

	@MethodLogUtil(type="",value="多条件查询岗位信息")
	public Map query(Map param, PageInfo p) {
		String deptId = (String) param.get("p.deptId");
		param.remove("p.deptId");
		StringBuilder sb = new StringBuilder();
		sb.append(" select p.id as id,p.postNo as postNo,p.deptId as deptId,d.departmentName as deptName,p.parentId, ");
		sb.append("         p1.postName as parentName,  p.positionId as positionId,s.positionName as positionName, ");
		sb.append("         p.postName as postName,p.shortName as shortName,p.sortNo as sortNo,p.isDeptHead as isDeptHead  ");
		sb.append(" from pubmodule_post_tbl p ");
		sb.append(" left join pubmodule_department_tbl d on p.deptId=d.departmentId ");
		sb.append(" left join pubmodule_post_tbl p1 on p.parentId = p1.id ");
		sb.append(" left join pubmodule_position_tbl s on p.positionId=s.id ");
		sb.append(" where p.delTag='0' "); 
		//StringBuilder sb = new StringBuilder("select id,postNo,deptId,parentId,postName,shortName,sortNo,isDeptHead from pubmodule_post_tbl where delTag='0' "); 
		if (StringUtils.isNotBlank(deptId)) {
			sb.append(" and p.deptId = '" + deptId + "'");
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
	public List<Map<String, Object>> buildTree(Map map,PageInfo p) {
		// TODO Auto-generated method stub
		String p1 = map.get("showType").toString();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();	
		if("0".equals(p1)){
			sb.append(" select t.cls, t.id,t.parentId,t.name,t.sortNo from  ( ");
			sb.append("  	select 'dept' as cls, CAST(d.departmentId as CHAR) as id,CAST(d.parent_Id as CHAR) as parentId,d.departmentName as name,d.sortNo as sortNo from pubmodule_department_tbl d where d.delTag = ? ");
			sb.append(" union ");
			sb.append(" 	select 'post' as cls, CAST(p.id as CHAR) as id, CAST(p.deptId as CHAR)  as parentId,p.postName as name, p.sortNo as sortNo from pubmodule_post_tbl p where p.delTag = ? ");
			sb.append("  and not exists (select d1.departmentId from pubmodule_department_tbl d1 where d1.delTag='1' and  d1.departmentId = p.deptId ) ");
			sb.append(" ) t order by t.cls,t.sortNo asc ");
			list = (List<Map<String, Object>>)this.find(sb.toString(), new Object[]{"0","0"});
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
		}else{			
			sb.append("select 'post' as cls,p.id as id,p.parentId as parentId,p.postName as name,p.sortNo as sortNo from pubmodule_post_tbl p where p.delTag = ? order by p.sortNo asc");
			list = (List<Map<String, Object>>)this.find(sb.toString(), new Object[]{"0"});		
		}	 
		System.out.println(list);
		return list;
	}
	
	@Override
	public Post addPost(Post post) { 
		//得到排序号
		int sortNo = getSavingDataSortNo(post);
		//更新受影响数据的排序号
		updateOthersSortNo(sortNo, true);
		//设置要保存数据的排序号
		post.setSortNo(sortNo);
		this.save(post);
		//更新受影响数据的hasChild字段
		updateHasChild();
		return post;
	}

	@Override
	public int batchDelPostByIds(String[] ids) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for (String s : ids) {
			sb.append(s).append(",");
		}
		String idsStr = sb.substring(0, sb.length() - 1);
		
		//得到需要删除数据的ID以及序号
		String sql1 = "select id,sortNo from pubmodule_post_tbl where delTag = '0' and id in ("+ idsStr + ")";
		List<Map<String,Object>> list = this.find(sql1, new Object[]{});
		for(int i=0;i<list.size();i++){
			Map<String,Object> mp = list.get(i);
			String id = mp.get("id").toString();
			String sno = mp.get("sortNo").toString();
			updateOthersSortNo(Integer.parseInt(sno), false);
		}
		String sql = "update pubmodule_post_tbl set delTag = '1' where id in ("
				+ idsStr + ")";
		this.getDAO().execute(sql);
		//跟新关联数据的hasChild字段
		updateHasChild();
		return 1;
	}

	@Override
	public int delPostById(String id) {
		// TODO Auto-generated method stub
		int sno = getDeletingDataSortNo(id);
		//更新其他受影响的数据的排序号
		updateOthersSortNo(sno, false);		
		String sql = "update pubmodule_post_tbl set delTag = '1' where id = " + id;
		this.getDAO().execute(sql);
		updateHasChild();
		return 1;
	}

	@Override
	public int modifyPost(Post post) { 
		Post temp = this.findById(post);
		//变更岗位的相关部门时，关联当前岗位的相关员工的deptId级联变更
		if(StringUtils.isNotBlank(post.getDeptId())){
			if (!temp.getDeptId().equals(post.getDeptId())) {
				//变更员工表
				String sql1 = "update pubmodule_employee_tbl set deptId = ? where id in(select empid from pubmodule_employeepost_tbl where postId = ?)";
				this.getDAO().update(sql1, post.getDeptId(),post.getId());
				//当用户和员工已经关联过时  此处需同步变更用户表里的department_id字段
				String sql2 = "update pubmodule_user_tbl set department_id = ? where isCreateStaff in (select empid from pubmodule_employeepost_tbl where postId = ?)";
				this.getDAO().update(sql2, post.getDeptId(),post.getId());
			}
		}
		this.update(post);
		return 1;
	}

	@Override
	public int getSavingDataSortNo(Post post) {
		// TODO Auto-generated method stub
		int sNo = 0;
		//新增数据
		if(post.getId()==null||"".equals(post.getId())){
			//post.getParentid();
			String sql = "select sortNo from pubmodule_post_tbl where delTag = '0' and parentId=? order by sortNo desc";
			List<Map<String,Object>> list = this.find(sql, new Object[]{post.getParentId()});
			if(list.size()==0){
				sNo = 1;
			}else{
				Map<String,Object> mp = list.get(0);
				String stno = mp.get("sortNo").toString();
				sNo = Integer.parseInt(stno)+1;
			}
		}else{
			//更新数据
			sNo = post.getSortNo();
		}
		return sNo;
	}

	@Override
	public int getDeletingDataSortNo(String id) {
		// TODO Auto-genrated method stub
		int sortNo = 0;
		String sql = "select sortNo from pubmodule_post_tbl where delTag = 0 and id = ? ";
		List<Map<String,Object>> list = this.find(sql, new Object[]{id});
		if(list.size()>0){
			sortNo = Integer.parseInt(list.get(0).get("sortNo").toString());
		}
		return sortNo;
	}
	
	@Override
	public void updateOthersSortNo(int sortNo, boolean isAdd) {
		// TODO Auto-generated method stub
		String sql = "";
		if(isAdd){
			sql = "update pubmodule_post_tbl set sortNo = sortNo+1 where sortNo>? ";			
		}else{
			sql = "update pubmodule_post_tbl set sortNo = sortNo-1 where sortNo>? ";
		}
		this.getDAO().update(sql, sortNo);
	}

	@Override
	public void updateHasChild() {
		// TODO Auto-generated method stub
		//把有子节点的child=0字段更新为1
		//String sql1 = "update pubmodule_post_tbl p set p.hasChild='1' where p.delTag ='0' and p.hasChild='0' and exists (select id from pubmodule_post_tbl p1 where p1.delTag='0' and p1.parentId = p.id)";
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" update pubmodule_post_tbl p set p.hasChild='1'  ");
		sb1.append(" where p.id in ( ");
		sb1.append(" select id from  ");
		sb1.append("  (  ");
		sb1.append(" 	select p2.id from pubmodule_post_tbl p2 where p2.delTag ='0' and p2.hasChild='0' and   ");
		sb1.append("    exists (select p1.id from pubmodule_post_tbl p1 where p1.delTag='0' and p1.parentId = p2.id) ");
		sb1.append("  ) a ");
		sb1.append(" ) ");
		this.getDAO().execute(sb1.toString());
		
		//把没有子节点child=1字段跟新为0
		//String sql2 = "update pubmodule_post_tbl p set p.hasChild='0' where p.delTag ='0' and p.hasChild='1' and not exists (select id from pubmodule_post_tbl p1 where p1.delTag='0' and p1.parentId = p.id)";
		StringBuffer sb2 = new StringBuffer();
		sb2.append(" update pubmodule_post_tbl p set p.hasChild='0'  ");
		sb2.append(" where p.id in ( ");
		sb2.append(" select id from  ");
		sb2.append("  (  ");
		sb2.append(" 	select p2.id from pubmodule_post_tbl p2 where p2.delTag ='0' and p2.hasChild='1' and not  ");
		sb2.append("   exists (select p1.id from pubmodule_post_tbl p1 where p1.delTag='0' and p1.parentId = p2.id) ");
		sb2.append("  ) a ");
		sb2.append(" ) ");
		this.getDAO().execute(sb2.toString());
	}

	@Override
	public int changePostLevel(String id, String type, String parentId,
			String[] sortList) {
		// TODO Auto-generated method stub
		//更新父节点
		if("1".equals(type)){
			String sql1 = "update pubmodule_post_tbl p set p.parentId=? where p.id=? ";
			this.getDAO().update(sql1, new Object[]{parentId,id});
		}
		//更新所有关联节点的顺序
		if(sortList!=null&&sortList.length>0){
			for(String str : sortList){
				if(str==null||"".equals(str)){					
					break;
				}
				String[] arr = str.split(",");
				String sql2 = "update pubmodule_post_tbl p set p.sortNo=? where p.id=? ";
				this.getDAO().update(sql2, new Object[]{arr[1],arr[0]});
			}
		}		
		//更新haschild字段
		updateHasChild();
		
		return 1;
	}

	@Override
	public Map<String,Object> getParentPostById(String postId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from pubmodule_post_tbl p1 where p1.delTag= ? ");
		sb.append("	 and EXISTS (select p2.id from pubmodule_post_tbl p2 ");
		sb.append("  where p2.delTag= ? and p1.id=p2.parentId and p2.id=? ) ");
		List<Map<String,Object>> list = find(sb.toString(), new Object[]{"0","0",postId});
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Post getPostByCode(String code) {
		// TODO Auto-generated method stub
		String sql = "select p.* from pubmodule_post_tbl p where p.delTag = '0' and p.postNo = ?";
		List<Post> list = find(sql,new Object[]{code},Post.class);
		if(list.size()>0){
			return list.get(0);			
		}
		return null;
	}

	@Override
	public int delPostByDeptId(String[] deptIds) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<deptIds.length;i++){ 
			sb.append("'"+deptIds[i]+"'");
			if (i!=deptIds.length-1) {
				sb.append(",");
			}
		}

		//得到需要删除数据的ID以及序号
		String sql1 = "select id,sortNo from pubmodule_post_tbl where delTag = '0' and deptId in ("+ sb.toString() + ")";
		List<Map<String,Object>> list = this.find(sql1, new Object[]{});
		for(int i=0;i<list.size();i++){
			Map<String,Object> mp = list.get(i);
			String id = mp.get("id").toString();
			String sno = mp.get("sortNo").toString();
			updateOthersSortNo(Integer.parseInt(sno), false);
		}
		
		//删除部门对应的岗位信息
		String sql = "update pubmodule_post_tbl set delTag='1' where delTag='0' and deptId in ("+sb.toString()+")";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public Post getDeptHeadPostByDeptId(String deptId) {
		// TODO Auto-generated method stub
		String sql = "select p.* from pubmodule_post_tbl p where p.delTag = ? and  p.isDeptHead = ? and p.deptId = ? ";
		List<Post> postList = find(sql,new String[]{"0","1",deptId},Post.class);
		if(postList.size()>0){
			return postList.get(0);
		}
		return null;
	}
		
}


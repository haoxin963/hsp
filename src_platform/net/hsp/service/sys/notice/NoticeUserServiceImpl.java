package net.hsp.service.sys.notice;

import java.util.List;

import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.rbac.User;
import net.hsp.service.BaseServiceImpl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * pubmodule_noticeuser_tbl业务类
 * 
 * @author smartTools
 */
@Service
@ServiceLogUtil(name = "pubmodule_noticeuser_tbl业务类")
@Lazy(true)
public class NoticeUserServiceImpl extends BaseServiceImpl implements NoticeUserService {
	/*
	 * @MethodLogUtil(type="",value="多条件查询pubmodule_noticeuser_tbl") public Map<String,Object>
	 * query(Map param, PageInfo p) { StringBuilder sb = new
	 * StringBuilder("select id,noticeId,userId,remark,status,updateTime from
	 * pubmodule_noticeuser_tbl where 1=1"); Map sqlBuild =
	 * SQLBuild.buildLike(param); sb.append(sqlBuild.get("sql")==null ? "" :
	 * sqlBuild.get("sql")); if(p==null){ Map<String,Object> map = new HashMap<String,Object>();
	 * List<Map<String,Object>> list = null; Object args =
	 * sqlBuild.get("args"); if (args!=null) { list = this.find(sb.toString(),
	 * (Object[])args); }else{ list = this.getDAO().queryForList(sb.toString()); }
	 * map.put("rows",list); map.put("total",list.size()); return map; }else{
	 * return this.findPageInfo(sb.toString(), (Object[])sqlBuild.get("args"),
	 * p); } }
	 */
	public void createNoticeUsers(int noticeId, String userIds) {
		String[] ids = userIds.split(",");
		String[] sqls = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			sqls[i] = "INSERT INTO pubmodule_noticeuser" + i % 10 + "_tbl (noticeId,userId) VALUES (" + noticeId + "," + Integer.parseInt(ids[i]) + ");";
		}
		this.getDAO().batchUpdate(sqls);
	}

	public void createNoticeUsers(int noticeId) {
		String sql = "select * from pubmodule_user_tbl where delTag = 0 and status = 1";
		List<User> list = (List<User>) this.find(sql, new Object[] {}, User.class);
		String[] sqls = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			User user = (User) list.get(i);
			sqls[i] = "INSERT INTO pubmodule_noticeuser" + i % 10 + "_tbl (noticeId,userId) VALUES (" + noticeId + "," + user.getId() + ");";
		}
		this.getDAO().batchUpdate(sqls);
	}
	/*
	 * public boolean saveReceipt(String noticeId , String userId, String
	 * content){ String sql="select"; }
	 *  /* private void initNoticeUserTable(){
	 * 
	 * try { ResultSet rs =
	 * this.baseDAO.getDataSource().getConnection().getMetaData().getTables(null,
	 * null,"pubmodule_noticeuser9_tbl", null); if (!rs.next()) { for(int i=1;i<=9;i++){
	 * this.getDAO().execute("CREATE TABLE pubmodule_noticeuser"+i+"_tbl LIKE
	 * pubmodule_noticeuser0_tbl"); } } } catch (Exception e) {
	 * System.err.println(e); e.printStackTrace (); } }
	 */
}

package net.hsp.service.sys.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.constants.PlatFormConstant;
import net.hsp.entity.sys.rbac.User;
import net.hsp.entity.sys.weixin.WeixinApp;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

public class WeixinServiceImpl extends BaseServiceImpl implements WeixinService {

	@SuppressWarnings("unchecked")
	@MethodLogUtil(type="",value="多条件查询微信")
	public Map<String,Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select appId,secret,custId,userName, id from uisp_weixinapp_tbl where 1=1"); 
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
	@MethodLogUtil(type = "", value = "根据openId查询对应userId")
	public String getUserIdByOpenId(String openId) {
		String sql = "select userId from pubmodule_weixinUser_tbl where openId = ?"; 
		try {
			return this.getDAO().queryForObject(sql, String.class, openId);
		} catch (Exception e) { 
			return null;
		}
	}
	
	
	@Override
	@MethodLogUtil(type = "", value = "根据userId查询对应OpenId")
	public String getOpenIdByUserId(String userId) {
		String sql = "select openId from pubmodule_weixinUser_tbl where userId = ?"; 
		try {
			return this.getDAO().queryForObject(sql, String.class, userId);
		} catch (Exception e) { 
			return null;
		}
	}

	
	@Override
	@MethodLogUtil(type = "", value = "创建用户和微信openId关系")
	public boolean createMapping(String appId, String openId, String userId, String userName, String password, String userType) {
		String sql = "insert into pubmodule_weixinUser_tbl(appId,openId,userId,userName,password,userType,createTime)values(?,?,?,?,?,?,?)";
		try {
			return this.getDAO().update(sql, appId, openId, userId, userName, password ,userType, new Date()) > 0;
		} catch (Exception e) {
			return false;
		}
	}

	
	@Override
	@MethodLogUtil(type = "", value = "更新某个openId的位置信息")
	public boolean updateGPS(String openId, String x, String y) {
		String sql = "update pubmodule_weixinUser_tbl set x = ?,y=? where openId = ?";
		try {
			return this.getDAO().update(sql, openId, x, y) > 0;
		} catch (Exception e) {
			return false;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public WeixinApp getWeixinAppByCustId(String custId) {
		String sql = "select * from uisp_weixinapp_tbl where custId = ?";
		RowMapper<WeixinApp> rowMapper = new BeanPropertyRowMapper(WeixinApp.class);
		try {
			return this.getDAO(PlatFormConstant.BASESTATIONID).queryForObject(sql, rowMapper,custId);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean deleteMapping(String openId){
		String sql = " delete from pubmodule_weixinUser_tbl where openId = ? ";
		try{
			this.getDAO().update(sql, openId);
			return true;
		}catch (Exception e) {
			return false;
		}
			
	}
	
	public boolean checkMapping(String userName,String openId){
		String sql = " select * from pubmodule_weixinUser_tbl where userName = ? and openId = ? ";
		List<Map<String,Object>> list = this.find(sql, new Object[]{userName, openId});
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean chkUserIsExist(String appId, String userId){
		String sql = " select * from pubmodule_weixinUser_tbl where appId = ? and userId = ? ";
		List<Map<String,Object>> list = this.find(sql, new Object[]{appId, userId});
		if(list.size() > 0){
			return true;
		}else{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public User getUserByUserId(String userId){
		String sql = "select userName,password from pubmodule_weixinUser_tbl where userId = ?"; 
		try {
			RowMapper<User> rowMapper = new BeanPropertyRowMapper(User.class);
			return this.getDAO().queryForObject(sql, rowMapper, userId);
		} catch (Exception e) { 
			return null;
		}
	}
	
	public boolean modifyUserPwd(String userId,String pwd){
		String sql = " update pubmodule_weixinUser_tbl set password ='"+pwd+"' where userId='"+userId+"'";
		this.getDAO().execute(sql);
		return true;
	}
}

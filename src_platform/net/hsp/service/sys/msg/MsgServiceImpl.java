package net.hsp.service.sys.msg;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.hsp.common.CacheUtil;
import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.msg.Message;
import net.hsp.entity.sys.msg.MessageStatus;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.sys.app.AppPushMessage;
import net.hsp.service.sys.app.UserAppService;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;

/**
 * pubmodule_message_tbl业务类
 * 
 * @author lk0516
 */ 
@ServiceLogUtil(name = "站内信业务类")
@Lazy(true)
public class MsgServiceImpl extends BaseServiceImpl implements MessageService {
	
	@Autowired
	private UserAppService userAppService;
	
	@Override
	public int[] batchDelete(Class cls, String[] pks) throws ServiceException {

		String id = "";
		for (int i = 0; i < pks.length; i++) { 
			id +=pks[i]+",";
		}
		id +="0";
		//this.getDAO().update("delete from pubmodule_message_status_tbl where messageid = ?",id);		
		return getDAO().batchDelete(cls, pks);
	}

	 

	@Override
	public int delete(Object entity) throws ServiceException { 
		//this.getDAO().update("delete from pubmodule_message_status_tbl where messageid = ?",((Message) entity).getId());	
		return super.delete(entity);
	}

	@MethodLogUtil(type = "", value = "多条件查询站内信")
	public Map<String, Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select id,title,message,sendtime from pubmodule_message_tbl where 1=1");
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	@MethodLogUtil(type = "", value = "多条件查询")
	public Map<String, Object> query(String userid, Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select m.*,ms.status from pubmodule_message_tbl m inner join pubmodule_message_status_tbl ms on ms.messageid=m.id where 1=1");
		String status = param.get("status")+"";
		if (!"".equals(status)) {
			if ("1".equals(status) || "2".equals(status)) {
				sb.append(" and m.id in (select messageid from pubmodule_message_status_tbl where userid="+userid+" and status= "+ status +")");
			}else {
				sb.append(" and m.id  in (select messageid from pubmodule_message_status_tbl where userid="+userid+" and status!=0)");
			}
		} else {
			sb.append(" and m.id in (select messageid from pubmodule_message_status_tbl where userid="+userid+" and status!=0)");
		}
		param.remove("status");
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	@MethodLogUtil(type = "0", value = "查询未读数") 
	@Cacheable(value = "session1800_sync",key="'msg_'+#userid")
	public Object unread(String userid) {
		String sql = "select count(*) as total from pubmodule_message_status_tbl where userid=" + userid + " and status =1";
		return this.find(sql, null).get(0).get("total");
	}

	@MethodLogUtil(type = "", value = "阅读消息，改变阅读状态") 
	public void readSign(String userid, String messageid) {
		String sql = "update pubmodule_message_status_tbl set status = 2 where userid= ?  and messageid = ?";
		this.getDAO().update(sql,userid,messageid); 
		CacheUtil cache = CacheUtil.getInstance();
		Cache c = cache.getCache("session1800_sync");
		int num = 0;
		String key = "msg_"+userid;
		Element old = c.get(key); 
		if (old!=null) {
			num = Integer.parseInt(old.getValue().toString());
		}
		Element e = new Element(key,num-1);
		c.put(e); 
	}

	@MethodLogUtil(type = "", value = "删除我的消息")
	public void doDeleteMine(String userid, String messageids) throws IllegalAccessException, InvocationTargetException {
		String[] ids = messageids.split(",");
		List<Map<String,Object>> rs = null;
		MessageStatus ms = null;
		int i = 0;
		for (String id : ids) {
			rs = this.getDAO().queryForList("select * from pubmodule_message_status_tbl where userid=" + userid + " and messageid=" + id);
			if (rs.size() != 0) {
				ms = new MessageStatus();
				Map<String,Object> map=rs.get(0);
				ms.setId(Long.parseLong(map.get("id").toString()));
				ms.setMessageid(Long.parseLong(map.get("messageid").toString()));
				ms.setStatus(0);
				ms.setUserid(Long.parseLong(map.get("userid").toString()));
				ms.setReadtime(new Date());
				this.update(ms);
				i++;
			}
		}
		CacheUtil cache = CacheUtil.getInstance();
		Cache c = cache.getCache("session1800_sync");
		int num = 0;
		String key = "msg_"+userid;
		Element old = c.get(key); 
		if (old!=null) {
			num = Integer.parseInt(old.getValue().toString());
		}
		Element e = new Element(key,num-i);
		c.put(e); 
	}

	@Override
	public Message save(Message msg, Set<String> to) {
		return save(msg,to,false);
//		msg = this.save(msg); 
//		MessageStatus s = new MessageStatus();
//		s.setStatus(1);		
//		s.setMessageid(msg.getId());
//		CacheUtil cache = CacheUtil.getInstance();
//		Cache c = cache.getCache("session1800_sync");
//		
//		for (String userId : to) {
//			s.setUserid(Long.parseLong(userId));
//			this.save(s); 
//			int num = 0;
//			String key = "msg_"+userId;
//			Element old = c.get(key); 
//			if (old!=null) {
//				num = Integer.parseInt(old.getValue().toString());
//			}
//			Element e = new Element(key,num+1);
//			c.put(e);
//		} 
//		return msg;
	}



	@Override
	public Message save(Message msg, Set<String> to, boolean isPushToApp) {
		msg = this.save(msg); 
		MessageStatus s = new MessageStatus();
		s.setStatus(1);		
		s.setMessageid(msg.getId());
		CacheUtil cache = CacheUtil.getInstance();
		Cache c = cache.getCache("session1800_sync");
		List<String> userIds = new ArrayList<String>();
		for (String userId : to) {
			s.setUserid(Long.parseLong(userId));
			this.save(s); 
			int num = 0;
			String key = "msg_"+userId;
			Element old = c.get(key); 
			if (old!=null) {
				num = Integer.parseInt(old.getValue().toString());
			}
			Element e = new Element(key,num+1);
			c.put(e);
			userIds.add(userId);
		} 
		if(isPushToApp) {
			AppPushMessage appPushMessage = new AppPushMessage();
			appPushMessage.setNoticeTitle("站内信");
			appPushMessage.setNoticeText(msg.getTitle());
			appPushMessage.setContent(msg.getMessage());
			try {
				userAppService.pushToUsers(userIds, appPushMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return msg;
	}
}

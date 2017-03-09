package net.hsp.service.sys.msg;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import net.hsp.entity.sys.msg.Message;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

/**
 * 站内信业务接口
 * @author lk0516
 */
public interface MessageService extends BaseService  {
	
	 /**
	 * 多条件查询站内信
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);
	
	/**
	 * 保存站内信
	 * @param msg
	 * @param to
	 * @return
	 */
	public Message save(Message msg,Set<String> to);
	
	/**
	 * 保存站内信及推送APP
	 * @param msg
	 * @param to
	 * @param isPushToApp 是否推送APP
	 * @return
	 */
	public Message save(Message msg,Set<String> to,boolean isPushToApp);
	
	public Map<String,Object> query(String userid,Map param, PageInfo p);
	
	/**
	 * 根据用户ID查询未读数
	 * @param userid
	 * @return
	 */
	public Object unread(String userid);
	
	public void doDeleteMine(String userid, String messageids) throws IllegalAccessException, InvocationTargetException;
	
	public void readSign(String userid, String messageid);
	 
}
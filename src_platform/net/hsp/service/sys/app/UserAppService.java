package net.hsp.service.sys.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.app.UserApp;
import net.hsp.service.BaseService;
import net.hsp.service.ServiceException;
import net.hsp.web.util.PageInfo;

/**
 * 用户手机端关联业务接口
 * 
 * @author nd0100
 */
public interface UserAppService extends BaseService {

	/**
	 * 多条件查询用户手机端关联
	 * 
	 * @param param
	 *            页面条件 可为空
	 * @param p
	 *            分页参数 可为空
	 * @return rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String, Object> query(Map param, PageInfo p);

	public UserApp saveOrUpdate(UserApp entity) throws ServiceException;
	
	/**
	 * 通过用户id获取手机端id
	 * @param userIds
	 * @return
	 * @throws ServiceException
	 */
	public List<String> getClientIdsByUser(List<String> userIds) throws ServiceException;
	
	/**
	 * 推送消息给所有用户
	 * @param appMsg
	 * @return
	 * @throws IOException
	 */
	public String pushToAll(AppPushMessage appMsg) throws IOException;
	
	/**
	 * 推送消息给指定用户
	 * @param userIds
	 * @param appMsg
	 * @return
	 * @throws IOException
	 */
	public String pushToUsers(List<String> userIds,AppPushMessage appMsg) throws IOException;

}
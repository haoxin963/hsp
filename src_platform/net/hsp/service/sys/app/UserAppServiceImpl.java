package net.hsp.service.sys.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.entity.sys.app.UserApp;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.ServiceException;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.HttpSessionFactory;
import net.hsp.web.util.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 用户手机端关联业务类
 * 
 * @author nd0100
 */
@Service
@ServiceLogUtil(name = "用户手机端关联业务类")
@Lazy(true)
public class UserAppServiceImpl extends BaseServiceImpl implements UserAppService {

	@MethodLogUtil(type = "1", value = "多条件查询用户手机端关联")
	public Map<String, Object> query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select userid,status,custid, id   from pubmodule_user_app_tbl where 1=1");
		Map sqlBuild = SQLBuild.buildLike(param);
		sb.append(sqlBuild.get(SQLBuild.SQL));
		return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get(SQLBuild.ARGS), p);
	}

	@Override
	@MethodLogUtil(type = "1", value = "保存或更新")
	public UserApp saveOrUpdate(UserApp entity) throws ServiceException {
		String clientid = entity.getClientid();
		System.out.println("clientid=>"+clientid);
		if (StringUtils.isBlank(clientid))
			return entity;
		if (isExist(clientid)) {
			updateByClientId(entity);
			return entity;
		} else {
			String custId = HttpSessionFactory.getCustId();
			entity.setCustid(custId);
			entity.setCreatedate(new Date());
			entity.setStatus("0");
			return save(entity);
		}
	}
	
	private void updateByClientId(UserApp entity){
		String sql = "update pubmodule_user_app_tbl set userid = " + entity.getUserid() + " where clientid = '" + entity.getClientid() + "'";
		this.getDAO().execute(sql);	
	}

	/**
	 * 判断clientid是否已存在
	 * 
	 * @param clientid
	 * @return
	 * @throws ServiceException
	 */
	private boolean isExist(String clientid) throws ServiceException {
		String sql = "select id from pubmodule_user_app_tbl where 1=1 and clientid =?";
		List<UserApp> list = this.find(sql, new String[] { clientid }, UserApp.class);
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> getClientIdsByUser(List<String> userIds) throws ServiceException {
		List<String> userList = new ArrayList<String>();
		if (userIds == null || userIds.size() == 0)
			return userList;
		String sql = "select clientid, id from pubmodule_user_app_tbl where 1=1 and userid in (" + StringUtils.join(userIds, ",") + ")";
		List<UserApp> list = this.find(sql, new String[] {}, UserApp.class);
		if (list != null && list.size() > 0) {
			for (UserApp userApp : list) {
				userList.add(userApp.getClientid());
			}
		}
		return userList;
	}

	public String pushToAll(AppPushMessage appMsg) throws IOException {
		String custId = HttpSessionFactory.getCustId();
		return PushMessageToApp.getInstance().pushToApp(appMsg, custId);
	}

	public String pushToUsers(List<String> userIds, AppPushMessage appMsg) throws IOException {
		String custId = HttpSessionFactory.getCustId();
		if (userIds == null || userIds.size() == 0) {
			return "用户不能为空！";
		}
		List<String> cids = getClientIdsByUser(userIds);
		if (cids.size() == 1) {
			return PushMessageToApp.getInstance().pushToSingle(cids.get(0), appMsg, custId);
		} else if (cids.size() > 1) {
			return PushMessageToApp.getInstance().pushToList(cids, appMsg, custId);
		}
		return null;
	}
}

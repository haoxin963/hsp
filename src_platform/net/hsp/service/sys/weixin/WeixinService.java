
package net.hsp.service.sys.weixin;

import java.util.Map;

import net.hsp.entity.sys.rbac.User;
import net.hsp.entity.sys.weixin.WeixinApp;
import net.hsp.service.BaseService;
import net.hsp.web.util.PageInfo;

public interface WeixinService extends BaseService {
	
	 /**
	 * 多条件查询微信
	 * @param param 页面条件 可为空
	 * @param p  分页参数 可为空
	 * @return  rows:数据集合 list< Map >,total:总条数
	 */
	public Map<String,Object> query(Map param,PageInfo p);

	
	/**
	 * 创建用户和微信openId关系
	 * @param appId
	 * @param openId
	 * @param userId
	 * @param userName
	 * @param password
	 * @param userType
	 * @return
	 */
	public boolean createMapping(String appId, String openId, String userId, String userName, String password, String userType);
	
	/**
	 * 删除用户和微信openId关系
	 * @param openId
	 * @return
	 */
	public boolean deleteMapping(String openId);
	
	/**
	 * 检查username和openid是否匹配
	 * @param userName
	 * @param openId
	 * @return
	 */
	public boolean checkMapping(String userName,String openId);
	
	/**
	 * 更新某个openId的位置信息
	 * @param openId
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean updateGPS(String openId,String x,String y);
	
	/**
	 * 根据userId查询对应openId
	 * @param userId
	 * @return
	 */
	public String getOpenIdByUserId(String userId);
	
	/**
	 * 根据openId查询对应userId
	 * @param openId
	 * @return
	 */
	public String getUserIdByOpenId(String openId);
	
	
	/**
	 * 根据custId找配置
	 * @param custId
	 * @return
	 */
	public WeixinApp getWeixinAppByCustId(String custId);
	
	/**
	 * 检验微信注册用户是否已注册过
	 * @param appId
	 * @param userId
	 * @return
	 */
	public boolean chkUserIsExist(String appId, String userId);
	
	/**
	 * 根据userId获取用户登录名和用户密码
	 * @param userId
	 */
	public User getUserByUserId(String userId);
	
	/**
	 * 根据userId获取用户登录名和用户密码
	 * @param userId
	 */
	public boolean modifyUserPwd(String userId,String pwd);
}

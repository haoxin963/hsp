package net.hsp.service.sys.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hsp.common.PropConfigFactory;
import net.hsp.common.constants.PlatFormConstant;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * APP推送消息
 * 
 * @author nd0100
 * 
 */
public class PushMessageToApp extends PropConfigFactory {
	private Logger logger = LogManager.getLogger(PushMessageToApp.class);
	private Map<String, String> config = new HashMap<String, String>();
	private String url = "http://sdk.open.api.igexin.com/apiex.htm";   

	private static boolean isOffline = true;
	private static long offlineExpireTime = (long) (24 * 3600 * 1000);
	private static int transmissionType = 2;// 1为立即启动，2则广播等待客户端自启动

	private final static PushMessageToApp pushMessage = new PushMessageToApp();

	private PushMessageToApp() {
	}

	public static PushMessageToApp getInstance() {
		return pushMessage;
	}

	/**
	 * 推送消息到所有用户
	 * 
	 * @param appMsg
	 * @return
	 * @throws IOException
	 */
	public String pushToApp(AppPushMessage appMsg, String custId) throws IOException {
		if(getAppKey(custId) == null || getMasterSecret(custId) == null || getAppId(custId) == null) {
			return custId+"配置文件有错误";
		}
		IGtPush push = new IGtPush(url, getAppKey(custId), getMasterSecret(custId));
		push.connect();
		AppMessage message = new AppMessage();
		TransmissionTemplate template = getTransmissionTemplate(appMsg, custId);
		List<String> appIds = new ArrayList<String>();
		appIds.add(getAppId(custId));
		message.setData(template);
		message.setAppIdList(appIds);
		message.setOffline(isOffline);
		message.setOfflineExpireTime(offlineExpireTime);
		IPushResult ret = push.pushMessageToApp(message);
		logger.info("custId =>"+custId+",推送响应=>"+ret);
		return ret.getResponse().toString();
	}

	/**
	 * 推送消息到单个用户
	 * 
	 * @param cid
	 * @param appMsg
	 * @return
	 * @throws IOException
	 */
	public String pushToSingle(String cid, AppPushMessage appMsg, String custId) throws IOException {
		if(getAppKey(custId) == null || getMasterSecret(custId) == null || getAppId(custId) == null) {
			return custId+"配置文件有错误";
		}
		IGtPush push = new IGtPush(url, getAppKey(custId), getMasterSecret(custId));
		push.connect();
		TransmissionTemplate template = getTransmissionTemplate(appMsg, custId);
		SingleMessage message = new SingleMessage();
		message.setOffline(isOffline);
		message.setOfflineExpireTime(offlineExpireTime);
		message.setData(template);
		Target target = new Target();
		target.setAppId(getAppId(custId));
		target.setClientId(cid);
		IPushResult ret = push.pushMessageToSingle(message, target);
		logger.info("custId =>"+custId+",client=>"+cid+",推送响应=>"+ret);
		return ret.getResponse().toString();
	}

	/**
	 * 推送消息到多个用户
	 * 
	 * @param cids
	 * @param appMsg
	 * @return
	 * @throws IOException
	 */
	public String pushToList(List<String> cids, AppPushMessage appMsg, String custId) throws IOException {
		if(getAppKey(custId) == null || getMasterSecret(custId) == null || getAppId(custId) == null) {
			return custId+"配置文件有错误";
		}
		IGtPush push = new IGtPush(url, getAppKey(custId), getMasterSecret(custId));
		push.connect();
		TransmissionTemplate template = getTransmissionTemplate(appMsg, custId);
		ListMessage message = new ListMessage();
		message.setData(template);
		message.setOffline(isOffline);
		message.setOfflineExpireTime(offlineExpireTime);
		List<Target> targets = new ArrayList<Target>();
		for (String cid : cids) {
			Target target = new Target();
			target.setAppId(getAppId(custId));
			target.setClientId(cid);
			targets.add(target);
		}
		String taskId = push.getContentId(message);
		IPushResult ret = push.pushMessageToList(taskId, targets);
		logger.info("custId =>"+custId+",clients=>"+cids.toString()+",推送响应=>"+ret);
		return ret.getResponse().toString();
	}
	
	/**
	 * 获取通知模版
	 * @param appMsg
	 * @param custId
	 * @return
	 */
	public TransmissionTemplate getTransmissionTemplate(AppPushMessage appMsg, String custId) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(getAppId(custId));
		template.setAppkey(getAppKey(custId));
		template.setTransmissionType(transmissionType);
		JSONObject jsonObject = JSONObject.fromObject(appMsg);
		System.out.println("通知内容 : "+jsonObject);
		template.setTransmissionContent(jsonObject.toString());
		return template;
	}

	@Override
	protected String getFile() {
		return PlatFormConstant.CONFIGFILEPATH + "/appmessage-conf.properties";
	}

	public String getAppId(String custId) {
		String key = custId + ".appId";
		if (config.get(key) == null) {
			String appId = getProperty(key);
			if (StringUtils.isNotBlank(appId)) {
				config.put(key, appId);
				return appId;
			}
			logger.error("appKey is appId");
			return null;
		} else {
			return config.get(key);
		}
	}

	public String getAppKey(String custId) {
		String key = custId + ".appKey";
		if (config.get(key) == null) {
			String appKey = getProperty(key);
			if (StringUtils.isNotBlank(appKey)) {
				config.put(key, appKey);
				return appKey;
			}
			logger.error("appKey is null");
			return null;
		} else {
			return config.get(key);
		}
	}

	public String getMasterSecret(String custId) {
		String key = custId + ".masterSecret";
		if (config.get(key) == null) {
			String masterSecret = getProperty(key);
			if (StringUtils.isNotBlank(masterSecret)) {
				config.put(key, masterSecret);
				return masterSecret;
			}
			logger.error("masterSecret is null");
			return null;
		} else {
			return config.get(key);
		}
	}

	public static void main(String[] args) {
		AppPushMessage appMsg = new AppPushMessage();
		appMsg.setNoticeTitle("test");
		appMsg.setNoticeText("我的测试！");
		appMsg.setContent("测试");
		JSONObject jsonObject = JSONObject.fromObject(appMsg);
		System.out.println(jsonObject);
		try {
			PushMessageToApp.getInstance().pushToSingle("c57f72dabb0fe54dd182e80f4f1d224a", appMsg, "uisp");
			// PushMessageToApp.getInstance().pushToApp(appMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

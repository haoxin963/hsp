package net.hsp.service.sys.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.hsp.common.weixin.api.MessageAPI;
import net.hsp.common.weixin.api.TokenAPI;
import net.hsp.common.weixin.bean.BaseResult;
import net.hsp.common.weixin.bean.Token;
import net.hsp.common.weixin.bean.message.Message;
import net.hsp.common.weixin.bean.templatemessage.TemplateMessage;
import net.hsp.common.weixin.bean.templatemessage.TemplateMessageResult;
import net.hsp.entity.sys.weixin.WeixinApp;
import net.hsp.web.util.SpringCtx;

import org.apache.commons.lang.StringUtils;

public class WeixinUtil {
	private static WeixinService weixinService;

	private static Map tokens = new Hashtable();

	/**
	 * 按站点发送模板消息
	 * 
	 * @param custId
	 * @param template
	 * @return
	 */
	public static TemplateMessageResult messageTemplateSend(String custId, TemplateMessage template) {
		MessageAPI api = new MessageAPI();
		return api.messageTemplateSend(getToken(custId), template);
	}

	/**
	 * 按站点发送客服消息
	 * 
	 * @param custId
	 * @param message
	 * @return
	 */
	public static BaseResult messageCustomSend(String custId, Message message) {
		MessageAPI api = new MessageAPI();
		return api.messageCustomSend(getToken(custId), message);
	}

	/**
	 * 取得当前站点公众号token 会缓存一定时间(3600秒)
	 * 
	 * @param custId
	 * @return
	 */
	public static String getToken(String custId) {
		String token = (String) tokens.get(custId + "token");
		try {
			Date dt = (Date) tokens.get(custId + "dt");
			if (dt == null || System.currentTimeMillis() - dt.getTime() > 3600 * 1000) {
				WeixinApp app = getAppCfg(custId);
				token = new TokenAPI().token(app.getAppId(), app.getSecret()).getAccess_token();
				dt = new Date();
				tokens.put(custId + "token", token);
				tokens.put(custId + "dt", dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	/**
	 * 根据openId返回对应的userId
	 * 
	 * @param openId
	 * @return
	 */
	public static String getUserIdByOpenId(String openId) {
		if (weixinService == null) {
			weixinService = (WeixinService) SpringCtx.getBean("weixinServiceImpl");
		}
		return weixinService.getUserIdByOpenId(openId);
	}

	/**
	 * 根据userId返回对应的openId
	 * 
	 * @param openId
	 * @return
	 */
	public static String getOpenIdByUserId(String userId) {
		if (weixinService == null) {
			weixinService = (WeixinService) SpringCtx.getBean("weixinServiceImpl");
		}
		return weixinService.getOpenIdByUserId(userId);
	}

	/**
	 * 根据站点Id返回对应的微信appId及Secret
	 * 
	 * @param custId
	 * @return
	 */
	public static WeixinApp getAppCfg(String custId) {
		if (weixinService == null) {
			weixinService = (WeixinService) SpringCtx.getBean("weixinServiceImpl");
		}
		WeixinApp app = weixinService.getWeixinAppByCustId(custId);
		return app;
	}

	/**
	 * 根据oauth回调的code获取openId
	 * 
	 * @param custId
	 * @param code
	 * @return
	 */
	public static String getOpenIdByCode(String custId, String code) {
		WeixinApp appId = getAppCfg(custId);
		Token token = new TokenAPI().token(appId.getAppId(), appId.getSecret(), code);
		return token.getOpenid();
	}

	/**
	 * 验证服务器地址的有效性
	 * 
	 * @param signature
	 *            微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @param token
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce, String token) {
		boolean flag = false;
		if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce) || StringUtils.isBlank(token)) {
			return false;
		}
		List<String> list = new ArrayList<String>();
		list.add(nonce);
		list.add(timestamp);
		list.add(token);
		Collections.sort(list);
		String sha1String = "";
		try {
			sha1String = sha1(list.get(0) + list.get(1) + list.get(2));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (signature.equals(sha1String)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * sha1加密
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha1(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(data.getBytes());
		StringBuffer buf = new StringBuffer();
		byte[] bits = md.digest();
		for (int i = 0; i < bits.length; i++) {
			int a = bits[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(checkSignature("fceb3a48a72fa546ca4999ccd71feec540fbdef9", "1470125188", "576238365", "11"));
	}

}

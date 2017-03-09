package net.hsp.common.weixin.util;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.hsp.common.weixin.api.PayMchAPI;
import net.hsp.common.weixin.bean.pay.PayJsRequest;
import net.hsp.common.weixin.bean.pay.PayNativeReply;
import net.hsp.common.weixin.bean.pay.PayNativeRequest;
import net.hsp.common.weixin.bean.pay.PayPackage;
import net.hsp.common.weixin.bean.paymch.MchPayNativeReply;
import net.hsp.common.weixin.bean.paymch.Unifiedorder;
import net.hsp.common.weixin.bean.paymch.UnifiedorderResult;
import net.hsp.web.util.ActionUtil;


public class PayUtil {


	/**
	 * 生成支付JS请求JSON
	 * @param payPackage
	 * @param appId
	 * @param paternerKey
	 * @param paySignkey	appkey
	 * @return
	 */
	public static String generatePayJsRequestJson(PayPackage payPackage,
				String appId,
				String paternerKey,
				String paySignkey){
		Map<String, String> mapP = MapUtil.objectToMap(payPackage);
		String package_ = SignatureUtil.generatePackage(mapP, paternerKey);
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(UUID.randomUUID().toString());
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType("sha1");
		payJsRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest,"signType","paySign");
		String paySign = SignatureUtil.generatePaySign(mapS,paySignkey);
		payJsRequest.setPaySign(paySign);
		return JsonUtil.toJSONString(payJsRequest);
	}



	/**
	 * 生成Native支付JS请求URL
	 * @param appid
	 * @param productid
	 * @param paySignkey
	 * @return
	 */
	public static String generatePayNativeRequestURL(
			String appid,
			String productid,
			String paySignkey){

		PayNativeRequest payNativeRequest = new PayNativeRequest();
		payNativeRequest.setAppid(appid);
		payNativeRequest.setNoncestr(UUID.randomUUID().toString());
		payNativeRequest.setProductid(productid);
		payNativeRequest.setTimestamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payNativeRequest,"sign");
		String sign = SignatureUtil.generatePaySign(mapS,paySignkey);
		payNativeRequest.setSign(sign);

		Map<String, String> map = MapUtil.objectToMap(payNativeRequest);
		return "weixin://wxpay/bizpayurl?" + MapUtil.mapJoin(map, false, false);
	}

	/**
	 * 生成 native 支付回复XML
	 * @param payPackage
	 * @param appId
	 * @param retCode 0 正确
	 * @param retErrMsg
	 * @param paternerKey
	 * @return
	 */
	public static String generatePayNativeReplyXML(PayPackage payPackage,
			String appId,
			String retCode,
			String retErrMsg,
			String paternerKey){

		PayNativeReply payNativeReply = new PayNativeReply();
		payNativeReply.setAppid(appId);
		payNativeReply.setNoncestr(UUID.randomUUID().toString());
		payNativeReply.setRetcode(retCode);
		payNativeReply.setReterrmsg(retErrMsg);
		payNativeReply.setTimestamp(System.currentTimeMillis()+"");
		String package_ = SignatureUtil.generatePackage(MapUtil.objectToMap(payPackage),paternerKey);
		payNativeReply.setPackage_(package_);
		payNativeReply.setSignmethod("sha1");
		String appSignature = SignatureUtil.generatePackage(
									MapUtil.objectToMap(payNativeReply,"appsignature","signmethod"),
									paternerKey);
		payNativeReply.setAppsignature(appSignature);

		return XMLConverUtil.convertToXML(payNativeReply);
	}









	//MCH -------------------------------------------------


	/**
	 * (MCH)生成支付JS请求对象
	 * @param prepay_id	预支付订单号
	 * @param appId
	 * @param key 商户支付密钥
	 * @return
	 */
	public static String generateMchPayJsRequestJson(String prepay_id,String appId,String key){
		String package_ = "prepay_id="+prepay_id;
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(UUID.randomUUID().toString());
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType("MD5");
		payJsRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest,"signType","paySign");
		String paySign = SignatureUtil.generateSign(mapS,key);
		payJsRequest.setPaySign(paySign);
		return JsonUtil.toJSONString(payJsRequest);
	}


	/**
	 * (MCH)生成Native支付JS请求URL
	 * @param appid
	 * @param mch_id
	 * @param productid
	 * @param key
	 * @return
	 */
	public static String generateMchPayNativeRequestURL(
			String appid,
			String mch_id,
			String productid,
			String key){

		PayNativeRequest payNativeRequest = new PayNativeRequest();
		payNativeRequest.setAppid(appid);
		payNativeRequest.setNoncestr(UUID.randomUUID().toString());
		payNativeRequest.setProductid(productid);
		payNativeRequest.setTimestamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payNativeRequest,"sign");
		mapS.put("mch_id",mch_id);
		String sign = SignatureUtil.generatePaySign(mapS,key);
		payNativeRequest.setSign(sign);

		Map<String, String> map = MapUtil.objectToMap(payNativeRequest);
		return "weixin://wxpay/bizpayurl?" + MapUtil.mapJoin(map, false, false);
	}

	/**
	 * (MCH)生成 native 支付回复XML
	 * @param mchPayNativeReply
	 * @param key
	 * @return
	 */
	public static String generateMchPayNativeReplyXML(MchPayNativeReply mchPayNativeReply,String key){
		Map<String, String> map = MapUtil.objectToMap(mchPayNativeReply);
		String sign = SignatureUtil.generateSign(map,key);
		mchPayNativeReply.setSign(sign);
		return XMLConverUtil.convertToXML(mchPayNativeReply);
	}
	
	/**
	 * 获取JSAPI请求参数
	 * @param openId 用户标识
	 * @param appId 公众账号ID
	 * @param mch_id 商户号
	 * @param key 商户密钥
	 * @param returnAction 回调url
	 * @param orderBody 订单内容
	 * @param orderNo 订单号
	 * @param orderPrice 订单价格
	 * @return JSAPI请求参数
	 */
	public static PayJsRequest generateMchPayJsRequest(String openId,String appId,String mch_id,String key,String returnAction,String orderBody,String orderNo,String orderPrice){
		Unifiedorder unifiedorder = new Unifiedorder();
		unifiedorder.setAppid(appId);
		unifiedorder.setMch_id(mch_id);
		unifiedorder.setNonce_str(UUID.randomUUID().toString());
		unifiedorder.setBody(orderBody);
		unifiedorder.setOut_trade_no(orderNo);
		unifiedorder.setTotal_fee(orderPrice);
		HttpServletRequest request = ActionUtil.getCtx().getRequest();
		String ip = request.getRemoteAddr();
		unifiedorder.setSpbill_create_ip(ip);
		StringBuffer url = request.getRequestURL();
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
		String notify_url = tempContextUrl + returnAction;
		unifiedorder.setNotify_url(notify_url);
		unifiedorder.setTrade_type("JSAPI"); // 交易类型
		unifiedorder.setOpenid(openId);
		String wxpackage = SignatureUtil.generatePackage(MapUtil.objectToMap(unifiedorder,"sign"), key);
		unifiedorder.setSign(wxpackage);
		UnifiedorderResult unifiedorderResult = new PayMchAPI().payUnifiedorder(unifiedorder);	
		String package_ = "prepay_id="+unifiedorderResult.getPrepay_id();
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(UUID.randomUUID().toString());
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType("MD5");
		payJsRequest.setTimeStamp(System.currentTimeMillis()/1000+"");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest,"signType","paySign");
		String paySign = SignatureUtil.generateSign(mapS,key);
		payJsRequest.setPaySign(paySign);
		return payJsRequest;
	}
	
}
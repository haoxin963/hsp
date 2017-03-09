package net.hsp.common.weixin.api;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import net.hsp.common.weixin.bean.paymch.Closeorder;
import net.hsp.common.weixin.bean.paymch.MchBaseResult;
import net.hsp.common.weixin.bean.paymch.MchShorturl;
import net.hsp.common.weixin.bean.paymch.MchShorturlResult;
import net.hsp.common.weixin.bean.paymch.Refundquery;
import net.hsp.common.weixin.bean.paymch.RefundqueryResult;
import net.hsp.common.weixin.bean.paymch.SecapiPayRefund;
import net.hsp.common.weixin.bean.paymch.SecapiPayRefundResult;
import net.hsp.common.weixin.bean.paymch.Unifiedorder;
import net.hsp.common.weixin.bean.paymch.UnifiedorderResult;
import net.hsp.common.weixin.client.XmlResponseHandler;
import net.hsp.common.weixin.util.MapUtil;
import net.hsp.common.weixin.util.SignatureUtil;
import net.hsp.common.weixin.util.XMLConverUtil;


/**
 * 微信支付 基于V3.X 版本 
 *
 */
public class PayMchAPI extends BaseAPI{


	/**
	 * 统一支付接口
	 * @param unifiedorder
	 * @return
	 */
	public UnifiedorderResult payUnifiedorder(Unifiedorder unifiedorder){
		String unifiedorderXML = XMLConverUtil.convertToXML(unifiedorder);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(xmlHeader)
										.setUri(MCH_URI + "/pay/unifiedorder")
										.setEntity(new StringEntity(unifiedorderXML,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,XmlResponseHandler.createResponseHandler(UnifiedorderResult.class));
	}



	/**
	 * 关闭订单
	 * @param closeorder
	 * @param key 商户支付密钥
	 * @return
	 */
	public MchBaseResult payCloseorder(Closeorder closeorder,String key){
		Map<String,String> map = MapUtil.objectToMap(closeorder);
		String sign = SignatureUtil.generateSign(map,key);
		closeorder.setSign(sign);
		String closeorderXML = XMLConverUtil.convertToXML(closeorder);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/closeorder")
				.setEntity(new StringEntity(closeorderXML,Charset.forName("utf-8")))
				.build();
		return localHttpClient.execute(httpUriRequest,XmlResponseHandler.createResponseHandler(MchBaseResult.class));
	}


	/**
	 * 退款申请
	 *
	 * 注意：
	 *	1.交易时间超过1 年的订单无法提交退款；
	 *	2.支持部分退款，部分退需要设置相同的订单号和不同的out_refund_no。一笔退款失
	 *	败后重新提交，要采用原来的out_refund_no。总退款金额不能超过用户实际支付金额。
	 * @param secapiPayRefund
	 * @param key 商户支付密钥
	 * @return
	 */
	public SecapiPayRefundResult secapiPayRefund(SecapiPayRefund secapiPayRefund,String key){
		Map<String,String> map = MapUtil.objectToMap( secapiPayRefund);
		String sign = SignatureUtil.generateSign(map,key);
		secapiPayRefund.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( secapiPayRefund);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/secapi/pay/refund")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return localHttpClient.execute(httpUriRequest,XmlResponseHandler.createResponseHandler(SecapiPayRefundResult.class));
	}

	/**
	 * 退款查询
	 *
	 * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款
	 * 20 分钟内到账，银行卡支付的退款3 个工作日后重新查询退款状态。
	 * @param refundquery
	 * @param key 商户支付密钥
	 * @return
	 */
	public RefundqueryResult payRefundquery(Refundquery refundquery,String key){
		Map<String,String> map = MapUtil.objectToMap(refundquery);
		String sign = SignatureUtil.generateSign(map,key);
		refundquery.setSign(sign);
		String refundqueryXML = XMLConverUtil.convertToXML(refundquery);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/refundqueryd")
				.setEntity(new StringEntity(refundqueryXML,Charset.forName("utf-8")))
				.build();
		return localHttpClient.execute(httpUriRequest,XmlResponseHandler.createResponseHandler(RefundqueryResult.class));
	}

	/**
	 * 短链接转换
	 * @param shorturl
	 * @param key 商户支付密钥
	 * @return
	 */
	public MchShorturlResult toolsShorturl(MchShorturl shorturl,String key){
		Map<String,String> map = MapUtil.objectToMap(shorturl);
		String sign = SignatureUtil.generateSign(map,key);
		shorturl.setSign(sign);
		String shorturlXML = XMLConverUtil.convertToXML(shorturl);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/tools/shorturl")
				.setEntity(new StringEntity(shorturlXML,Charset.forName("utf-8")))
				.build();
		return localHttpClient.execute(httpUriRequest,XmlResponseHandler.createResponseHandler(MchShorturlResult.class));
	}

}

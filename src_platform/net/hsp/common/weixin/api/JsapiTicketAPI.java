package net.hsp.common.weixin.api;

import net.hsp.common.weixin.bean.JsapiTicket;
import net.hsp.common.weixin.client.JsonResponseHandler;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

public class JsapiTicketAPI extends BaseAPI {
	
	/**
	 * 获取微信JS接口的临时票据
	 * @param access_token 基本token
	 * @return
	 */
	public JsapiTicket getTicket(String access_token){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
	      .setUri(BASE_URI + "/cgi-bin/ticket/getticket")
	      .addParameter("access_token", access_token)
	      .addParameter("type", "jsapi")
	      .build();
	    return (JsapiTicket)this.localHttpClient.execute(httpUriRequest, JsonResponseHandler.createResponseHandler(JsapiTicket.class));
	}
}

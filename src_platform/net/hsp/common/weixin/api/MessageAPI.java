package net.hsp.common.weixin.api;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

import net.hsp.common.weixin.bean.Article;
import net.hsp.common.weixin.bean.BaseResult;
import net.hsp.common.weixin.bean.Media;
import net.hsp.common.weixin.bean.MessageSendResult;
import net.hsp.common.weixin.bean.Uploadvideo;
import net.hsp.common.weixin.bean.massmessage.MassMessage;
import net.hsp.common.weixin.bean.message.Message;
import net.hsp.common.weixin.bean.templatemessage.TemplateMessage;
import net.hsp.common.weixin.bean.templatemessage.TemplateMessageItem;
import net.hsp.common.weixin.bean.templatemessage.TemplateMessageResult;
import net.hsp.common.weixin.client.JsonResponseHandler;
import net.hsp.common.weixin.util.JsonUtil;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;


/**
 * 当用户主动发消息给公众号的时候
 * （包括发送信息、点击自定义菜单click事件、订阅事件、扫描二维码事件、支付成功事件、用户维权），
 * 微信将会把消息数据推送给开发者，
 * 开发者在一段时间内（目前修改为48小时）可以调用客服消息接口，
 * 通过POST一个JSON数据包来发送消息给普通用户，
 * 在48小时内不限制发送次数。
 * 此接口主要用于客服等有人工消息处理环节的功能，方便开发者为用户提供更加优质的服务。
 * @author popular
 *
 */
public class MessageAPI extends BaseAPI{


	/**
	 * 消息发送
	 * @param access_token
	 * @param messageJson
	 * @return
	 */
	public BaseResult messageCustomSend(String access_token,String messageJson){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/message/custom/send")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(BaseResult.class));
	}

	/**
	 * 消息发送
	 * @param access_token
	 * @param message
	 * @return
	 */
	public BaseResult messageCustomSend(String access_token,Message message){
		String str = JsonUtil.toJSONString(message);
		return messageCustomSend(access_token,str);
	}

	/**
	 * 高级群发 构成 MassMPnewsMessage 对象的前置请求接口
	 * @param access_token
	 * @param articles 图文信息 1-10 个
	 * @return
	 */
	public Media mediaUploadnews(String access_token,List<Article> articles){
		String str = JsonUtil.toJSONString(articles);
		String messageJson = "{\"articles\":"+str+"}";
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/media/uploadnews")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(Media.class));
	}

	/**
	 * 高级群发 构成 MassMPvideoMessage 对象的前置请求接口
	 * @param access_token
	 * @param uploadvideo
	 * @return
	 */
	public Media mediaUploadvideo(String access_token,Uploadvideo uploadvideo){
		String messageJson = JsonUtil.toJSONString(uploadvideo);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(MEDIA_URI+"/cgi-bin/media/uploadvideo")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(Media.class));
	}


	/**
	 * 高级群发接口 根据分组进行群发
	 * @param access_token
	 * @param messageJson
	 * @return
	 */
	public MessageSendResult messageMassSendall(String access_token,String messageJson){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/message/mass/sendall")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(MessageSendResult.class));
	}

	/**
	 * 高级群发接口 根据分组进行群发
	 * @param access_token
	 * @param massMessage
	 * @return
	 */
	public MessageSendResult messageMassSendall(String access_token,MassMessage massMessage){
		String str = JsonUtil.toJSONString(massMessage);
		return messageMassSendall(access_token,str);
	}


	/**
	 * 高级群发接口 根据OpenID列表群发
	 * @param access_token
	 * @param messageJson
	 * @return
	 */
	public MessageSendResult messageMassSend(String access_token,String messageJson){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/message/mass/send")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(MessageSendResult.class));
	}

	/**
	 * 高级群发接口 根据OpenID列表群发
	 * @param access_token
	 * @param massMessage
	 * @return
	 */
	public MessageSendResult messageMassSend(String access_token,MassMessage massMessage){
		String str = JsonUtil.toJSONString(massMessage);
		return messageMassSend(access_token,str);
	}


	/**
	 * 高级群发接口	删除群发
	 * 请注意，只有已经发送成功的消息才能删除删除消息只是将消息的图文详情页失效，
	 * 已经收到的用户，还是能在其本地看到消息卡片。
	 * 另外，删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
	 * @param access_token
	 * @param msgid
	 * @return
	 */
	public BaseResult messageMassDelete(String access_token,String msgid){
		String messageJson = "{\"msgid\":" + msgid + "}";
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/message/mass/delete")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(BaseResult.class));
	}




	/**
	 * 模板消息发送
	 * @param access_token
	 * @param templateMessage
	 * @return
	 */
	public TemplateMessageResult messageTemplateSend(String access_token,TemplateMessage templateMessage){
		String messageJson = JsonUtil.toJSONString(templateMessage);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(jsonHeader)
				.setUri(BASE_URI+"/cgi-bin/message/template/send")
				.addParameter("access_token", access_token)
				.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
				.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(TemplateMessageResult.class));
	}

	
	public static void main(String[] args) {
 
		String openId = "oL_DVs5e_MOTEvZw-dPhpmCuIQqQ";
		MessageAPI api = new MessageAPI();
		LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<String, TemplateMessageItem>();
		data.put("first", new TemplateMessageItem("您好!", "#ff0000"));
		data.put("keyword1", new TemplateMessageItem("a", "#173177"));
		data.put("keyword2", new TemplateMessageItem("已办", "#173177"));
		data.put("remark", new TemplateMessageItem("dd", "#173177"));
		TemplateMessage template = new TemplateMessage();
		template.setData(data);
		template.setTemplate_id("NfyPxacw6gfnQ9Fs63d4OfhS3tdO78W3YWRojIXaPCk");
		// template.setUrl("http://hk.pnrtec.com/weixin/first/home.jsp?code=021a677e1d471f6653f62e448a70370-&state=");
		template.setTouser(openId);
		// "oMBHujkhO0uIg5uzRZmEaYxQ1e58");// jsmart
		// 王宁oMBHujtn0S3ZSAo10jKBxJfssoHc
		// 尤oMBHujotqFln6UmeTEV5KSP_9jMg已经得到openId:
		String token = new TokenAPI().token("wx4db2c6f0ac7db6b5", "7dcd294188f8d125113edd089a730bfc").getAccess_token();
		api.messageTemplateSend(token, template);
	}
}

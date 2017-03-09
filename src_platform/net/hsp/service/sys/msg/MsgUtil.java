package net.hsp.service.sys.msg;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.hsp.common.Log;
import net.hsp.dao.DynamicDataSource;
import net.hsp.web.util.HttpSessionFactory;

public class MsgUtil {
	
	/**
	 * 指定站点
	 * @param custId
	 * @param body
	 */
	public static void sendMsg(String custId,Set<String> userIds,String body) {
		send(custId,body);
	}
	
	public static void sendMsg(String custId,String userId,String body) {
		send(custId,body);
	}
	
	public static void sendMsg(Set<String> userIds,String body) {
		send(null,body);
	}
	
	public static void sendMsg(String userId,String body) {
		send(null,body);
	}
	
	public static void sendMsgToAll(String custId,String body) {
		send(null,body);
	}
	
	
	private static void send(String custId,String body){
		try {
			if(custId==null){
				custId = DynamicDataSource.getCustId();
			}
			String baseURL = getPushServer(custId);
			if (baseURL==null) {
				Log.error("当前站点["+custId+"]没有配置消息服务!");
				return;
			}
			String url = baseURL + "/export/msg?cmd=broadcast&title=title";
			Map<String,String> data = new HashMap<String,String>();
			data.put("content", body); 
			HttpClientUtil.post(url,data); 
		} catch (Exception e) {
			Log.error(e);
		}
	}

	public static String getPushServer(String custId) {
		Map station = HttpSessionFactory.getStation(custId);
		if (station!=null) {
			String address = (String)station.get("msgServerAddr");
			if (address!=null) {
				return "http://"+address.split(":")[0]+":9090/plugins";
			}
		}
		return null;
	}

}

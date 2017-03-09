package net.hsp.common.weixin.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientNew {
	static CloseableHttpClient httpclient = HttpClients.createDefault();
	public static String post(String url, List<NameValuePair> nameValuePairs) throws IOException { 
		HttpPost httppost = new HttpPost(url);
		UrlEncodedFormEntity uefEntity;
		if(nameValuePairs!=null){
			uefEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			httppost.setEntity(uefEntity);
		} 
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		return entity != null ? EntityUtils.toString(entity) : "";
	}

}

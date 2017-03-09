package net.hsp.common.weixin.bean.xmlmessage;

public class XMLImageMessage extends XMLMessage{

	private String mediaId;
	
	public XMLImageMessage(String toUserName, String fromUserName,String mediaId) {
		super(toUserName, fromUserName, "image");
		this.mediaId = mediaId;
	}


	@Override
	public String subXML() {
		return "<Image><MediaId><![CDATA["+mediaId+"]]></MediaId></Image>";
	}

	
}

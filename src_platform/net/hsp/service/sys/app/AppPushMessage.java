package net.hsp.service.sys.app;

/**
 * APP消息
 * 
 * @author nd0100
 * 
 */
public class AppPushMessage {
	private String noticeTitle;
	private String noticeText;
	private String content;
	/**
	 * get通知栏标题
	 * @return
	 */
	public String getNoticeTitle() {
		return noticeTitle;
	}
	
	/**
	 * set通知栏标题
	 * @param noticeTitle
	 */
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	
	/**
	 * get通知栏内容
	 * @return
	 */
	public String getNoticeText() {
		return noticeText;
	}
	
	/**
	 * set通知栏内容
	 * @param noticeText
	 */
	public void setNoticeText(String noticeText) {
		this.noticeText = noticeText;
	}
	
	/**
	 * get透传的内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * set透传的内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	

}

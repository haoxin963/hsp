package net.hsp.web.util;

import java.util.Map;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import net.hsp.common.CacheUtil;
import net.hsp.common.constants.PlatFormConstant;

public class SessionListener implements HttpSessionAttributeListener {

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		// System.out.println("session属性被添加，属性：" + event.getName() + "，值：" +
		// event.getValue());
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		try {
			if (PlatFormConstant.CURRENT_USERNAME.equals(event.getName())) {
				Map btns = (Map) event.getSession().getServletContext().getAttribute("btns");
				if (btns != null) {
					Map map = (Map) btns.get(event.getSession().getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE));
					if (map != null) {
						map.remove(event.getValue());
					}
				}
				try {
					//System.out.println(event.getSession().getAttribute(PlatFormConstant.CURRENT_USERID));
					String userId = String.valueOf(event.getSession().getAttribute(PlatFormConstant.CURRENT_USERID));
					String custId = String.valueOf(event.getSession().getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE));
					//CacheUtil.getInstance().getCache("session1800_sync").remove("fun_"+userId);
//					CacheUtil.getInstance().getCache("session1800_sync_online").remove(custId+"_"+userId); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("SessionListener Exception:" + e.getMessage());
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		// System.out.println("session属性被替换，名称：" + event.getName() + "，值：" +
		// event.getValue());
	}

}

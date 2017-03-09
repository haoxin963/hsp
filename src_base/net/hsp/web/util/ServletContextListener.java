package net.hsp.web.util;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class ServletContextListener implements ServletContextAttributeListener {

	public void attributeAdded(ServletContextAttributeEvent event) {

		//System.out.println("application属性被添加，属性：" + event.getName() + "，值：" + event.getValue());

	}

	public void attributeRemoved(ServletContextAttributeEvent event) {

		//System.out.println("application属性被删除，名称：" +event.getName() + "，值：" + event.getValue());

	}

	public void attributeReplaced(ServletContextAttributeEvent event) {

		//System.out.println("application属性被替换，名称：" + event.getName() + "，值：" + event.getValue());

	}

}

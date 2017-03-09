package net.hsp.service.sys.manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import net.hsp.common.PathUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author pengyq
 */
public class XMLFileUtil {
	private static String TYPE_CONTEXT = "context";
	private static String TYPE_SERVLET = "servlet";
	private static String TYPE_JBPM = "jbpm";

	public static String ELEMENT_TYPE_DATA = "datasource";
	public static String ELEMENT_TYPE_LUCENE = "lucene";
	public static String ELEMENT_TYPE_THREAD = "thread";
	public static String ELEMENT_TYPE_MAIL = "mail";

	/**
	 * 读取uisp的站点配置信息
	 * 
	 * @param file
	 *            文件在应用服务器下的全路径名
	 * @param preKey
	 *            bean的前缀
	 * @param dsname
	 *            站点名称
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String readContextXMLBean(String file, String preKey, String dsname) throws JDOMException, IOException {
		Namespace emptyNS = Namespace.getNamespace("", "http://www.springframework.org/schema/beans");
		String url = PathUtil.getRootPath() + file;
		SAXBuilder sb = new SAXBuilder();
		sb.setValidation(false);
		Document doc = sb.build(new File(url));
		Element root = doc.getRootElement();
		String newId = preKey + dsname;
		List childern = root.getChildren("bean", emptyNS);
		for (int i = 0; i < childern.size(); i++) {
			Element tmp = (Element) childern.get(i);
			String id = tmp.getAttributeValue("id");
			if (id != null && id.equals(newId)) {
				List properties = tmp.getChildren("property", emptyNS);
				for (int j = 0; j < properties.size(); j++) {
					Element p = (Element) properties.get(j);
					String name = p.getAttributeValue("name");
					if ("serviceUrl".equals(name)) {
						Element value = p.getChild("value", emptyNS);
						return value == null ? "" : value.getValue();
					}
				}
			}
		}
		return "";
	}

	/**
	 * 创建*-context.xml配置文件bean节点,这里主要datasource与lucene两大功能用到
	 * 
	 * @param file
	 *            文件在应用服务器下的全路径名
	 * @param preKey
	 *            bean的前缀
	 * @param dsname
	 *            站点名称
	 * @param isLocal
	 *            是否本地，本地和远程的bean的构建不一样,true-本地,false-远程
	 * @param type
	 *            何种功能用到此方法,分:"datasource"与"lucene"
	 * @param df
	 *            动态form，存放基本数据，主要用到"fileSystemPath"及"remoteDBAddress"2个参数值
	 * 
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static void createContextXMLBean(String file, String preKey, String dsname, boolean isLocal, String type, String ip, String dbPrefix, String initSize, String maxSize, String minSize) throws JDOMException, IOException {
		createXMLBean(file, preKey, dsname, TYPE_CONTEXT, isLocal, type, ip, dbPrefix, initSize, maxSize, minSize);
	}

	/**
	 * 删除指定*-context.xml配置文件内的dsname bean
	 * 
	 * @param file
	 *            文件在应用服务器下的全路径名
	 * @param preKey
	 *            bean的前缀
	 * @param dsname
	 *            站点名称
	 */
	public static void removeContextXMLBean(String file, String preKey, String dsname) {
		removeXMLBean(file, preKey, dsname, TYPE_CONTEXT);
	}

	/**
	 * 删除指定*-servlet.xml配置文件内的dsname bean
	 * 
	 * @param file
	 *            文件在应用服务器下的全路径名
	 * @param preKey
	 *            bean的前缀
	 * @param dsname
	 *            站点名称
	 */
	public static void removeServletXMLBean(String file, String preKey, String dsname) {
		removeXMLBean(file, preKey, dsname, TYPE_SERVLET);
	}

	/**
	 * 删除jbpm.cfg.xml内的context配置节点
	 * 
	 * @param file
	 *            文件在应用服务器下的全路径名，这里固定为根目录的jbpm.cfg.xml
	 * @param dsname
	 *            站点名称
	 */
	public static void removeJbpmXMLContext(String file, String dsname) {
		removeXMLBean(file, null, dsname, TYPE_JBPM);
	}

	/*
	 * 创建配置文件节点
	 */
	private static void createXMLBean(String file, String preKey, String dsname, String fileType, boolean isLocal, String type, String ip, String dbPrefix, String initSize, String maxSize, String minSize) throws JDOMException, IOException {
		if (dsname == null || dsname.equals(""))
			return;
		// 定义命名空间为空
		Namespace emptyNS = Namespace.getNamespace("", "http://www.springframework.org/schema/beans");
		String url = PathUtil.getRootPath() + file;

		SAXBuilder sb = new SAXBuilder();
		sb.setValidation(false);
		sb.setEntityResolver(new EntityResolver() {
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				return new InputSource(new StringReader(""));
			}
		});

		Document doc = sb.build(new File(url));
		Element root = doc.getRootElement();
		if (fileType.equals(TYPE_CONTEXT)) {
			String newId = preKey + "_" + dsname;
			removeBeanById(newId, root);
			root.addContent(createContextBeanElement(emptyNS, newId, dsname, isLocal, type, ip, dbPrefix, initSize, maxSize, minSize));
		}
		emptyNS = null;
		doc.setRootElement(root);
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		outputter.output(doc, new FileOutputStream(url));

	}

	private static void removeXMLBean(String file, String preKey, String dsname, String fileType) {
		try {
			if (dsname == null || dsname.equals(""))
				return;

			String url = PathUtil.getRootPath() + file;

			SAXBuilder sb = new SAXBuilder();
			sb.setValidation(false);
			Document doc = sb.build(new File(url));
			Element root = doc.getRootElement();

			String newId = preKey + dsname;
			removeBeanById(newId, root);

			doc.setRootElement(root);
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
			outputter.output(doc, new FileOutputStream(url));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 创建数据源或索引文件子节点
	private static Element createContextBeanElement(Namespace emptyNS, String newId, String dsname, boolean isLocal, String type, String ip, String dbPrefix, String initSize, String maxSize, String minSize) {
		Element emt = new Element("bean");
		emt.setAttribute("id", newId);
		emt.setNamespace(emptyNS);
		if (isLocal) { // 本地配置
			emt.setAttribute("class", "net.hsp.dao.ProxoolExtDataSource");
			emt.addContent(subElement(emptyNS, "value", "com.mysql.jdbc.Driver", "property", "name", "driver"));
			emt.addContent(subElement(emptyNS, "value", ip + "/" + dbPrefix + "_" + dsname, "property", "name", "driverUrl"));
			emt.addContent(subElement(emptyNS, "value", "fuisp", "property", "name", "user"));
			emt.addContent(subElement(emptyNS, "value", "fuisp", "property", "name", "password"));
			emt.addContent(subElement(emptyNS, "value", "proxool." + dsname, "property", "name", "alias"));
			emt.addContent(subElement(emptyNS, "value", "300000", "property", "name", "maximumActiveTime"));
			emt.addContent(subElement(emptyNS, "value", initSize, "property", "name", "prototypeCount"));
			emt.addContent(subElement(emptyNS, "value", minSize, "property", "name", "maximumConnectionCount"));
			emt.addContent(subElement(emptyNS, "value", minSize, "property", "name", "minimumConnectionCount"));
			emt.addContent(subElement(emptyNS, "value", "50", "property", "name", "simultaneousBuildThrottle"));
			emt.addContent(subElement(emptyNS, "value", "select CURRENT_DATE", "property", "name", "houseKeepingTestSql"));

			/**
			 * emt.setAttribute("class",
			 * "com.alibaba.druid.pool.DruidDataSource");
			 * emt.addContent(subElement(emptyNS, "value",
			 * "jdbc:mysql://localhost:3306/uisp3_proj", "property", "name",
			 * "url")); emt.addContent(subElement(emptyNS, "value", "fuisp",
			 * "property", "name", "username"));
			 * emt.addContent(subElement(emptyNS, "value", "fuisp", "property",
			 * "name", "password")); emt.addContent(subElement(emptyNS, "value",
			 * "1", "property", "name", "initialSize"));
			 * emt.addContent(subElement(emptyNS, "value", "1", "property",
			 * "name", "minIdle")); emt.addContent(subElement(emptyNS, "value",
			 * "50", "property", "name", "maxActive"));
			 * emt.addContent(subElement(emptyNS, "value", "60000", "property",
			 * "name", "maxWait")); emt.addContent(subElement(emptyNS, "value",
			 * "300000", "property", "name", "timeBetweenEvictionRunsMillis"));
			 * emt.addContent(subElement(emptyNS, "value", "50", "property",
			 * "name", "minEvictableIdleTimeMillis"));
			 * emt.addContent(subElement(emptyNS, "value", "select
			 * CURRENT_DATE", "property", "name", "validationQuery"));
			 */
		}
		return emt;
	}

	private static Element createServletBeanElement(Namespace emptyNS, String newId, String dsname, String newName, boolean isId) {
		Element emt = new Element("bean");

		return emt;
	}

	/*
	 * 创建节点子类
	 */
	private static Element subElement(Namespace emptyNS, String vKey, String vText, String pKey, String pAtt, String pAttText) {
		return subElement(emptyNS, vKey, "", vText, pKey, pAtt, pAttText, false);
	}

	/*
	 * 创建节点子类
	 */
	private static Element subElement(Namespace emptyNS, String vKey, String vAtt, String vText, String pKey, String pAtt, String pAttText, boolean isVAttribute) {

		Element value = new Element(vKey);
		if (isVAttribute) {
			value.setAttribute(vAtt, vText);
			value.setNamespace(emptyNS);
		} else {
			value.setText(vText);
			value.setNamespace(emptyNS);
		}

		Element node = new Element(pKey);
		node.setAttribute(pAtt, pAttText);
		node.setNamespace(emptyNS);
		node.addContent(value);
		return node;
	}

	/*
	 * 创建Jbpm配置文件的节点元素
	 */
	private static Element subJbpmElement(Namespace emptyNS, String name, String factory) {
		Element service = new Element("service");
		// service.setNamespace(emptyNS);
		service.setAttribute("name", name);
		service.setAttribute("factory", factory);
		return service;
	}

	/*
	 * 删除指定Element内的bean Id
	 */
	private static void removeBeanById(String newId, Element root) {
		removeBean(newId, root, "id");
	}

	/*
	 * 删除指定Element内的bean name
	 */
	private static void removeBeanByName(String newId, Element root) {
		removeBean(newId, root, "name");
	}

	/**
	 * 删除指定Element内的bean
	 * 
	 * @param newId
	 *            需要删除的beanId
	 * @param root
	 *            需要删除bean的父类
	 * @param key
	 *            需要删除的bean主属性
	 */
	public static void removeBean(String newId, Element root, String key) {
		List beans = root.getChildren();
		for (int i = 0; i < beans.size(); i++) {
			Element tmp = (Element) beans.get(i);
			if (tmp.getAttributeValue(key) != null && tmp.getAttributeValue(key).equals(newId)) {
				root.removeContent(tmp);
				break;
			}
		}
	}
}

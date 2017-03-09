<%@ page language="java"
	import="org.springframework.web.context.*,org.springframework.web.context.support.*,org.springframework.beans.factory.support.*,org.springframework.beans.factory.config.*"
	pageEncoding="UTF-8"%> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%
	//WebApplicationContextUtils.getWebApplicationContext(config.getServletContext(),"");
	 
 
	StringBuffer sb = new StringBuffer(); 
 
	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());

	String[] s = context.getBeanDefinitionNames(); 

	for (int i = 0; i < s.length; i++) {
		RootBeanDefinition a = new RootBeanDefinition(s[i]); 
		org.springframework.beans.factory.support.GenericBeanDefinition ab = new org.springframework.beans.factory.support.GenericBeanDefinition(a);
		 
		sb.append("<tr><td>" + s[i] + "</td><td>" + ab.isLazyInit() + "</td><td>"+ab.getAttribute("id")+"</td><td>移除</td></tr>");
	} 

	 
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:include page="/com/inc/incMeta.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>

	<jsp:include page="/com/easyui/inc.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>

	<body>
		<table class="easyui-datagrid" style="width: auto; height: auto">
			<thead>
				<tr>
					<th data-options="field:'itemid'">
						beanId
					</th>
					<th data-options="field:'sql2'">
						延迟加载
					</th>
					<th data-options="field:'sql'">
						scope
					</th>
					<th data-options="field:'diy'">
						操作	
					</th>
				</tr>
			</thead>
			<tbody>
				<%=sb.toString()%>
			</tbody>
		</table>
	</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>

    		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
	</head>

	<%
	/**

		<jsp:param name="funs" value="loadPage('cache/cache.jspx')"></jsp:param>
		<jsp:param name="funs" value="loadPage('log/sql.jspx')"></jsp:param>
		<jsp:param name="funs" value="loadPage('log/uri.jspx')"></jsp:param>
		<jsp:param name="funs" value="loadPage('log/session.jspx')"></jsp:param>
		<jsp:param name="funs" value="loadPage('serverInfo.jspx')"></jsp:param>
		*/
	%>
	<body>
		<%@include file="/view/sys/monitor/nav.jsp" %> 
	</body>
</html>

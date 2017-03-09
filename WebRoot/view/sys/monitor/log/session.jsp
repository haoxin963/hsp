<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true" session="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>登录日志列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/monitor/log/script/sessionList.js"></script>

	</head>
	<body class="bodyLayout">
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead"> 
					<th field="SESSIONID" width=70 report=true>
						SESSIONID
					</th>
					<th field="UserAgent" width=70 report=true>
						UserAgent
					</th>
					<th field="RemoteAddress" width=70 report=true>
						RemoteAddress
					</th>
					<th field="RequestCount" width=70 report=true>
						RequestCount
					</th>
					<th field="JdbcExecuteCount" width=70 report=true>
						JdbcExecuteCount
					</th>
					<th field="JdbcExecuteTimeMillis" width=70 report=true>
						JdbcExecuteTimeMillis
					</th>
					<th field="JdbcFetchRowCount" width=70 report=true>
						JdbcFetchRowCount
					</th>
					<th field="JdbcUpdateCount" width=70 report=true>
						JdbcUpdateCount
					</th> 
				</tr>
			</thead>
		</table>
	</body>
</html>

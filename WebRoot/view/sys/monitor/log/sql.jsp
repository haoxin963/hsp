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
		<script type="text/javascript" src="${vpath}/sys/monitor/log/script/sqlList.js"></script>

	</head>
	<body class="bodyLayout">
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead"> 
						<th field="SQL" width=170 report=true>
						SQL
					</th> 
					<th field="TotalTime"  report=true>
						执行时间
					</th>
					<th field="ExecuteCount"  report=true>
						执行数
					</th>
					<th field="FetchRowCount"  report=true>
						读取行数
					</th>
					<th field="MaxTimespan"  report=true>
						最慢的执行耗时
					</th>
				
					<th field="JdbcUpdateCount"  report=true>
						更新行数
					</th>
					
					<th field="InTransactionCount"  report=true>
						在事务中运行的次数
					</th> 
					<th field="ConcurrentMax"  report=true>
						最大并发
					</th> 
				</tr>
			</thead>
		</table>
	</body>
</html>

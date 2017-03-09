<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>用户操作日志列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/monitor/log/script/userActionLogList.js"></script>

	</head>
	<body class="bodyLayout">
		<div class="table-detail"> 
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead" id="exportHead" />
					<input type="text" style="display: none" name="exportTitle" id="exportTitle" />
					<table class='searchTable'>
						<tr>
							<td>用户名${separator}<input type='text' class='filter' name="filter[name]">	</td>
							<td>IP${separator}<input type='text' class='filter' name="filter[ip]">	</td>
							
							<td>
								<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include></td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th field="name" width="30" report=true>
						用户名
					</th>
					<th field="ip" width="30" report=true>
						IP
					</th>
					<th field="action" width="30" report=true>
						动作
					</th>
					<th field="operateTime" width="30" report=true>
						操作时间
					</th>
					<th field="ms" width="30" report=true>
						耗时
					</th>
				</tr>
			</thead>
		</table>
	</body>
</html>

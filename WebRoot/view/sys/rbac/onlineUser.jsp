<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>在线用户列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
	</head>
	<body class="bodyLayout">
		<table class="easyui-datagrid" style="width: auto; height: auto">
			<thead>
				<tr>
					<th data-options="field:'a'">

					</th>
					<th data-options="field:'b'">

					</th>
					<th data-options="field:'c'">

					</th>
					<th data-options="field:'d'">

					</th>
					<th data-options="field:'e'">

					</th>
					<th data-options="field:'f'">

					</th>
					<th data-options="field:'g'">

					</th>
					<th data-options="field:'h'">

					</th>
					<th data-options="field:'i'">

					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${command}" var="v" varStatus="vs">
					<td>
						${v.n}
					</td>
					<c:if test="${vs.count % 9 eq 0}">
						</tr>
						<tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>

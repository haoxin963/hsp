<%@page language="java" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>数据导入</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>
	<body class="bodyLayout">
		<form class="inputForm">
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<td class="valueTd" style="text-align: center;">
						${msg}
						<br />
						<a
							href="${path}/sys/dataimport/toImport.do?type=${command.type}&entity=${command.entity}"><input
								type="button" value="返回"> </a>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
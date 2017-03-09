<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色表列表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script type="text/javascript">
	var select_roles = [ ${roleids} ];
	var userId = ${userId};
</script>
</head>
<body class="bodyLayout">
	<div class="pd-10" style="background-color: #ffffff">
		<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				角色名称${separator} <input type='text'
					name="filter[roleName]" class="input-text" style="width:120px">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
	</div>

	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="id" style="width:30px"></th>
				<th w_index="role" width="40%;" report="true">角色标记</th>
				<th w_index="roleName" width="40%;" report="true">角色名称</th>
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript"
	src="${vpath}/sys/rbac/script/grant_role.js"></script>
</html>

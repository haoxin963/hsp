<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<title>用户表列表</title>
</head>

<body class="bodyLayout">
	<c:if test="${ param.selector ne 'true' }">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="批量删除"></jsp:param>
					<jsp:param name="texts" value="分配角色"></jsp:param>
					<jsp:param name="texts" value="启用/禁用"></jsp:param>
					<jsp:param name="texts" value="重置密码"></jsp:param>
					<jsp:param name="texts" value="权限视图"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete()"></jsp:param>
					<jsp:param name="funs" value="toGrantRole()"></jsp:param>
					<jsp:param name="funs" value="enableUser()"></jsp:param>
					<jsp:param name="funs" value="resetPwd()"></jsp:param>
					<jsp:param name="funs" value="viewUserAuth()"></jsp:param>
					<jsp:param name="funs" value="report()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="ids" value="id4"></jsp:param>
					<jsp:param name="ids" value="id5"></jsp:param>
					<jsp:param name="ids" value="id6"></jsp:param>
					<jsp:param name="ids" value="id7"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2;"></jsp:param>
					<jsp:param name="icons" value="&#xe62b;"></jsp:param>
					<jsp:param name="icons" value="&#xe61d;"></jsp:param>
					<jsp:param name="icons" value="&#xe68f;"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe644;"></jsp:param>
					
				</jsp:include>
				
			</span>
		</div>
	</c:if>

	<div class="pd-10" style="background-color: #ffffff">
		<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" /> <input type="text" style="display: none"
					name="exportTitle" id="exportTitle" /> <input type="text"
					style="display: none" name="exportField" id="exportField" />
				用户名${separator} <input type='text' class="input-text"
					style="width:120px" name="filter[userName]">
				真实姓名${separator} <input type='text' class="input-text"
					style="width:120px" name="filter[trueName]">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
	</div>


	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="id" style="width:30px"></th> 
				<th w_index="userName" width="25%;" report="true">用户姓名</th>
				<th w_index="trueName" width="25%;"  report="true">真实姓名</th>
				<th w_index="lastLogTime" width="15%;"  report="true">上次登录时间</th>
				<th w_index="status" w_render="disabled"   width="8%;">启用/禁用</th>
				<th w_render="operate" width="15%;">操作</th>
			</tr>
		</table>
	</div>
<script type="text/javascript"
	src="${vpath}/sys/rbac/script/userList.js"></script>
</body>
</html>

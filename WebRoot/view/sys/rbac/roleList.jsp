<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<title>角色表列表</title>
</head>
<body class="bodyLayout">
	<c:if test="${ param.selector ne 'true' }">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="批量删除"></jsp:param>
					<jsp:param name="texts" value="角色模块配置"></jsp:param>
					<jsp:param name="texts" value="角色用户关系"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="refFunc()"></jsp:param>
					<jsp:param name="funs" value="roleToUser()"></jsp:param>
					<jsp:param name="funs" value="report()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="ids" value="id4"></jsp:param>
					<jsp:param name="ids" value="id5"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2;"></jsp:param>
					<jsp:param name="icons" value="&#xe62b;"></jsp:param>
					<jsp:param name="icons" value="&#xe61d;"></jsp:param>
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
				角色名称${separator} <input type='text' class="input-text" style="width:120px"
					name="filter[roleName]">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
	</div>
	
	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="id" style="width:30px"></th> 
				<th w_index="roleName" width="45%;" report="true">角色名称</th>
				<th w_index="role" width="45%;"  report="true">角色标记</th>
				<th w_render="operate" width="15%;">操作</th>
			</tr>
		</table>
	</div>

</body>
<script type="text/javascript"
	src="${vpath}/sys/rbac/script/roleList.js"></script>
</html>

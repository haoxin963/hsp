<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>待办列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/workflow/script/todoList.js"></script>
	</head>
	<body class="easyui-layout" fit="true">
		<div class="toolbarDiv" align="left">
			<jsp:include page="/com/inc/toolbar.jsp">
				<jsp:param name="texts" value="新增"></jsp:param>
				<jsp:param name="funs" value="toAdd()"></jsp:param>
				<jsp:param name="ids" value="id1"></jsp:param>
				<jsp:param name="icons" value="icon-add"></jsp:param>
			</jsp:include>
		</div>
		<div id="tb" class="searchTb">
			<form id="searchForm" method="post" class="searchForm">
				流程名称${separator}
				<input name="filter[NAME]" class="filter">
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr>
					<th field="TITLE" width=180>
						流程名称
					</th>
					<th field="trueName" width=180>
						上次操作
					</th>
					<th data-options="width:50,field:'STATUS'">
						状态
					</th>
					<th field="START_DATE" width=180>
						开始时间
					</th>
					<th data-options="width:120,field:'STEP_NAME',formatter:rowformater">
						操作
					</th>
				</tr>
			</thead>
		</table>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>流程列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/workflow/script/entryList.js"></script>
	</head>
	<body class="easyui-layout" fit="true">
		<div class="toolbarDiv" align="left">
			
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
					<th field="NAME" width=180>
						流程类型
					</th>
					<th data-options="width:50,field:'STATE'">
						状态
					</th>
					<th data-options="width:120,field:'ENTRY_ID',formatter:rowformater">
						操作
					</th>
				</tr>
			</thead>
		</table>
	</body>
</html>

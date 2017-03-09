<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>pubmodule_noticeuser_tbl列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="dialog" value="v2" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/notice/script/noticeUserList.js"></script>
	</head>
	<body class="bodyLayout">
		<c:if test="${ param.selector ne 'true' }">
			<div class="toolbarDiv" align="left">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建推送"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="编辑"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param>
					<jsp:param name="icons" value="icon-remove"></jsp:param>
					<jsp:param name="icons" value="icon-edit"></jsp:param>
					<jsp:param name="exportXls" value="report(1);"></jsp:param>
					<jsp:param name="exportAllXls" value="report(2);"></jsp:param>
					<jsp:param name="exportPdf" value="report(3);"></jsp:param>
					<jsp:param name="exportAllPdf" value="report(4);"></jsp:param>
				</jsp:include>
			</div>
		</c:if>
		<div id="tb" class="searchTb">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" />
				<input type="text" style="display: none" name="exportTitle"
					id="exportTitle" />
				信息ID${separator}
				<input type='text' class='filter' name="filter[noticeId]">
				用户${separator}
				<input type='text' class='filter' name="filter[userId]">
				阅读回执${separator}
				<input type='text' class='filter' name="filter[remark]">
				阅读状态${separator}
				<input type='text' class='filter' name="filter[status]">
				更新时间${separator}
				<input type='text' class='filter' name="filter[updateTime]">
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'id',checkbox:true"></th>
					<th field="title" width=70 report=true>
						信息ID
					</th>
					<th field="userId" width=70 report=true>
						用户
					</th>
					<th field="remark" width=70 report=true>
						阅读回执
					</th>
					<th field="status" width=70 report=true>
						阅读状态
					</th>
					<th field="updateTime" width=70 report=true>
						更新时间
					</th>
					<th
						data-options="width:40,field:'fieldx',formatter:rowformater,align:'center'">
						操作
					</th>
				</tr>
			</thead>
		</table>
	</body>
</html>

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
		<title>pubmodule_noticecomment_tbl列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/notice/script/noticeCommentList.js"></script>
	</head>
	<body class="bodyLayout">
		<c:if test="${ param.selector ne 'true' }">
			<div class="toolbarDiv" align="left">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="返回"></jsp:param>
					<jsp:param name="texts" value="批量删除"></jsp:param>
					<jsp:param name="funs" value="doBack('${noticeId}')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="icons" value="icon-back"></jsp:param>
					<jsp:param name="icons" value="icon-remove"></jsp:param>
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
				评论人${separator}
				<input type='text' class='filter' name="userName">
				评论时间${separator}
				<input type='text' class='Wdate' name="dateTimeStart" readOnly="readOnly" onClick="WdatePicker()">
				~
				<input type='text' class='Wdate' name="dateTimeEnd" readOnly="readOnly" onClick="WdatePicker()">
				<input type='hidden' name="noticeId" value="${noticeId}">
				
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'id',checkbox:true"></th>
					<th field="userName" width=70 report=true>
						用户名
					</th>
					<th field="trueName" width=70 report=true>
						用户姓名
					</th>
					<th field="dateTime" width=100 report=true>
						评论时间
					</th>
					<th field="content" width=300 report=true>
						评论内容
					</th>
					<th data-options="width:40,field:'fieldx',formatter:rowformater,align:'center'">
						操作
					</th>
				</tr>
			</thead>
		</table>
	</body>
</html>

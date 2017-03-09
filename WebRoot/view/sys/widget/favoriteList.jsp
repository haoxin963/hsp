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
		<title>菜单收藏列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="dialog" value="v2" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/widget/script/favoriteList.js"></script>

	</head>
	<body class="bodyLayout">
		<c:if test="${ param.selector ne 'true' }">
			<div class="toolbarDiv" align="left">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
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
				</jsp:include>
			</div>
		</c:if>
		<c:if test="${searchType ne 'advance' }">
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" />
					<input type="text" style="display: none" name="exportTitle"
						id="exportTitle" />
					菜单别名${separator}
					<input type='text' class='filter' name="filter[aliasName]">
					<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
			<jsp:include page="/com/search/searchBtn.jsp">
				<jsp:param name="baseSearch"
					value="[{'field':'aliasName','text':'菜单别名'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'aliasName','text':'菜单别名','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'id',checkbox:true"></th>
					<th field="functionName" width=70 report=true>
						菜单原名
					</th>
					<th data-options="width:40,field:'iconPath',formatter:rowImage">
						菜单图标
					</th>
					<th field="aliasName" width=70 report=true>
						菜单别名
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

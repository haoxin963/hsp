<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>客户列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="dialog" value="v2" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/manage/script/customerList.js"></script>

	</head>
	<body class="bodyLayout">
		<div class="table-detail">
			<c:if test="${ param.selector ne 'true' }">
				<div class="toolbarDiv" align="left">
					<jsp:include page="/com/inc/toolbar.jsp">
						<jsp:param name="texts" value="新建"></jsp:param>
						<jsp:param name="texts" value="删除"></jsp:param>
						<jsp:param name="texts" value="编辑"></jsp:param>
						<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
						<jsp:param name="funs" value="doDelete('')"></jsp:param>
						<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
						<jsp:param name="ids" value="addBtn"></jsp:param>
						<jsp:param name="ids" value="removeBtn"></jsp:param>
						<jsp:param name="ids" value="editBtn"></jsp:param>
						<jsp:param name="icons" value="icon-add"></jsp:param>
						<jsp:param name="icons" value="icon-remove"></jsp:param>
						<jsp:param name="icons" value="icon-edit"></jsp:param>
					</jsp:include>
				</div>
			</c:if>
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportTitle" id="exportTitle" />
					<input type="text" style="display: none" name="exportHead" id="exportHead" />
					<input type="text" style="display: none" name="exportField" id="exportField" />
					<table class='searchTable'>
						<tr>

							<td>
								客户名称${separator}
							</td>
							<td>
								<input type='text' class='filter' name="filter[custName]">
							</td>

							<td>
								状态${separator}
							</td>
							<td>
								<input type='text' class='filter' name="filter[status]">
							</td>
							<td><jsp:include page="/com/inc/searchBtn.jsp"></jsp:include></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'custId',checkbox:true"></th>

					<th field="custName" width=70 report=true>
						客户名称
					</th>
					<th field="phone" width=70 report=true>
						电话
					</th>
					<th field="address" width=70 report=true>
						地址
					</th>
					<th width=20 report=true data-options="field:'status',formatter:statusformater,align:'center'">
						状态
					</th>
					<c:if test="${ param.selector ne 'true' }">
						<th data-options="field:'x',formatter:rowformater,align:'center'">
							操作
						</th>
					</c:if>
				</tr>
			</thead>
		</table>
	</body>
</html>

<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>员工通讯录</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script type="text/javascript"
	src="${path}/resource/layout/jquery-ui.js"></script>
<script type="text/javascript"
	src="${path}/resource/layout/jquery.layout.js"></script>
<script>
	$(document).ready(function() {
		$("body").layout({
			applyDefaultStyles : true,
			spacing_open : 3
		});
	});
</script>

</head>
<body>
<div class="ui-layout-west" data-options="region:'west',split:true" title="${s.treeTitle}">
		<jsp:include page="/resource/inc/incSimpleTree.jsp">
			<jsp:param name="serviceBeanId" value="employeeServiceImpl" />
			<jsp:param name="idField" value="${s.idField}" />
			<jsp:param name="parentIdField" value="${s.parentIdField}" />
			<jsp:param name="labelField" value="${s.labelField}" />
			<jsp:param name="tableName" value="${s.tableName}" />
			<jsp:param name="treeSQL" value="${s.treeSQL}" />
		</jsp:include>
	</div>

<div class="ui-layout-center" style="padding:0px">
		<c:if test="${ param.selector ne 'true' }">
			
			<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
		</div>
		
		</c:if>
		<c:if test="${searchType ne 'advance' }">
				<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" /> <input type="text" style="display: none"
						name="exportTitle" id="exportTitle" /> 工号: <input type='text'
						class="input-text"  style="width:120px" name="filter[e.empno]"> 姓名: <input
						type='text' class="input-text"  style="width:120px"name="filter[e.empname]"> 部门: <input
						type='text' class="input-text"  style="width:120px"name="filter[d.departmentName]">
					岗位: <input type='text' class="input-text"  style="width:120px" name="filter[p.postName]">
					<input type='hidden' id="deptId" 
						name="filter[e.deptId]"> <input type='hidden' id="postId"
						name="filter[ep.postId]">
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
			</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
			<jsp:include page="/resource/search/searchBtn.jsp">
				<jsp:param name="hidenFields"
					value="[{'name':'filter[e.deptId]','id':'deptId'},{'name':'filter[ep.postId]','id':'postId'}]"></jsp:param>
				<jsp:param name="baseSearch"
					value="[{'field':'e.empno','text':'工号'},{'field':'e.empname','text':'姓名'},{'field':'d.departmentName','text':'部门'},{'field':'p.postName','text':'岗位'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'e.empno','text':'工号','type':'string'},{'field':'e.empname','text':'姓名','type':'string'},{'field':'d.departmentName','text':'部门','type':'string'},{'field':'p.postName','text':'岗位','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>

      	<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="areaId" style="width:30px"></th>
					<th w_index="empno"  width="30%;"  report="true">工号</th>
					<th w_index="empname" width="30%;" report="true">姓名</th>
						<th w_index="mobile"  width="30%;"  report="true">手机号</th>
					<th w_index="email" width="30%;" report="true">email</th>
						<th w_index="address"  width="30%;"  report="true">地址</th>
					<th w_index="homeTel" width="30%;" report="true">家庭电话</th>
						<th w_index="officeTel"  width="30%;"  report="true">办公室电话</th>
				</tr>

			</table>
		</div>
	</div>
	<script type="text/javascript"
	src="${vpath}/sys/org/script/addressbookList.js"></script>
</body>

</html>

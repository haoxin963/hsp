<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>部门信息列表</title>
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
<body >
<div class="ui-layout-west" data-options="region:'west',split:true" title="${s.treeTitle}">
		<jsp:include page="/resource/inc/incSimpleTree.jsp">
			<jsp:param name="serviceBeanId" value="departmentServiceImpl" />
			<jsp:param name="callBack" value="expendFirstLevel" />
		</jsp:include>
	</div>
<div class="ui-layout-center" style="padding:0px">
			<c:if test="${ param.selector ne 'true' }">
				<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
			<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="编辑"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
					<jsp:param name="funs" value="report()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="ids" value="id4"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
					<jsp:param name="icons" value="&#xe644;"></jsp:param>
				</jsp:include>
					</span>
				</div>
		</c:if>
		<c:if test="${searchType ne 'advance' }">
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" /> <input type="text" style="display: none"
						name="exportTitle" id="exportTitle" /> <input type="text"
						style="display: none" name="exportField" id="exportField" /> <input
						type="text" style="display: none" name="filter[parent_Id]"
						id="parentId" /> 名称： <input type='text' class="input-text"  style="width:120px" 
						name="filter[departmentName]"> 简称： <input type='text' class="input-text"  style="width:120px" 
						name="filter[shortName]"> 部门编号： <input type='text'
						class="input-text"  style="width:120px" name="filter[departmentNumber]">
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
			<jsp:include page="/com/search/searchBtn.jsp">
				<jsp:param name="hidenFields"
					value="[{'name':'filter[parent_Id]','id':'parentId'}]"></jsp:param>
				<jsp:param name="baseSearch"
					value="[{'field':'departmentName','text':'名称'},{'field':'shortName','text':'简称'},{'field':'departmentNumber','text':'部门编号'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'departmentName','text':'名称','type':'string'},{'field':'shortName','text':'简称','type':'string'},{'field':'departmentNumber','text':'部门编号','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>

			<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="name" width="25%;"  report=true>名称</th>
					<th w_index="shortName"  width="25%;"  report=true>简称</th>
					<th w_index="code"  width="25%;"  report=true>部门编号</th>
					<th w_index="isdisplay"
						w_render="fdisplayformatter"
						 width="25%;"  report=true>是否显示</th>
					<th w_index="remark"  width="35%;"  report=true>描述</th>
						<c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>

			</table>
		</div>
	</div>
	
	<script type="text/javascript"
	src="${vpath}/sys/org/script/departmentList.js"></script>
</body>

</html>

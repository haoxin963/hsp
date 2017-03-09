<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
		<title>岗位信息列表</title>
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
	<div class="ui-layout-west"  >
	<div class="layui-tab"   lay-filter="role"   style="margin-top:-5px;">
	    <ul class="layui-tab-title  " >
			<li class="layui-this" style="width:79px;">部门视图</li>
			<li style="width:79px;">岗位视图</li>
		</ul>
		<jsp:include page="/resource/inc/incSimpleTree.jsp">
			    <jsp:param name="serviceBeanId" value="postServiceImpl" />
				<jsp:param name="idField" value="${s.idField}" />
				<jsp:param name="parentIdField" value="${s.parentIdField}" />
				<jsp:param name="labelField" value="${s.labelField}" />
				<jsp:param name="tableName" value="${s.tableName}" />
				<jsp:param name="treeSQL" value="${s.treeSQL}" />
		</jsp:include>
	</div>
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
				<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
					<form id="searchForm" method="post" class="searchForm">
						<input type="text" style="display: none" name="exportHead" id="exportHead" />
						<input type="text" style="display: none" name="exportTitle" id="exportTitle" />
						<input type="text" style="display: none" name="exportField" id="exportField" />	
						
						<input type="text" style="display: none" name="filter[p.deptId]"
							id="deptId" />
						<input type="text" style="display: none" name="filter[p.parentId]"
							id="parentId" />
						岗位代号：
						<input type='text' class='input-text'  style="width:120px" name="filter[p.postNo]">
						岗位名称：
						<input type='text' class='input-text'  style="width:120px" name="filter[p.postName]">
						岗位简称：
						<input type='text' class='input-text'  style="width:120px" name="filter[p.shortName]">					 
							<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
			<jsp:include page="/resource/inc/searchBtn.jsp">
				<jsp:param name="hidenFields"
					value="[{'name':'filter[p.deptId]','id':'deptId'},{'name':'filter[p.parentId]','id':'parentId'}]"></jsp:param>
				<jsp:param name="baseSearch"
					value="[{'field':'p.postNo','text':'岗位代号'},{'field':'p.postName','text':'岗位名称'},{'field':'p.shortName','text':'岗位简称'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'p.postNo','text':'岗位代号','type':'string'},{'field':'p.postName','text':'岗位名称','type':'string'},{'field':'p.shortName','text':'岗位简称','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>
         
         <div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="deptName" width="25%;" report="true">部门名称</th>
					<th w_index="postNo" width="25%;" report="true">岗位代号</th>
					<th w_index="postName" width="25%;" report="true">岗位名称</th>
					<th w_index="parentName" width="25%;" report="true">上级岗位</th>
					<c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>

			</table>
		</div>
	</div>
      <script type="text/javascript" src="${vpath}/sys/org/script/postList.js"></script>   
		<script>
		var index = 0;
		layui.use('element',function() {
			var element = layui.element();

			//一些事件监听
			element
					.on(
							'tab(role)',
							function(data) {
				 
								index = data.index;
								loadTree(index);
							});
						});
		
		</script>
	</body>	 
</html>

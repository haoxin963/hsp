<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>pubmodule_noticecategory_tbl列表</title>
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
				<%@include file="/view/sys/notice/noticeCategoryTree.jsp" %>
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
					<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" />
					<input type="text" style="display: none" name="exportTitle"
						id="exportTitle" />
					<input type="text" style="display: none" name="filter[parentId]"
						id="parentId" />
					类型名称:
					<input type='text' class="input-text"  style="width:120px"name="filter[name]">
						<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
					</div>
		</div>
		
		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_render="rowParent" width="25%;" report="true">上级栏目</th>
					<th w_index="code" width="25%;" report="true">	代号</th>
					<th w_index="name" width="25%;" report="true">名称</th>
					<th w_index="remark" width="25%;" report="true">备注</th>
					<c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>

			</table>
		</div>
	</div>
	</body>
		<script type="text/javascript"
			src="${vpath}/sys/notice/script/noticeCategory.js"></script>
	<script>
		var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
					pIdKey:"parentId"
				}
			},
			callback: { 
		        onClick: onTreeClickBack  
		    }  
		};
 		 
		function onTreeClickBack(){ 
			var nodes = zTree.getSelectedNodes();
			if(nodes.length>0){ 
				document.getElementById("parentId").value = nodes[0].id;
				doList();
			}
		};
		
		$(document).ready(function(){
			 createTree(); 
		}); 
 	</script>
</html>

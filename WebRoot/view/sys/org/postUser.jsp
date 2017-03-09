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
		<title>部门岗位下的用户</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/org/script/postUser.js"></script>
		<style>.pagination-info{display:none;}</style>
	</head>
	<body class="easyui-layout">
		<div data-options="region:'west',split:true" title="部门岗位视图" style="width: 160px;">
			<jsp:include page="/com/inc/incSimpleTree.jsp">
				<jsp:param name="serviceBeanId" value="employeeServiceImpl" /> 
				<jsp:param name="callBack" value="expendFirstLevel" />
			</jsp:include>
		</div> 
				
		<div data-options="region:'center',border:false"> 
			<form id="searchForm" method="post" class="searchForm" style="display:none"> 
					<input type='hidden' id="deptId" class='filter' name="filter[e.deptId]">
					<input type='hidden' id="postId" class='filter' name="filter[p.postId]">	 
		</form>
			<table id="dg" width="100%" height="100%">
				<thead>
					<tr id="gridThead">
						<th data-options="field:'id',checkbox:true"></th>
						<th  width=70 report=true data-options="field:'isdisplay',formatter:fmt" >
							用户名/姓名
						</th>
					</tr>
				</thead>
			</table>
		</div>
			<script>
				function getChecked(){ 
					var obj = [];
					var nodes = $('#dg').datagrid('getSelections');  
					for(var i=0;i<nodes.length;i++){
						var item = {"id":nodes[i].id,"name":nodes[i].username,"key":"value"};
						obj.push(item);
					} 
					return obj;
				}	
			</script>
	</body>
 
</html>

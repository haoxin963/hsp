<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<title>员工信息列表</title>
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
						<jsp:param name="texts" value="批量生成用户"></jsp:param>
						<jsp:param name="texts" value="手动关联用户"></jsp:param>
						<jsp:param name="texts" value="导出"></jsp:param>
						<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
						<jsp:param name="funs" value="doDelete('')"></jsp:param>
						<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
						<jsp:param name="funs" value="doBatchToUser()"></jsp:param>
						<jsp:param name="funs" value="doSelectUser()"></jsp:param>
						<jsp:param name="funs" value="report()"></jsp:param>
						<jsp:param name="ids" value="id1"></jsp:param>
						<jsp:param name="ids" value="id2"></jsp:param>
						<jsp:param name="ids" value="id3"></jsp:param>
						<jsp:param name="ids" value="id4"></jsp:param> 
						<jsp:param name="ids" value="id5"></jsp:param> 
						<jsp:param name="ids" value="id6"></jsp:param>
						<jsp:param name="icons" value="&#xe600;"></jsp:param>
						<jsp:param name="icons" value="&#xe6e2"></jsp:param>
						<jsp:param name="icons" value="&#xe6df;"></jsp:param>
						<jsp:param name="icons" value="&#xe6df;"></jsp:param>
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
						<input type="text" style="display: none" name="exportHead"
							id="exportHead" />
						<input type="text" style="display: none" name="exportTitle"
							id="exportTitle" />					 
						工号:
						<input type='text' class="input-text"  style="width:120px" name="filter[e.empno]">
						姓名:
						<input type='text' class="input-text"  style="width:120px" name="filter[e.empname]">
						部门:
						<input type='text'  class="input-text"  style="width:120px" name="filter[d.departmentName]">
						岗位:
						<input type='text'  class="input-text"  style="width:120px" name="filter[p.postName]">
						<input type='hidden' id="deptId" class='filter' name="filter[e.deptId]">
						<input type='hidden' id="postId" class='filter' name="filter[ep.postId]">				 
							<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
					</form>
				</div>
		</div>
			</c:if>
			<c:if test="${searchType eq 'advance' }">
				<jsp:include page="/resource/inc/searchBtn.jsp">
					<jsp:param name="hidenFields"
						value="[{'name':'filter[p.deptId]','id':'deptId'},{'name':'filter[ep.postId]','id':'postId'}]"></jsp:param>
					<jsp:param name="baseSearch"
						value="[{'field':'e.empno','text':'工号'},{'field':'e.empname','text':'姓名'},{'field':'d.departmentName','text':'部门'},,{'field':'p.postName','text':'岗位'}]"></jsp:param>
					<jsp:param name="advanceSearch"
						value="{'items':[{'field':'e.empno','text':'工号','type':'string'},{'field':'e.empname','text':'姓名','type':'string'},{'field':'d.departmentName','text':'部门','type':'string'},{'field':'p.postName','text':'岗位','type':'string'}]}"></jsp:param>
				</jsp:include>
			</c:if>
			
			<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="empno" width="25%;" report="true">工号</th>
					<th w_index="empname" width="25%;" report="true">	姓名</th>
					<th w_index="mobile" width="25%;" report="true">手机</th>
					<th w_index="deptName" width="25%;" report="true">部门</th>
					<th w_index="postName" width="25%;" report="true">	岗位</th>
					<th w_index="sex" width="25%;" report="true">性别</th>
					<th w_render="status" width="25%;" report="true">状态</th>
					<th w_index="userName" width="25%;" report="true">用户名</th>
					<c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>

			</table>
		</div>
	</div>
	
		
		
		
		<script type="text/javascript"
			src="${vpath}/sys/org/script/employeeList.js"></script>
	</body>
 
</html>

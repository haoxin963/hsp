<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
		<title>职位信息列表</title>
	</head>
	<body class="bodyLayout">
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
					职位代号${separator}
					<input type='text' ' class="input-text"  style="width:120px" name="filter[positionNo]">
					职位名称${separator}
					<input type='text' ' class="input-text"  style="width:120px" name="filter[positionName]">				 
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
				<jsp:include page="/resource/inc/searchBtn.jsp">
				<jsp:param name="baseSearch"
					value="[{'field':'positionNo','text':'职位代号'},{'field':'positionName','text':'职位名称'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'positionNo','text':'职位代号','type':'string'},{'field':'positionName','text':'职位名称','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>
		
		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="positionNo"  width="25%;"  report="true">职位代号</th>
					<th w_index="positionName" width="25%;" report="true">职位名称</th>
					<th w_index="roleName" width="25%;" report="true">对应角色</th>
					<th w_render="operate" width="15%;">操作</th>
				</tr>

			</table>
		</div>
	<script type="text/javascript"
	src="${vpath}/sys/org/script/positionList.js"></script>
	</body>
</html>

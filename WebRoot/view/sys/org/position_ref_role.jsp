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
		<title>角色表列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/org/script/position_ref_role.js"></script>
		<script type="text/javascript">
			var select_roles = [${roleids}];
		</script>
	</head>
	<body class="bodyLayout">
		<div class="toolbarDiv">
			<jsp:include page="/com/inc/toolbar.jsp"></jsp:include>
		</div>
		<div id="tb" class="searchTb">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" />
				<input type="text" style="display: none" name="exportTitle"
					id="exportTitle" />				  
				角色名称${separator}
				<input type='text' class='filter' name="filter[roleName]">				 
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>

		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'id',checkbox:true"></th>
					<th field="role" width=70 report=true>
						角色标记
					</th>
					<th field="roleName" width=70 report=true>
						角色名称
					</th>
				</tr>
			</thead>
		</table>
		
	</body>
</html>

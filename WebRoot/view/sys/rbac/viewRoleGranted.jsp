<%@page import="net.hsp.entity.sys.rbac.User"%>
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
<title>角色分配视图</title>
<jsp:include page="/com/inc/incCssJs.jsp">
	<jsp:param name="com" value="core" />
</jsp:include>
<jsp:include page="/com/inc/jsonList.jsp">
	<jsp:param name="command" value="command"></jsp:param>
</jsp:include>

</head>
<body class="bodyLayout">
	<div class="toolbarDiv">
		<jsp:include page="/com/inc/toolbar.jsp"></jsp:include>
	</div>
	<div id="tb" class="searchTb">
		<b>角色名称:&nbsp;</b>${role.roleName}
	</div>

	<table id="dg" width="100%" height="100%">
		<thead>
			<tr id="gridThead">
				<th data-options="checkbox:true"></th>
				<th field="id" width="100" report=true>ID</th>
				<th field="userName" width="300" report=true>用户名</th>
				<th field="trueName" width="300" report=true>真实姓名</th>
			</tr>
		</thead>
	</table>
	
	<script type="text/javascript">
	var basePath = path + '/sys/rbac/role';
	var dg;
	$(function() {
		var i = 0;
		var listPath = "viewRoleGranted.json?filter[rid]=${role.id}"
		if (typeof (dataList) != "undefined") {
			dg = $('#dg').datagrid({
				data : dataList,
				pagination : true, 
				fitColumns : constants.fitColumns,
				striped : true,
				rownumbers : true,
				loadFilter : function(r) {
					if (r.command) { 
						return r.command;
					} else {
						return r;
					}
				},
				onBeforeLoad : function() {
					if (i == 1) {
						var opts = $(this).datagrid('options');
						opts.url = listPath;
					}
					i++;
				}
			});
		} else {
			dg = $('#dg').datagrid({
				url : listPath,
				loadFilter : function(r) {
					if (r.command) {
						return r.command;
					} else {
						return r;
					}
				},
				pagination : true,
				fitColumns : constants.fitColumns,
				striped : true,
				rownumbers : true
			});
		}

	});
	</script>
	
</body>
</html>

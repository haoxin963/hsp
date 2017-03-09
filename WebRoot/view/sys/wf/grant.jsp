<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>流程使用权限分配 </title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<body>
	
		 
		 
 
		 <div id="tabs" class="easyui-tabs" style="width:auto;height:auto">    
		 	<div title="角色分配"> <table id="dg2" border="false">
				<thead>
					<tr> 
						<th field="roleName"  width="30">
							角色名称
						</th>
						
						<th field="role"  width="30">
						 	代码
						</th> 
						<th data-options="width:10,field:'a',formatter:rowformaterRole"> 
							操作
						</th>
					</tr>
				</thead>
			</table></div>
			<div title="用户分配">
			<table id="dg" border="false">
				<thead>
					<tr>
						
						<th field="userName"  width="30">
							用户名
						</th>
						
						<th field="trueName"  width="30">
						 	姓名
						</th> 
						<th field="xx"  width="10">
							选中
						</th>
					</tr>
				</thead>
			</table>
			</div>    
		 </div>  
		
	</body>
	<script>
var basePath = '${path}/sys/rbac/user';
var dg;
var dg2;
$(function(){
	var listPath = basePath+"/doList.json";
	dg = $('#dg').datagrid({
	    url:listPath,
			loadFilter: function(r){ 
				if (r.command){ 
					return r.command;
				} else {
				return r;
			}
		}, 
	    pagination:true, 
	    fitColumns:constants.fitColumns,
	    striped:true,
	    rownumbers:true,
	    checkOnSelect:false
	});  
	
	var listPath = "${path}/sys/rbac/role/doList.json";
	dg2 = $('#dg2').datagrid({
	    url:listPath,
			loadFilter: function(r){ 
				if (r.command){ 
					return r.command;
				} else {
				return r;
			}
		}, 
	    pagination:true, 
	    fitColumns:constants.fitColumns,
	    striped:true,
	    rownumbers:true,
	    checkOnSelect:false 
	});
}); 

function execGrant(id){
	
	jQuery.ajax({
		type: 'POST',
		url: basePath+"/doList.json",
		success: function(){
			$.dialog.tips('    授权成功!   ');
		},
		dataType: "json"
		});
}

function rowformaterRole(value,row,index){
	return "<input type='checkbox' value='1'  checked onclick='execGrant("+row.id+")' />";	
}
</script>
</html>
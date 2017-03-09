<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>所有流程</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<body>
		<div class="easyui-layout" fit="true">
			<div class="toolbarDiv" align="left">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="部署流程"></jsp:param> 
					<jsp:param name="texts" value="启用/禁用"></jsp:param>
					<jsp:param name="funs" value="deploy()"></jsp:param> 
					<jsp:param name="funs" value="enableUser()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>  
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param> 
					<jsp:param name="icons" value="icon-enable"></jsp:param>
				</jsp:include>
			</div>
			<table id="dg">
				<thead>
					<tr>
						<th field="name" width="32">
							流程名称
						</th>
						
						<th field="description">
							表单
						</th>
						<th field="version" width="20">
							版本
						</th>
						<th field="id" width="30">
							ProcessDefinitionId/DeploymentId
						</th> 
						<th field="key" width="22">
							KEY
						</th>
						<th field="isSuspended" width="12">
							是否挂起
						</th>
						<th data-options="field:'opr',formatter:rowformater" width="30">
							操作
						</th>
					</tr>
				</thead>
			</table>

		</div>
	</body>
	<script>
var basePath = '${path}/sys/wf';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/processList.json";
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
	    rownumbers:true
	});  
}); 

function doDelete(id){  
	jQuery.messager.confirm(constants.confirmDelete,constants.confirmDelete,function(r){
			if (r){ 
				function success(result){
					if(result.status=='1'){
						jQuery.each(rows, function (i, n) {
						 	dg.datagrid('deleteRow',dg.datagrid('getRowIndex',n));
						});
					}else{
						parent.jQuery.messager.alert(constants.alertTitle, result.msg, 'error');
					}
				};
				jQuery.ajax({
				type: 'POST',
				url: basePath+"/delete.json?deploymentId="+id, 
				success: function(){
					dg.datagrid('reload');
				},
				dataType: "json"
				});
			}
	});
};

function showImg(id){  
	$.dialog({cancel:true,title:'流程图',content: 'url:'+basePath+'/resourceRead.do?resourceType=image&processDefinitionId='+id,name:'a',id:'bbb',lock: true,parent:this,width: 1000, height: 500});
};

function start(startPage,key){  
	window.location = basePath+"/toStart.do?wfKey="+key+"&formUrl="+startPage;
};

function deploy(startPage,key){  
	$.dialog({title:'流程部署',content: 'url:'+basePath+'/deploy.jspx',name:'a',id:'bbb',lock: true,parent:this,max:false,width: 400, height: 90});
};

function changeStatus(status,processDefinitionId){
	function ok1(){ 
		var cascade = top.document.getElementById("cascade").value;
		jQuery.ajax({
			type: 'POST',
			async:false,
			url: basePath+"/changeStatus.json?cascade="+cascade+"&processDefinitionId="+processDefinitionId+"&status="+status,
			success: function(){
				 dg.datagrid('reload');
				 return true;
			},
			dataType: "json"
		});
		
	}
	$.dialog({content: '同时挂起相关流程:<select id="cascade"><option value="true">级联</option><option value="false">不级联</option></select>',max:false,lock:true,title:'确认',ok:ok1,width:250,height:100});
}

function grant(id){
	$.dialog({content: 'url:'+basePath+'/grant.jspx',lock:true,title:'流程启动权限分配',width:750,height:500});
}

function rowformater(value,row,index)
{ 
	var str = "<a href='javascript:void(0);' name='grant' onclick=grant('"+row.id+"')>权限设置</a>&nbsp;&nbsp;";
	if(row.isSuspended){
		str += "<a href='javascript:void(0);' name='changeStatus' onclick=changeStatus('active','"+row.id+"')>激活</a>";
	}else{
		str += "<a href='javascript:void(0);' name='changeStatus' onclick=changeStatus('suspend','"+row.id+"')>挂起</a>";
	}
	
	return str+"&nbsp;&nbsp;<a href='javascript:void(0);' name='imgBtn' onclick=showImg('"+row.id+"')>流程图</a>&nbsp;&nbsp;<a href='javascript:void(0);'  name='delBtn' onclick=doDelete('"+row.deploymentId+"')><img src='"+path+"/com/images/icon/edit_remove.png' title='"+constants.deleteForm+"'/></a>";
};
</script>
</html>
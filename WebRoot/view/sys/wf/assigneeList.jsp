<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>待办任务列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<body class="bodyLayout">  
				<table id="dg" width="100%" height="100%"  border=false>
					<thead>
						<tr>
							<th field="taskId" hidden="true">
								编号
							</th>
							<th field="defname" width="50">
								流程名称
							</th>
							<th field="taskname" width="50">
								任务名称
							</th>
							<th field="x" width="50">
								上一步骤提交人
							</th>
							<th field="startuser" width="50">
								创建人
							</th>
							<th field=createtime width="50">
								创建时间
							</th>
							<th data-options="field:'opr',formatter:rowformater" width="20">
								操作
							</th>
						</tr>
					</thead>
				</table> 

		<script type="text/javascript"> 
		 
		
		function toTaskPage(page,pid,taskId){
			window.location = "${path}/sys/wf/toTaskPage.do?taskId="+taskId+"&formUrl="+page+"&processInstanceId="+pid;
		};
		
		function rowformater(value,row,index)
		{  
			var str = "<a href='javascript:void(0);' name='startBtn' onclick=toTaskPage('"+row.description+"','"+row.piid+"','"+row.taskid+"')>办理</a>&nbsp;&nbsp;";
			str+="<a href='javascript:void(0);' name='startBtn' onclick=processInstanceHistory('"+row.piid+"')>流程历史</a>";
			return str;
		};
		
	    var basePath = '${path}/sys/wf';
		var dg;
		$(function(){
			var i=0;
			var listPath = basePath+"/assigneeList.json";
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
			    striped:true,
			    fitColumns:true,
			    fit:true,
			    rownumbers:true
			});  
		}); 
		
		function processInstanceHistory(processInstanceId){ 
			var url = basePath+"/processInstanceHistory.jspx?processInstanceId="+processInstanceId;
			$.dialog({cancel:true,title:'流程图',content: 'url:'+url,name:'selectorFrame',id:'bbb',lock: true,parent:this,width: 1000, height: 500});
		}
	     
	</script>
	</body>
</html>
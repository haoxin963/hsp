<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>待认领</title>
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
					<th field="name" width="50">
						任务名称
					</th>
					<th field="processDefinitionId" width="50">
						流程定义
					</th>
					<th data-options="field:'opr',formatter:rowformater">
								操作
					</th>
				</tr>
			</thead>
		</table> 
	</body> 
	
		<script>
			var basePath = '${path}/sys/wf';
			var dg;
			$(function(){
				var i=0;
				var listPath = basePath+"/waitingClaimList.json";
				dg = $('#dg').datagrid({
				    url:listPath,
						loadFilter: function(r){ 
							if (r.command){ 
								return r.command;
							} else {
							return r;
						}
					},  
				    fitColumns:constants.fitColumns,
				    striped:true,
				    rownumbers:true
				});  
			}); 
			   
			 function claimTask(taskId){
				if(window.confirm('确定签收吗？')){
					window.location = basePath+'/claimTask.json?taskId='+taskId;					
				}
			}  
			 
			function rowformater(value,row,index)
			{ 
				return "<a href=\"#\" onclick=\"claimTask('"+row.taskId+"')\">签收</a>";
			};
			</script>
</html>
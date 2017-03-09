<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>流程监控</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<body>
		<div class="easyui-layout" fit="true">
			<div region="center" style="padding: 1px;">
				<table id="dg" >
					<thead>
						<tr>
							  
							<th field="name" width="50">
								名称
							</th>
							<th field="processDefinitionId" width="50">
								流程定义
							</th>
							<th field="taskName" width="50">
								当前任务名称
							</th>
							
							<th field="processInstanceId" width="50">
								流程实例
							</th>
							<th field="activityId" width="50">
								activityId
							</th>
							<th data-options="field:'opr',formatter:rowformater">
								操作
							</th>
						</tr>
					</thead>
				</table>

			
			</div>
		</div>
	</body>
	
		<script type="text/javascript"> 
				var basePath = '${path}/sys/wf';
				var dg;
				$(function(){
					var i=0;
					var listPath = basePath+"/runningList.json";
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
					    fit:true,
					    rownumbers:true
					});  
				}); 
				 
				function deleteProcessInstance(processInstanceId)
				{ 
					$.dialog.confirm('确认删除正在运行的流程吗?', function(){
						jQuery.ajax({
							type: 'POST',
							async:false,
							url: basePath+"/deleteProcessInstance.json?processInstanceId="+processInstanceId,
							success: function(){
								$.dialog.tips('操作成功!');
								 dg.datagrid('reload');
								 return true;
							},
							dataType: "json"
						});
					});
					
				};
				
				function rowformater(value,row,index)
				{ 
					return "<a href='javascript:void(0);' name='startBtn' onclick=deleteProcessInstance('"+row.processInstanceId+"')>移除</a>&nbsp;&nbsp;<a href='javascript:void(0);' name='startBtn' onclick=processInstanceHistory('"+row.processInstanceId+"')>流程历史</a>";
				};

				//查看流程历史
				function processInstanceHistory(processInstanceId){ 
					var url = basePath+"/processInstanceHistory.jspx?processInstanceId="+processInstanceId;
					$.dialog({cancel:true,title:'流程图',content: 'url:'+url,name:'selectorFrame',id:'bbb',lock: true,parent:this,width: 1000, height: 500});
				} 
			</script>
</html>
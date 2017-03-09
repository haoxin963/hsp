<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>流程监控</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param value="v2" name="dialog"/>
		</jsp:include>
	</head>
	<body class="bodyLayout"> 
				<table id="dg" width="100%" height="100%"  border=false>
					<thead>
						<tr>
							<th field="defid" hidden="true">
								编号
							</th>
							<th field="defname" width="50">
								流程名称
							</th>
							<th field="startuser" width="50">
								创建人
							</th>
							<th field="starttime" width="30"  >
								开始时间
							</th>
							<th field="endtime" width="30"  >
								结束时间
							</th>
							<th width="15" data-options="field:'x',formatter:statusFormater">
								状态
							</th>
							<th width="30"  data-options="field:'opr',formatter:rowFormater">
								操作
							</th>
						</tr>
					</thead>
				</table>
				<script type="text/javascript"> 
						var basePath = '${path}/sys/wf';
						var dg;
						$(function(){
							var i=0;
							var listPath = basePath+"/finishedList.json";
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
						 
						function rowFormater(value,row,index)
						{ 
							return "<a href='javascript:void(0);' name='startBtn' onclick=loadFormByKey('"+row.bizid+"','"+row.jsontxt+"')>表单</a>&nbsp;&nbsp;<a href='javascript:void(0);' name='startBtn' onclick=processInstanceHistory('"+row.piid+"')>流程历史</a>";
						};
						
						function statusFormater(value,row,index)
						{ 
							return (row.endtime == null)  ? "流转中" : "结束"; 
						};
						
		 
						function processInstanceHistory(processInstanceId){ 
							var url = basePath+"/processInstanceHistory.jspx?processInstanceId="+processInstanceId;
							$.dialog({cancel:true,title:'流程历史',content: 'url:'+url,name:'selectorFrame',id:'bbb',lock: true,parent:this,width: 1000, height: 500});
						}
						
						function loadFormByKey(key,description){
								var descriptionJson = eval("(" + description + ")");
								var actionURL = "${path}/"+descriptionJson.formUrl+"?id="+key;
								 jQuery.dialog({
										width: 1000,
										height:500,
									    id: 'userinfoDialog', 
									    content: 'url:'+actionURL,
									    lock:true,
									    title:''
								});
						}
			</script>  
	</body>
</html>
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程处理</title>
<jsp:include page="/com/inc/incCssJs.jsp">
	<jsp:param value="v2" name="dialog" />
</jsp:include>
</head>
<body style="overflow-y: hidden" scroll="no">
 	<style>
 		.key{
 			width:100px;
 			text-align: right;
 			background-color: #fbfbfb;
 		}
 		td{
 			padding: 3px;
 		}
 	</style>
	<form class="inputForm">
		<table cellspacing="1" cellpadding="1" border="0"
			class="inputFormTable" width="100%">
			<tr>
				<td class="key">下一步骤${separator}</td>
				<td><span id="name"></span> </td>
			</tr>
			<tr style="display: none">
				<td class="key">执行人${separator}</td>
				<td><span id="assignee"><INPUT type="text"   id="assigneeInput" name="assigneeInput" onclick="showUser()" style="display: none"  placeholder="请选择下一步执行人"/></span></td>
			</tr>
			<tr style="display: none">
				<td class="key">候选执行人${separator}</td>
				<td><span id="candidateUsers"></span></td>
			</tr>
			<tr style="display: none">
				<td class="key">候选执行角色${separator}</td>
				<td><span id="candidateGroups"></span></td>
			</tr>
			
		</table>
	</form>
  
		 <jsp:include page="/com/inc/selector/setupSelector.jsp">
		 	<jsp:param name="cfg" value="{'nav':{'show':true},'fun':'nextTask','searchBar':'true','dialog':{'w':'800','h':'400','title':'选择'},'container':{'show':true},'reg':[{'type':'deptUser','title':'按部门','dft':'true'},{'type':'roleUser','title':'按角色'},{'type':'postUser','title':'按岗位'}]}"></jsp:param>
		 </jsp:include>
		 
	<script>
	  		var taskId = "${param.taskId}";
	  		
			function showUser(){ 
					 	nextTask(window);
			}
		
			var assigneeExp = "";
			var api = frameElement.api, w = api.opener;
			var innerWin = w.getWin();
			var keys = w.getVariables(innerWin);
			var datas = [];
			var doc = innerWin.document;
			for (var i = 0; i < keys.length; i++) {
				datas.push("m['" + keys[i] + "']="+ doc.getElementById(keys[i]).value);
			}
			
			function nextTaskBack(objs){   
				try{
				 var valueField = [];
				 var labelField = [];
				 for(var i=0;i< objs.length;i++){ 
					 valueField.push(objs[i].name)
					 labelField.push(objs[i].id)
				 }
				 $("#assigneeInput").val(valueField); 
				 if(assigneeExp!=""){
					 w.appendInput(assigneeExp,valueField,true);
				 }
				}catch(e){
					alert(e);
				}
				 return true;
			}

	</script>
	<script src="${vpath}/sys/wf/script/nextTask.js?a=<%=Math.random()%>"></script>
</body>
</html>
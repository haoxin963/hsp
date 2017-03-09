<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@page import="org.activiti.engine.task.Task"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.hsp.web.util.SpringCtx"%>
<%@page import="net.hsp.WorkFlowController"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param value="v2" name="dialog"/>
		</jsp:include> 
			<%@include file="/view/sys/wf/script.jsp" %>
			<script>
					function loadNextTask(){
							$.ajax({
							   type: "POST",
							   url: "${path}/sys/wf/nextTask.json?taskId=${taskId}",
							   data: "name=John&location=Boston",
							   success: function(msg){
							     //$("#next").html(msg);
							   }
							});
					}
 				
				var action = "next";
				function back(){
					var processId = "${param.processInstanceId }"; 
					  $.ajax({
				            url: '${path}/sys/wf/moveBack.json?processId='+processId, 
				            type: 'POST',
				            async:false,
				            dataType: "json",
				            success: function(result) {  
				              	window.location="${path}/sys/wf/assigneeList.jspx"
				            },
				            error: function(msg) {
				              	 
				            }
				        });
				}
				

				
				/**完成委派任务*/
				function resolveTask(){
					action = "resolveTask";
					var t = "wfResolveTask";
					var win =getWin();
					var doc = win.document; 
					var myform = win.document.getElementById(win.formId);
					if(!win.document.getElementById(t)){
						var v = document.createElement("input");
						v.type = "text";
						v.name = t;
						v.id = t;
						v.value = "true";
						myform.appendChild(v);
					}
					formSetup();		
					formSubmit();
				} 
				
				/**暂存表单*/
				function tempSave(){
					action = "tempSave";
					var win =getWin();
					var doc = win.document; 
					var myform = win.document.getElementById(win.formId);
					var taskId = doc.getElementById("taskId");
					if(taskId){
						myform.removeChild(taskId);
					}
					var wfKey = doc.getElementById("wfKey");
					if(wfKey){
						myform.removeChild(wfKey);
					}
					var resolveTask = doc.getElementById("wfResolveTask");
					if(resolveTask){
						myform.removeChild(resolveTask);
					}
					appendInput("processInstanceId","${param.processInstanceId}");	
					formSubmit();
				}
				
				function formSaveSuccessCallback(result){
					if(action == "tempSave"){
						$.dialog.tips('保存表单成功!');  
					}else if(action=="delegateTask"){
						$.dialog.tips('委派成功!');
					}else if(action=="resolveTask"){
						redirectToSuccess();
						//window.location = "${path}/sys/wf/assigneeList.jspx";
					}else if(action=="completeTask"){
						redirectToSuccess();
						//window.location = "${path}/sys/wf/assigneeList.jspx";
					}else{
						window.location = "${path}/sys/wf/assigneeList.jspx";
					}
				}
				
				function selectUserButtonBack(){
					var doc = userW.content.document;
					if(userW.content.$("#assigneeInput").val()=="" && userW.content.document.getElementById("assigneeInput").style.display!="none"){
						$.dialog.tips("未选择下一步执行者");
						return false;
					}

					completeTask();
				}
				
				/**完成任务*/
				function completeTask(){
					action = "completeTask";

				
					/**
					var doc = userW.content.document;
					var nextAssignee = doc.getElementById("assigneeInput").value;
					if(nextAssignee!=""){
						appendInput("assignee",nextAssignee);
					}else{
						appendInput("assignee","");
					}*/
					formSetup();		
					formSubmit();
				}
				
				var userW;
			    var bt = [{name: " 完成 ",callback:selectUserButtonBack,focus: true}];
				function completeTaskConfirm(){ 
					//var validate = getWin().formValidate();
					//if(!validate){
					//	return;
					//}
					var owner = "${task.owner}";
					var state = "${task.delegationState}";
					if(state == "PENDING" && owner !=""){
						resolveTask();
						return;
					} 
					userW = $.dialog({
						title:'下一步骤及处理人',
						width: 450,
						height:200,
					    content: 'url:${path}/sys/wf/nextTask.jspx?taskId=${requestScope.taskId}',
					    lock:true,
					    max:false,
					    button:bt
					});
				} 
				
				function delegateTask(){
						action = "delegateTask";
						delegate(window,{'k':'delegateUserName','v':'delegateUserId'});
				}
		 
			</script>
			  
		<jsp:include page="/com/inc/selector/setupSelector.jsp">
			<jsp:param name="cfg" value="{'nav':{'show':true},'fun':'delegate','searchBar':'true','dialog':{'w':'800','title':'选择'},'container':{'show':true},'reg':[{'type':'postUser','title':'部门岗位'},{'type':'deptUser','title':'按部门','default':'true'},{'type':'roleUser','title':'按角色'}]}"></jsp:param>
		</jsp:include>
			
		<script>
		  	function delegateBack(objs){ 
		  		alert(JSON.stringify(objs))
		  			var p = delegateParam;
			        var valueField = [];
			        var labelField = [];
			        for(var i=0;i< objs.length;i++){ 
			        	valueField.push(objs[i].name)
			        	labelField.push(objs[i].name)
			        }
			        if(labelField!=null && labelField.length>0)
				 	 $.ajax({
			            url: '${path}/sys/wf/delegateTask.json?taskId=${taskId}', 
			            type: 'POST',
			            data:"userName="+valueField[0],
			            async:false,
			            dataType: "json",
			            success: function(result) {  
			            	redirectToSuccess();
			            },
			            error: function(msg) {
			              	 
			            }
			        });
			        return true;
			}
		</script>
		
			<!--
					back() 退回 他人代办 
			 -->
			<div class="toolbarDiv" align="left">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="完成任务"></jsp:param>
					<jsp:param name="funs" value="completeTaskConfirm()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param>
					<jsp:param name="texts" value="保存表单"></jsp:param>
					<jsp:param name="funs" value="tempSave()"></jsp:param>
					<jsp:param name="ids" value="tempSaveBtn"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param>
					<jsp:param name="texts" value="委派"></jsp:param>
					<jsp:param name="funs" value="delegateTask()"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param>
				</jsp:include>
			</div>
			<div id="next"></div>
			<div id="processInfo" style="text-align:center;background: #DDDDDD;line-height: 40px;padding-left: 20px;font-family: 微软雅黑">
			<span style="font-size: 20px">${process.name }-${task.name }</span> 
			</div>
			<div id="taskProperty" style="height: 30px;background: #eeeeee;line-height: 30px;padding-right: 20px;text-align: right" >
				<b>当前步骤</b>${separator} <span style="color:#ff0000">${task.name }</span>&nbsp;&nbsp;
				<!-- 
				<b>优先级</b>:
				<c:choose>
					<c:when test="${task.priority <=30 }">低</c:when>
					<c:when test="${task.priority <=50 }">中</c:when>
					<c:when test="${task.priority <=100 }">高</c:when>
				</c:choose>&nbsp;&nbsp;
				<b>到期日</b>${separator}${empty task.dueDate ? '无' : task.dueDate}&nbsp;&nbsp;
				 -->
				<b>创建时间</b>${separator}<fmt:formatDate value="${task.createTime}"   type="both"/>
			</div>
			<iframe src="${path}${formUrl }?id=${bizKey }" id="workFlowFormIframe" height=100% width=100%></iframe>
			<script>
			
			function formAuth(){
				<%
				Task task = (Task)request.getAttribute("task");
				out.println("var desc = "+JSONObject.fromObject(task.getDescription()));
				%> 
				//黑名单 
				var win = getWin();
				 var  x=win.$("#"+win.formId).serializeArray(); 
				  $.each(x, function(i, field){
					  if(field.name != "wfComment"){
						  var f = win.$("#"+field.name);
						  f.prop("readonly", true);
						  f.css("background","#eeeeee");
						  f.focus(function(){
							  this.defaultIndex=this.selectedIndex;
							});
						  f.change(function(){
							  this.selectedIndex=this.defaultIndex;
						  });
					  } 
				  });
				  
				  //白名单 
				  for(var i=0;i<desc.include.length;i++){
					  var f = win.$("#"+desc.include[i]);
					  f.prop("readonly", false);
					  f.css("background","#ffffff"); 
					  f.unbind();
				  } 
			}
			</script>  
 
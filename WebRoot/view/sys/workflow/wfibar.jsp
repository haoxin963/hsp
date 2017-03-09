<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*" trimDirectiveWhitespaces="true"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String formId =  "wfform";
	String customFormId = request.getParameter("formId");
	if(null!=customFormId){
		formId = customFormId;
	}
	String titleId = request.getParameter("titleId");
 %>
<script>
	var formId = '<%=formId%>';
<% 
	String saveFunc = request.getParameter("saveFunc");
	if(null == saveFunc){
%>
	function doInit(){
		var wfTitle = $('#<%=titleId%>').val();
		var actionPath ='${path}/sys/workflow/initialize.do?workflowName=${workflow.workflowName}&actionId=${workflow.actionId}&wfTitle='+wfTitle;
		if($("#"+formId).valid()){
			$('#'+formId).submit(); 
		}
	}
<%
	}else{
%>
	function <%=saveFunc%>(obj){
		var wfTitle = $('#<%=titleId%>').val();
		var actionPath ='${path}/sys/workflow/initialize.json?workflowName=${workflow.workflowName}&actionId=${workflow.actionId}&wfTitle='+wfTitle;
		if($("#"+formId).valid()){
			$("#"+formId).form({
				url :actionPath,
				onSubmit: function(param){
				},
				success:function(result){
					result = jQuery.parseJSON(result);
					if(result.status == 1){
						 parent.jQuery.messager.alert(constants.alertTitle,constants.success);
						 top.ymPrompt.close();
						 if(obj){
							obj.datagrid('reload');
						 }
					}else{
						jQuery.messager.alert(constants.alertTitle,result.msg);
					}
				}
			}); 
			
			
			$('#'+formId).submit(); 
		}
	}
<%
	}
%>
</script>
<% 
	if(null == saveFunc){
%>
<div
	style="padding: 0px; border: 0px solid #ddd; background-color: #E0ECFF">
	<a href="#" onclick="doInit()" class="easyui-linkbutton"
			data-options="plain:true,iconCls:'icon-reload'">保存</a>
</div>
<%
	}
%>


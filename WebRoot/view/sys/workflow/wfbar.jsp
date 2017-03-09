<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" trimDirectiveWhitespaces="true"   pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");
	String formId =  "wfform";
	String customFormId = request.getParameter("formId");
	if(null!=customFormId){
		formId = customFormId;
	}
 %>
<script>
	var formId = '<%=formId%>';
	function doAction(id){
		var actionPath ='${path}/sys/workflow/doAction.json?entryId=${workflow.entryId}&actionId='+id;
		if($("#"+formId).valid()){
			$("#"+formId).form({
				url :actionPath,
				onSubmit: function(param){
					 param.chooseNextUsers = $('#chooseNextUsers').val();
				},
				success:function(result){
					result = jQuery.parseJSON(result);
					console.log(result);
					if(result.status == 1){
						jQuery.messager.alert(constants.alertTitle,constants.success);
						window.location.href ='${path}/sys/workflow/listToDo.do';
					}else{
						jQuery.messager.alert(constants.alertTitle,result.msg);
					}
				}
			}); 
			
			
			$('#'+formId).submit(); 
		}
	}
</script>
<div style="padding: 0px; border: 0px solid #ddd; background-color: #E0ECFF">
	<c:forEach var="action" items="${actions}">
	<a href="#" onclick="doAction(${action.id})" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">${action.name}</a>
	</c:forEach>
	&nbsp;&nbsp;
	<c:if test="${true == allowChooseNextUsers}">
	下一步执行者<input type="text" id="chooseNextUsers">
	</c:if> 
</div>

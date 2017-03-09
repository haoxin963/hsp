<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>菜单收藏</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>

	<body class="bodyLayout">
		<div class="toolbarDiv">
			<jsp:include page="/com/inc/toolbar.jsp"></jsp:include>
		</div>
		<form class="inputForm"
			action="${path}/sys/widget/favorite/doSave.json" id="favoriteForm"
			modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<input type="hidden" name="id" value="${command.id}">
					<input type="hidden" name="userId" value="${command.userId}">
					<input type="hidden" name="sortNo" value="${command.sortNo}">
					<input type="hidden" name="functionId" value="${command.functionId}">
					<td class="lableTd">
						菜单原名 ${separator}
					</td>
					<td class="valueTd">
						${command.functionName}
					</td>
				</tr>
				<tr>
					<td class="lableTd">
						菜单图标 ${separator}
					</td>
					<td class="valueTd">
					<input type="text" name="iconPath" value="${command.iconPath}" maxlength='100'>
					</td>
				</tr>
				<tr>
					<td class="lableTd">
						菜单别名 ${separator}
					</td>
					<td class="valueTd">
					<input type="text" name="aliasName" value="${command.aliasName}" maxlength='100'>
					</td>
				</tr>
			</table>
		</form>
	</body>

<script> 
	var serverURL = "${path}/sys/widget/favorite";
	var formId = "favoriteForm";
	 
	function doSave(obj){ 
		if($("#"+formId).valid()){
			$("#"+formId).form({
				onSubmit: function(){
				},
				success:function(result){
					 result = jQuery.parseJSON(result);
					 if(result.status == "1"){
					 	obj.formSaveSuccessCallback(result);
					}else{
						top.jQuery.messager.alert(constants.alertTitle,result.msg);
					}
				}
			}); 
			$('#'+formId).submit(); 
		}
	} 
	$().ready(function(){
		var v = $("#"+formId).validate();
	});
</script>
</html>
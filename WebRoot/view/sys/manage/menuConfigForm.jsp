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
		<title>权限表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>
	
	<body class="bodyLayout"> 			 
		<f:form class="inputForm"
			action="${path}/sys/manage/station/doFunctionSave.json" id="functionForm"
			modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd">
						<span class='required'>*</span> 功能名称 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="functionName" required='true'/>
					</td>
					<td class="lableTd">
						父节点 ${separator}
					</td>
					<td class="valueTd">			
						<input type="text" name="parentName" value="${parentFun.functionName}" readonly />
						<input type="hidden" name="parentId" value="${parentFun.functionId}"/>
						<input type="hidden" name="custDomain" value="${custDomain}"/>									
					</td>
				</tr>				
				<tr>
					<td class="lableTd">
						功能地址 ${separator}
					</td>
					<td class="valueTd" colspan="3">
						<f:input path="linkAddress" maxlength='255' style="width:570px;" />
					</td>
					</tr>
				<tr>
					<td class="lableTd">
						内联地址 ${separator} 
					</td>
					<td class="valueTd" colspan="3">
						<f:textarea path="innerUrl" maxlength='500' style="width:570px;height:50px;" title="此处请填写当前功能地址里依赖的一些URL，多个地址请用半角逗号分隔" />
						
					</td>
				</tr>
				<tr>					
					<td class="lableTd">
						按钮ID ${separator}
					</td>
					<td class="valueTd">
						<f:input path="buttonId" maxlength='30' />
					</td>
					<td class="lableTd">
						是否按钮 ${separator}
					</td>
					<td class="valueTd">
						<f:select path="tag">
							<f:option value="f">否</f:option>
							<f:option value="b">是</f:option>
						</f:select>						 
					</td>					 
				</tr>
				<tr>					
					 
					
					<td class="lableTd">
						是否显示 ${separator}
					</td>
					<td class="valueTd">
						<f:select path="type">
							<f:option value="1">是</f:option>
							<f:option value="0">否</f:option>
						</f:select>						 
					</td>
					<td class="lableTd">
						Icon地址 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="pictureAddr" maxlength='255' title="菜单或者按钮的图标"/>
					</td>				 
				</tr>
				<f:hidden path="functionId" required='true' maxlength='255' />
				<f:hidden path="flag" maxlength='1' />
				<f:hidden path="child" maxlength='1' />
				<f:hidden path="sortNo" integer='true' maxlength='255' />
			</table>
		</f:form>
	</body>

	<script> 
			var formId = "functionForm";
			 
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

			$().ready(function() { 
				var v = $("#"+formId).validate(); 
			});
		</script>
</html>
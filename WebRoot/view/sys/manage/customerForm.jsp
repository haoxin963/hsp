<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>客户</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>




	<body class="bodyLayout">
		<f:form class="inputForm" action="${path}/sys/manage/customer/doSave.json" id="customerForm" modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0" class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd">
						<f:hidden path="custId" required='true' number='true' maxlength='255' />
						<span class='required'>*</span>客户名称 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="custName" maxlength='30' required='true' cssStyle="width:350px" />
					</td>
				</tr>
				<tr>

					<td class="lableTd">
						地址 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="address" maxlength='255' cssStyle="width:350px" />
					</td>
				</tr>

				<tr>
					<td class="lableTd">
						电话 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="phone" maxlength='30' />
					</td>
				</tr>
				<tr>
					<td class="lableTd">
						<span class='required'>*</span> 状态 ${separator}
					</td>
					<td class="valueTd"> 
						<f:select path="status">
							<f:option value="0">启用</f:option>
							<f:option value="1">禁用</f:option>
						</f:select>
					</td>
				</tr>

			</table>
		</f:form>
	</body>

	<script> 
			var serverURL = "${path}/sys/manage/customer";
			var formId = "customerForm";
			 
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
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>站点</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>




	<body class="bodyLayout">
		<f:form class="inputForm" action="${path}/sys/manage/station/doSave.json" id="stationForm" modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0" class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd">
						<span class='required'>*</span>所属客户${separator}
					</td>
					<td class="valueTd">
						<f:hidden path="custId" id="custId" number='true' maxlength='255' />
						<jsp:include page="/com/inc/selector.jsp">
							<jsp:param name="type" value="1" />
							<jsp:param name="required" value="true" />
							<jsp:param name="initValue" value="${ command.custId eq 0 ? '' : command.custId }"></jsp:param>
							<jsp:param name="dialogTitle" value="请选择" />
							<jsp:param name="targetId" value="custId" />
							<jsp:param name="valueField" value="custId" />
							<jsp:param name="labelField" value="custName" />
							<jsp:param name="singleSelect" value="true" />
							<jsp:param name="convertDataSource" value="/sys/manage/customer/doLoad.json" />
							<jsp:param name="dataPage" value="/sys/manage/customer/doList.do"></jsp:param>
						</jsp:include>
					</td>
				</tr>
				
				<tr>
					<f:hidden path="statId" required='true' maxlength='48' />
				 
					<td class="lableTd">
						<span class='required'>*</span>站点全称 ${separator}
					</td>
					<td class="valueTd">
						<f:input path="statName" maxlength='50' cssStyle="width:350px" />
					</td>
				</tr> 
				<c:if test="${empty command.statId}">
					<tr>
						<td class="lableTd">
							<span class='required'>*</span>域名 ${separator}
						</td>
						<td class="valueTd">
							<f:input path="domainAddress" maxlength='100' cssStyle="width:350px" title="如dev0" />
						</td>
					</tr>
				</c:if>
			</table>
		</f:form>
	</body>

	<script> 
			var serverURL = "${path}/sys/manage/station";
			var formId = "stationForm";
			 
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
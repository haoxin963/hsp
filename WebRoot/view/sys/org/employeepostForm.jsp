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
<title>员工岗位信息</title>
<jsp:include page="/com/inc/incCssJs.jsp">
	<jsp:param name="com" value="core"></jsp:param>
</jsp:include>
</head>




<body class="bodyLayout">
	<div class="toolbarDiv">
		<jsp:include page="/com/inc/toolbar.jsp"></jsp:include>
	</div>
	<f:form class="inputForm"
		action="${path}/sys/org/employeepost/doSave.json"
		id="employeepostForm" modelAttribute="command" method="POST">
		<table cellspacing="1" cellpadding="1" border="0"
			class="inputFormTable" width="100%">
			<tr>
				<f:hidden path="id" required='true' integer='true' maxlength='255' />
				<td class="lableTd"><span class='required'>*</span> 工员ID
					${separator}</td>
				<td class="valueTd"><f:input path="empid" required='true'
						maxlength='255' /></td>

				<td class="lableTd"><span class='required'>*</span> 岗位ID
					${separator}</td>
				<td class="valueTd"><f:input path="postId" required='true'
						maxlength='255' /></td>
			</tr>
			<tr>

				<td class="lableTd"><span class='required'>*</span> 是否主要岗位
					${separator}</td>
				<td class="valueTd"><f:input path="isMainPost" required='true'
						maxlength='1' /></td>

				<td class="lableTd"><span class='required'>*</span> 删除标记
					${separator}</td>
				<td class="valueTd"><f:input path="delTag" required='true'
						maxlength='1' /></td>
			</tr>


		</table>
	</f:form>
</body>

<script>
	var serverURL = "${path}/sys/org/employeepost";
	var formId = "employeepostForm";

	function doSave(obj) {
		if ($("#" + formId).valid()) {
			$("#" + formId).form(
					{
						onSubmit : function() {

						},
						success : function(result) {
							result = jQuery.parseJSON(result);
							if (result.status == "1") {
								obj.formSaveSuccessCallback(result);
							} else {
								top.jQuery.messager.alert(constants.alertTitle,
										result.msg);
							}
						}
					});
			$('#' + formId).submit();
		}
	}

	$().ready(function() {
		var v = $("#" + formId).validate();
	});
</script>
</html>
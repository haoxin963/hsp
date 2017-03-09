<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
		<title>TaskEntity</title>
	<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
	</head>
	<body class="bodyLayout">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
		<f:form class="layui-form" action="${path}/sys/schedule/addTask.json"
			id="taskForm" modelAttribute="command" method="POST">
					<table class="table_one">
				<tr>
				<td>
				    <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
						   类名 ${separator}</label>
						<div class="layui-input-block">
							<input type="text" name="taskClass"  maxlength='50' autocomplete="off"
								placeholder="请输入类名" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				</tr>
			</table>
		</f:form>
	</body>
	<script> 
			var serverURL = "${path}/sys/schedule/addTask";
			var formId = "taskForm";
			 
			function doSave(obj){ 
				if (valid()) {
					$("#" + formId).ajaxSubmit({
						success : function(result) {
							result = jQuery.parseJSON(result);
							if (result.status == "1") {
								obj.closeAndRef();
							} else {
								alertError(result.msg);
							}
						}
					});
				}
			} 
		</script>
</html>
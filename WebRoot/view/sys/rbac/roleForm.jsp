<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>角色表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>




<body>
	<f:form class="layui-form" action="${path}/sys/rbac/role/doSave.json"
		id="roleForm" modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<td><f:hidden path="id" required='true' number='true'
						maxlength='255' /> <f:hidden path="delTag" maxlength='1' /> <f:hidden
						path="wayMark" maxlength='255' />
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							角色名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="roleName" maxlength='50' autocomplete="off"
								placeholder="请输入角色名" class="layui-input" lay-verify="required" />
						</div>
					</div></td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							角色标记${separator}</label>
						<div class="layui-input-block">
							<f:input path="role" maxlength='50' autocomplete="off"
								placeholder="请输入角色标记" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
		</table>
	</f:form>
</body>

<script>
	var serverURL = "${path}/sys/rbac/role";
	var formId = "roleForm";

	function doSave(obj) {
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
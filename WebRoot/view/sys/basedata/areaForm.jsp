<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>区域</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>




<body class="bodyLayout">
	<f:form class="layui-form"
		action="${path}/sys/basedata/area/doSave.json" id="areaForm"
		modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<f:hidden path="areaId" required='true' maxlength='48' />
				
				<td>
				       <div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							区域名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="areaName"  autocomplete="off" maxlength='100' 
								placeholder="请输入区域名称" class="layui-input" lay-verify="required"  />
						</div>
					</div>
				</td>
				<td>
				         <div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							父节点 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="parentId"  autocomplete="off"  maxlength='48' value="${param.parentId}" readonly="true" 
								placeholder="请输入区域名称" class="layui-input" lay-verify="required"  />
						</div>
					</div>
				
				
				 </td>



			</tr>


		</table>
	</f:form>
</body>

<script>
var formId = "areaForm";
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
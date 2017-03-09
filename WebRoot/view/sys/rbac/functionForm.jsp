<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>

<body class="bodyLayout">
	<f:form class="layui-form"
		action="${path}/sys/rbac/function/doSave.json" id="functionForm"
		modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							功能名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="functionName" maxlength='50' autocomplete="off"
								placeholder="请输入功能名称" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 父节点${separator}</label>
						<div class="layui-input-block">
							<c:if test="${empty parentId}">
								<input name="parentName" autocomplete="off"
									value="${parentObj.functionName}" class="layui-input" readonly />
								<input type="hidden" name="parentId" value="${command.parentId}" />
							</c:if>
							<c:if test="${!empty parentId}">
								<input name="parentName" autocomplete="off"
									value="${parentName}" class="layui-input" readonly />
								<input type="hidden" name="parentId" value="${parentId}" />
							</c:if>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="layui-form-item">
						<label class="layui-form-label"> 功能地址 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="linkAddress" autocomplete="off"
								placeholder="请输入功能地址" class="layui-input" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="layui-form-item">
						<label class="layui-form-label"> 内联地址 ${separator}</label>
						<div class="layui-input-block">
							<f:textarea path="innerUrl" autocomplete="off"
								placeholder="此处请填写当前功能地址里依赖的一些URL，多个地址请用半角逗号分隔" class="layui-textarea" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 按钮ID ${separator}</label>
						<div class="layui-input-block">
							<f:input path="buttonId" autocomplete="off" placeholder="请输入按钮ID"
								class="layui-input" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 是否按钮 ${separator}</label>
						<div class="layui-input-block">
							<f:select path="tag">
								<f:option value="f">否</f:option>
								<f:option value="b">是</f:option>
							</f:select>
						</div>
					</div>
				</td>

			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 是否显示 ${separator}</label>
						<div class="layui-input-block">
							<f:select path="type">
								<f:option value="1">是</f:option>
								<f:option value="0">否</f:option>
							</f:select>
						</div>
					</div>
				</td>

				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">Icon ${separator}</label>
						<div class="layui-input-block">
							<f:input path="pictureAddr" autocomplete="off"
								placeholder="请输入Icon" class="layui-input" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">sortNo${separator}</label>
						<div class="layui-input-block">
							<f:input path="sortNo" autocomplete="off" placeholder="请输入序号"
								class="layui-input" />
						</div>
					</div>
				</td>
			</tr>
			<f:hidden path="functionId" required='true' maxlength='255' />
			<f:hidden path="flag" maxlength='1' />
			<f:hidden path="child" maxlength='1' />
		</table>
	</f:form>
</body>

<script>
	var serverURL = "${path}/sys/rbac/function";
	var formId = "functionForm";

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
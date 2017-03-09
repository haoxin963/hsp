<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>字典</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<style>
.cccc{
    margin-left:250px;
    margin-top:-38px;
}
</style>
</head>
<body>

	<br>
	<f:form class="layui-form" action="${path}/sys/dic/dictionary/save.json"
		id="dicForm" modelAttribute="command" method="POST">
			<input type="hidden" name="typeCode"
							value="${command.dicType.typeCode}">
						<input type="hidden" name="dicTypeId"
							value="${command.dic.dicTypeId}">
						<input type="hidden" name="dictId" value="${command.dic.dictId}">
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							字典代号 ${separator}</label>
						<div class="layui-input-block">
							<input type="text" name="dicCode"  autocomplete="off"
								placeholder="字典代号 " class="layui-input" lay-verify="required"  value="${command.dic.dicCode}"/>
						</div>
					</div>
				</td>
				<td >
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>字典名称
							${separator}</label>
						<div class="layui-input-block">
							<input type="text" name="dictName" value="${command.dic.dictName}"  autocomplete="off"
								placeholder="请输入字典名称" class="layui-input"
								lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
		    <c:forEach var="extField" items="${command.dicType.extFields}">
					<c:if test="${extField.display}">
						<tr>
							<td  colspan="2">
					<div class="layui-form-item">
						<label class="layui-form-label">${extField.fieldName}
							${separator}</label>
						<div class="layui-input-block">
							<input type="text" name="${extField.extField}" value="${command.dic[extField.extField]}" autocomplete="off"  
								class="layui-input"  style="width:240px;"/><div class="cccc">${extField.description}</div>
						</div>
					</div>
				</td>
						</tr>
					</c:if>
					<c:if test="${!extField.display}">
						<input type="hidden" name="${extField.extField}"
							value="${command.dic[extField.extField]}">
					</c:if>
				</c:forEach>
		</table>
</f:form>
	<script>
	
	<c:forEach var="extField" items="${command.dicType.extFields}">
	<c:if test="${extField.display && extField.validation != null && extField.validation != ''}">
	$.validator.addMethod('${extField.extField}', function(value, element) {       
	     return ${extField.validation}.test(value);       
	 }, '${extField.errorMsg}');   
	</c:if>
    </c:forEach>
	$(function(){
		$('.dicForm').validate({
			rules:{
				<c:forEach var="extField" items="${command.dicType.extFields}">
				<c:if test="${extField.display && extField.validation != null && extField.validation != ''}">
				${extField.extField}:{
					${extField.extField}:true
				},
				</c:if>
				</c:forEach>
			},
		});
	});
		var formId = "dicForm";
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

</body>
</html>
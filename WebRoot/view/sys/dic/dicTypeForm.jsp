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
</head>
<body>

	<br>
	<f:form class="layui-form" action="${path}/sys/dic/dicType/save.json"
		id="dicTypeForm" modelAttribute="command" method="POST">
		<f:hidden path="dicTypeId" />
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							类型代号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="typeCode"  autocomplete="off"
								placeholder="请输入类型代号" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>类型名称
							${separator}</label>
						<div class="layui-input-block">
							<f:input path="dicTypeName"  autocomplete="off"
								placeholder="请输入类型名称" class="layui-input"
								lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							是否显示 ${separator}</label>
						<div class="layui-input-block">
							 	<f:radiobutton path="state" value="1" title="是" />
							    <f:radiobutton path="state" value="0" title="否" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
				
						<div class="layui-input-block">
							<input type="button" id="btnAddExt"
					                  value="添加扩展字段"  class="layui-input"/>
						</div>
					</div>
				</td>
			</tr>
			<c:forEach var="extField" items="${command.extFields}"
				varStatus="status">
			     <tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>
							扩展字段 ${separator}</label>
						<div class="layui-input-block">
						    <f:hidden path="extFields[${status.index}].extField" />
						    <f:hidden path="extFields[${status.index}].dicTypeId" />
							<f:input path="extFields[${status.index}].fieldName"  autocomplete="off"
								placeholder="请输入扩展字段" class="layui-input" lay-verify="required" />
							<f:radiobutton path="extFields[${status.index}].display" value="1" /> 启用 
							<f:radiobutton path="extFields[${status.index}].display" value="0" /> 废弃
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>验证规则
							${separator}</label>
						<div class="layui-input-block">
							<f:input path="extFields[${status.index}].validation"  autocomplete="off"
								placeholder="请输入验证规则" class="layui-input"
								lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>规则说明
							${separator}</label>
						<div class="layui-input-block">
							<f:input path="extFields[${status.index}].description"  autocomplete="off"
								placeholder="请输入规则说明" class="layui-input"
								lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
					
				
					<div class="layui-form-item">
						<label class="layui-form-label"><span class="required">*</span>出错提示
							${separator}</label>
						<div class="layui-input-block">
							<f:input path="extFields[${status.index}].errorMsg"  autocomplete="off"
								placeholder="请输入出错提示" class="layui-input"
								lay-verify="required" />
						</div>
					</div>
					</div>
				</td>
			</tr>
			</c:forEach>
		</table>


	</f:form>
	<script>
	var extNums = ${fn:length(command.extFields)};
	$(function() {
	    $('#btnAddExt').click(function() {
				var extFieldTbl = '<tr><td><div class="layui-form-item"><label class="layui-form-label"><span class="required">*</span>扩展字段 ${separator}</label>';
				extFieldTbl+='<div class="layui-input-block"><input type="text" name="extFields['+extNums+'].fieldName"  autocomplete="off" placeholder="请输入扩展字段" class="layui-input" lay-verify="required" /></div></div></td><td>';
				extFieldTbl+='<div class="layui-form-item"><label class="layui-form-label"><span class="required">*</span>验证规则${separator}</label><div class="layui-input-block"><input type="text" name="extFields['+extNums+'].validation"  autocomplete="off"placeholder="请输入验证规则" class="layui-input" lay-verify="required" />';
				extFieldTbl+='</div></div></td></tr><tr><td><div class="layui-form-item"><label class="layui-form-label"><span class="required">*</span>规则说明${separator}</label><div class="layui-input-block"><input type="text" name="extFields['+extNums+'].description"  autocomplete="off"placeholder="请输入规则说明" class="layui-input"	lay-verify="required" /></div>';
				extFieldTbl+='</div></td><td><div class="layui-form-item"><label class="layui-form-label"><span class="required">*</span>出错提示${separator}</label><div class="layui-input-block"><input type="text" name="extFields['+extNums+'].errorMsg"  autocomplete="off"placeholder="请输入出错提示" class="layui-input"lay-verify="required" />';
				extFieldTbl+='</div></div></div></td></tr>';
				$('.table_one').append(extFieldTbl);
				extNums++;
			});
        });
	
		var formId = "dicTypeForm";
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
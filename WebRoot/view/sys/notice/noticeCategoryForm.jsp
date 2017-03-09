<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
		<title>pubmodule_noticecategory_tbl</title>

	</head>
	<body >
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
		<f:form class="layui-form"
			action="${path}/sys/notice/noticeCategory/doSave.json"
			id="noticeCategoryForm" modelAttribute="command" method="POST">
			<table class="table_one">
				<tr>
				<f:hidden path="id" required='true' integer='true' maxlength='255' />
				<f:hidden path="parentId" required='true' integer='true' maxlength='255' />
				<f:hidden path="delTag" maxlength='255' />
				   <td>
				         <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							 上级栏目 ${separator}</label>
						<div class="layui-input-block">
						    ${pName}
						</div>
					</div>
				   </td>
				   <td></td>
				</tr>
				<tr>
				   <td>
				       <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							代号 ${separator}</label>
						<div class="layui-input-block">
						<c:if test="${isEdit}">
							<f:input path="code" id="code" maxlength='50' autocomplete="off"
								placeholder="请输入代号" class="layui-input" lay-verify="required"  onBlur="checkCode()" />
						</c:if>
						
						<c:if test="${!isEdit}">
							<f:input path="code" id="code" autocomplete="off" maxlength='50' 
								placeholder="请输入代号" class="layui-input" lay-verify="required" />
						</c:if>
						</div>
					</div>
				</td>
				<td>${command.code}</td>
				</tr>
				<tr>
				    	<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="name" maxlength='50' autocomplete="off"
								placeholder="请输入名称" class="layui-input" lay-verify="required" />
						</div>
					</div>
					</td>
					<td></td>
				</tr>
				<tr>
				    <td>
					<div class="layui-form-item">
						<label class="layui-form-label">
							备注 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="remark" maxlength='200' autocomplete="off"
								class="layui-input"  />
						</div>
					</div>
					</td>
					<td></td>
				</tr>
			</table>
		</f:form>
	</body>
	<script> 
	
	function checkCode(){
		var code = $("#code").val();
		$.post("${path}/sys/notice/noticeCategory/doCheckCode.json?code="+code, function(data) {
			var ret = JSON.parse(data);
			if(ret.command == true){
				alert("代码已经存在，请调整！");
				$("#code").focus();
				return false;
			}
			return true;
		});
	}
	
		var serverURL = "${path}/sys/notice/noticeCategory";
		var formId = "noticeCategoryForm";
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
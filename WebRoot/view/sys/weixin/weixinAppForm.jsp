<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<title>微信</title>
		<jsp:include page="/resource/inc/incMeta.jsp" />
        <jsp:include page="/resource/inc/incCssJs.jsp" />
		
	</head>




	<body class="bodyLayout">
		<f:form class="layui-form" action="${path}/sys/weixin/weixinApp/doSave.json" id="weixinAppForm" modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0" class="inputFormTable" width="100%">
				<tr>
					<f:hidden path="id" required='true' integer='true' maxlength='255' />
					<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							appId${separator}</label>
						<div class="layui-input-block">
							<f:input path="appId" maxlength='32' autocomplete="off"
								placeholder="请输入appId" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
					
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							secret${separator}</label>
						<div class="layui-input-block">
							<f:input path="secret" maxlength='32' autocomplete="off"
								placeholder="请输入secret" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>

				
				</tr>
				<tr>
                   	<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							所属站点${separator}</label>
						<div class="layui-input-block">
							<f:input path="custId" maxlength='32' autocomplete="off"
								placeholder="请输入所属站点" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				  	<td>
					<div class="layui-form-item">
						<label class="layui-form-label">
							微信用户名${separator}</label>
						<div class="layui-input-block">
							<f:input path="userName" maxlength='32' autocomplete="off"
								placeholder="请输入微信用户名" class="layui-input" />
						</div>
					</div>
				</td>
			

				
				</tr>
				<tr>
                      	<td>
					<div class="layui-form-item">
						<label class="layui-form-label">
							微信密码${separator}</label>
						<div class="layui-input-block">
							<f:input path="pwd" maxlength='32' autocomplete="off"
								placeholder="请输入微信密码" class="layui-input" />
						</div>
					</div>
				</td>
				
                 	<td>
					<div class="layui-form-item">
						<label class="layui-form-label">
							二维码${separator}</label>
						<div class="layui-input-block">
							<f:input path="barcode" maxlength='32' autocomplete="off"
								placeholder="请输入二维码" class="layui-input" />
						</div>
					</div>
				</td>
					
				</tr>
				<tr>

					<td class="lableTd">
						 
					</td>
					<td class="valueTd">
						<f:hidden path="delTag" maxlength='1' />
					</td>
					<td colspan="2"></td>
				</tr>

			</table>
		</f:form>
	</body>

	<script> 
			var serverURL = "${path}/sys/weixin/weixinApp";
			var formId = "weixinAppForm";
			 
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
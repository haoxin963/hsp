<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>用户表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>
<body>

	<f:form class="layui-form" action="${path}/sys/rbac/user/doSave.json"
		id="userForm" modelAttribute="command" method="POST">
		<f:hidden path="id" required='true' number='true' maxlength='255' />
		<f:hidden path="delTag" maxlength='1' />
		<f:hidden path="status" maxlength='1' />
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							用户名 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="userName" maxlength='50' autocomplete="off"
								placeholder="请输入用户名" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>密码
							${separator}</label>
						<div class="layui-input-block">
							<c:if test="${!empty command.id}">
								<input type="password" id="password" name="password"
									value="${command.password}" readonly class="layui-input" />
							</c:if>
							<c:if test="${empty command.id}">
								<input type="text" id="password" name="password" value=""
									lay-verify="password" required='true' autocomplete="off"
									placeholder="请输入密码" class="layui-input" />
							</c:if>
						</div>
					</div>
				</td>
			</tr>

			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							真实姓名 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="trueName" required='true' lay-verify="trueName"
								maxlength='100' placeholder="请输入真实姓名" class="layui-input" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">性别 ${separator}</label>
						<div class="layui-input-block">
							<f:radiobutton path="sex" value="1" title="男" />
							<f:radiobutton path="sex" value="0" title="女" />


						</div>
					</div>
				</td>
			</tr>

			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">移动电话 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="mobileTelephone" maxlength='100'
								placeholder="移动电话" lay-verify="mobileTelephone"
								class="layui-input" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">移动电话是否公开 ${separator}</label>
						<div class="layui-input-block">
							<f:radiobutton path="isPublicMobileTelephone" value="1" title="是" />
							<f:radiobutton path="isPublicMobileTelephone" value="0" title="否" />

						</div>
					</div>
				</td>
			</tr>

			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">E-mail ${separator}</label>
						<div class="layui-input-block">
							<f:input path="email" lay-verify="email" maxlength='100'
								email="true" placeholder="邮箱" class="layui-input" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label">登录后需要修改初始密码 ${separator}</label>
						<div class="layui-input-block">
							<c:if test="${empty command.id}">
								<f:hidden path="isUpdatePass" maxlength='1' />
								<input type="radio" name="isuppwd" value="1" title="是" />
								<input type="radio" name="isuppwd" value="0" checked="checked"
									title="否" />
							</c:if>
							<c:if test="${not empty command.id}">
								<f:input path="isUpdatePass" class="layui-input" />
								<input type="radio" name="isuppwd" value="1" disabled="true"
									title="是"
									${command.isUpdatePass=='1'||command.isUpdatePass=='2'?'checked':''} />
								<input type="radio" name="isuppwd" value="0" disabled="true"
									title="否"
									${command.isUpdatePass==''||command.isUpdatePass==null||command.isUpdatePass=='0'?'checked':''} />
							</c:if>
						</div>
					</div>
				</td>
			</tr>
		</table>


	</f:form>
	<script>
		var serverURL = "${path}/sys/rbac/user";
		var formId = "userForm";

		function formVerifyExt() {
			//自定义验证规则
			form
					.verify({
						password : [ /(.+){6,12}$/, '密码必须6到12位' ],
						trueName : function(value) {
							if (value.length < 2) {
								return '用户名至少得2个字符啊';
							}
						},
						mobileTelephone : function(value) {
							if (value.length > 0) {
								if (!form.config.verify["phone"][0].test(value)) {
									return form.config.verify["phone"][1];
								}
							}
						},
						email : function(value) {
							if (value.length > 0) {
								var emailVf = [
										/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
										'邮箱格式不正确' ];
								if (!emailVf[0].test(value)) {
									return emailVf[1];
								}
							}
						}
					});
		}

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
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>批量生成用户</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<style type="text/css">
.inputForm fieldset {
	font-family: "微软雅黑";
	border: 1px solid #E6E6E6;
	background-color: #FFFFFF;
	margin-top: 15px;
}

.inputForm fieldset legend {
	font-size: 15px;
}

.lableTd {
	width: 18%;
}

fieldset ul {
	list-style: none;
}

ul li {
	height: 25px;
	line-height: 25px;
}
</style>
</head>

<body>
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
	<form class="layui-form"
		action="${path}/sys/org/employee/doBatchCreateUser.json"
		id="createUserForm" method="POST">
		<input type="hidden" name="ids" value="${requestScope.ids}" />
		<fieldset>
			<legend>用户名</legend>
			<table class="table_one">
				<tr>
					<td><input type="radio" id="asempno" name="username"
						value="1" checked title="根据工号生成用户名" /></td>
				</tr>
				<tr>
					<td><input type="radio" id="asempname" name="username"
						value="2" title="根据姓名生成用户名" /></td>
				</tr>
				<tr>
					<td><input type="radio" id="asempname" name="username"
						value="4" title="根据姓名全拼生成用户名" /></td>
				</tr>
				<tr>
					<td><input type="radio" id="astel" name="username" value="3"
						title="根据手机号生成用户名(如果没有手机则根据工号生成)" /></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>密码</legend>

			<table class="table_one">
				<tr>
					<td colspan=2><input type="radio" id="defnumber"
						name="password" value="1" checked title="123456" /></td>
				</tr>
				<tr>
					<td colspan=2><input type="radio" id="pidnum" name="password"
						value="2" title="身份证后6位(没有则按照123456)" /></td>
				</tr>
				<tr>
					<td style="width:140px;"><input type="radio" id="txtin"
						name="password" value="3" title="录入密码" /></td>
					<td><input type="text" id="defined_pwd" name="defined_pwd"
						class="layui-input" /></td>
				</tr>
			</table>

		</fieldset>
	</form>
</body>

<script> 
			var serverURL = "${path}/sys/org/employee";
			var formId = "createUserForm";
			var ids = "${param.ids}";
			function doSave(win){
				//用户名
				var $username = $("input:radio[name='username']:checked");
				
				//密码
				var $pasType = $("input:radio[name='password']:checked");
				var password;
				if($pasType.val()=='1'){
					password = "123456";
				}
				if($pasType.val()=='2'){
					password = "PID";
				}
				if($pasType.val()=='3'){
					var $defpwd = $("#defined_pwd");
					if($defpwd.val()==""||$.trim($defpwd.val())==""){
						alert("自定义密码不能为空！");
						return;
					}
					if($.trim($defpwd.val()).length<6){
						alert("自定义密码最少为6位！");
						return;
					}
					password = $.trim($defpwd.val());
				}
				$.ajax({
					type: 'POST',
					url: path+'/sys/org/employee/doBatchCreateUser.json',										
					dataType: "json",
					data:{
						"ids":ids,
						"usrType":$username.val(),
						"pwdType":password
					},
					success:function(result){
						result = jQuery.parseJSON(result);
						if (result.status == "1") {
							obj.closeAndRef();
						} else {
							alertError(result.msg);
						}
					}
				});
			}
			
	
		</script>
</html>
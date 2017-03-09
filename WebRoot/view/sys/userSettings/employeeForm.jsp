<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<title>员工信息</title>

<link rel="stylesheet" href="${vpath}/sys/userSettings/script/style.css"
	type="text/css" />
<script type="text/javascript"
	src="${vpath}/sys/userSettings/script/cropbox.js"></script>
</head>
<body class="bodyLayout">
	<f:form class="inputForm" action="${path}/sys/userSettings/doSave.json"
		id="employeeForm" modelAttribute="command" method="POST">
		<div style="text-align:center">
			<img
				style="width:93px;border-radius:50%;margin-bottom: 20px;margin-top: 20px;cursor:pointer"
				onclick="popImage()" src="${path}/sys/userSettings/viewPhoto.json"
				onerror="this.src='${vpath }/sys/userSettings/images/photobg.jpg';this.onerror=null;;" />
		</div>
		<table cellspacing="1" cellpadding="1" border="0"
			class="inputFormTable" width="600" style="margin:auto;">
			<tr>
				<input id="employeeId" type="hidden" value='${command.employeeId}'>

				<td class="lableTd" style="width:30%;text-align: right;">姓名${separator}</td>
				<td class="valueTd">${command.trueName}</td>
			</tr>
			<tr>
				<td class="lableTd" style="width:30%;text-align: right;">岗位${separator}</td>
				<td class="valueTd">${command.postName}</td>
			</tr>
			<tr>
				<td class="lableTd" style="width:30%;text-align: right;">手机号${separator}</td>
				<td class="valueTd"><input id="input_mobile" type="text"
					path="mobile" maxlength='50' disabled="true"
					value='${command.mobile}'>&nbsp;<input id="btn_mobile"
					type="button" value="编辑" class="bluebutton" onclick="edit_mobile()">
				</td>
			</tr>
			<tr>
				<td class="lableTd" style="width:30%;text-align: right;">座机${separator}</td>
				<td class="valueTd"><input id="input_officeTel" type="text"
					maxlength='50' disabled="true" value='${command.officeTel}'>&nbsp;<input
					id="btn_officeTel" type="button" value="编辑" class="bluebutton"
					onclick="edit_officeTel()"></td>
			</tr>
			<tr>
				<td class="lableTd" style="width:30%;text-align: right;">邮箱${separator}</td>
				<td class="valueTd"><input id="input_email" type="text"
					maxlength='50' disabled="true" value='${command.email}'>&nbsp;<input
					id="btn_email" type="button" value="编辑" class="bluebutton"
					onclick="edit_email()"></td>
			</tr>
		</table>
	</f:form>
</body>
<style type="text/css">
.inputForm input[type=text].borderinputmessage {
	border: 1px solid #23a9cc;
}

.valueTd input[type=button] {
	background-color: #ec9d00;
	color: #fff;
	width: 50px;
	height: 20px;
	line-height: 20px;
	cursor: pointer;
}

.valueTd input[type=button].bluebutton {
	background-color: #23a9cc;
}
</style>
<script>
	var serverURL = "${path}/sys/userSettings";
	var formId = "employeeForm";
	var mobile_click = 0;
	var officeTel_click = 0;
	var email_click = 0;
	var url = serverURL + "/doSave.json";
	var arr = new Array();
	arr["mobile"] = false;
	arr["officeTel"] = false;
	arr["email"] = false;
	var btn_id = "";
	var input_id = "";

	$(document).ready(function() {
		$(".valueTd input[type=button]").click(function() {
			$(this).toggleClass("bluebutton");
		})

		var msg = '${errmsg}';
		if (msg != undefined && typeof (msg) != "undefined" && msg != '') {
			alert(msg);
			$('#btn_mobile').attr('disabled', "true");
			$('#btn_officeTel').attr('disabled', "true");
			$('#btn_email').attr('disabled', "true");
		}

	});

	function edit_mobile() {
		var btn_id = "btn_mobile";
		var input_id = "input_mobile";
		var data = {};
		data.mobile = $("#" + input_id).val();
		data.employeeId = $("#employeeId").val();
		if (mobile_click == 0) {
			$("#" + btn_id).val("保存");
			$("#" + input_id).attr("disabled", false).addClass(
					"borderinputmessage").focus();
			mobile_click = 1;
			return;
		} else if (mobile_click == 1) {
			$("#" + input_id).attr("disabled", true).removeClass(
					"borderinputmessage");
			jQuery.ajax({
				type : 'POST',
				url : url,
				data : data,
				success : set_btn(btn_id, input_id),
				dataType : "json"
			});
			mobile_click = 0;
		}
	}

	function popImage() {
		var url = serverURL + '/doImage.do';
		showDialog("上传头像", url,'900','','view');
	}

	
	function edit_officeTel() {
		var btn_id = "btn_officeTel";
		var input_id = "input_officeTel";
		var data = {};
		data.officeTel = $("#" + input_id).val();
		data.employeeId = $("#employeeId").val();
		if (officeTel_click == 0) {
			$("#" + btn_id).val("保存");
			$("#" + input_id).attr("disabled", false).addClass(
					"borderinputmessage").focus();
			officeTel_click = 1;
			return;
		} else if (officeTel_click == 1) {
			$("#" + input_id).attr("disabled", true).removeClass(
					"borderinputmessage");
			jQuery.ajax({
				type : 'POST',
				url : url,
				data : data,
				success : set_btn(btn_id, input_id),
				dataType : "json"
			});
			officeTel_click = 0;
		}
	}
	function set_btn(btn_id, input_id) {
		$("#" + btn_id).val("编辑");
		$("#" + input_id).attr("disabled", true);
		jQuery.messager.alert(constants.alertTitle, "操作成功");
	}

	function edit_email() {
		var btn_id = "btn_email";
		var input_id = "input_email";
		var data = {};
		data.employeeId = $("#employeeId").val();
		data.email = $("#" + input_id).val();
		if (email_click == 0) {
			$("#" + btn_id).val("保存");
			$("#" + input_id).attr("disabled", false).addClass(
					"borderinputmessage").focus();
			email_click = 1;
			return;
		} else if (email_click == 1) {
			$("#" + input_id).attr("disabled", true).removeClass(
					"borderinputmessage");
			jQuery.ajax({
				type : 'POST',
				url : url,
				data : data,
				success : set_btn(btn_id, input_id),
				dataType : "json"
			});
			email_click = 0;
		}

	}
</script>
</html>
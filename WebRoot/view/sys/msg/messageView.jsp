<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>ec_message_tbl</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script type="text/javascript"
	src="${vpath}/sys/msg/script/jquery.autosize.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('textarea').autosize();
		try {
			top.heartbeatFun();
		} catch (e) {
		}
	});
</script>
<style>
.lableTd {
	background-color: #fbfbfb;
	text-align: right;
	width: 5%;
}
</style>
</head>
<body class="bodyLayout">
	<f:form class="inputForm" action="${path}/sys/message/doSave.json"
		id="messageForm" modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<f:hidden path="id" required='true' number='true' maxlength='255' />
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 消息标题 ${separator}</label>
						<div class="layui-input-block">${command.title}</div>
					</div>
				</td>

			</tr>
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 发送时间 ${separator}</label>
						<div class="layui-input-block">
							<fmt:formatDate value="${command.sendtime}" pattern="yyyy-MM-dd" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td style="word-wrap:break-word; word-break:break-all;">
					<div class="layui-form-item">
						<label class="layui-form-label"> 消息内容${separator}</label>
						<div class="layui-input-block">
							<c:if test="${not empty command.href}">
								<a href="${command.href}" target="_blank">${command.message}</a>
							</c:if>
							<c:if test="${ empty command.href}">
							${command.message}
						</c:if>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</f:form>
	</div>
</body>
<script>
	var serverURL = "${path}/entity/proj/message";
	var formId = "messageForm";

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
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>发起</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param value="v2" name="dialog"/>
		</jsp:include>
	</head>
	<body>
		<%@include file="/view/sys/wf/script.jsp" %>
		<script>
			function formSaveSuccessCallback(){ 
				top.jQuery.messager.alert("提示","操作成功", 'info',function(){
					window.location="${path}/sys/wf/processFilterList.jspx"; 
				});
			}
		</script>
		<div class="toolbarDiv" align="left">
			<jsp:include page="/com/inc/toolbar.jsp">
				<jsp:param name="texts" value="启动流程"></jsp:param>
				<jsp:param name="funs" value="start()"></jsp:param>
				<jsp:param name="ids" value="start"></jsp:param>
				<jsp:param name="icons" value="icon-add"></jsp:param>
			</jsp:include>
		</div>
 		<iframe id="workFlowFormIframe" src="${path}${requestScope.formUrl}" width="100%" height="500px" frameborder="0"></iframe>
	</body>
</html>
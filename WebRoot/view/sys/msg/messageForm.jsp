<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>ec_message_tbl</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/msg/script/jquery.autosize.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('textarea').autosize();
			});
		</script>
	</head>
	<body class="bodyLayout">
		<f:form class="inputForm" action="${path}/sys/message/doSave.json" id="messageForm" modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0" class="inputFormTable" width="100%">
				<tr>
					<f:hidden path="id" required='true' number='true' maxlength='255' />
					<td class="lableTd">
						消息标题 ${separator}
					</td>
					<td class="valueTd" colspan="3">
						<f:input path="title" maxlength='255' />
					</td>
				</tr>  
				<tr>
					<td class="lableTd">
						<span class='required'>*</span> 消息内容 ${separator}
					</td>
					<td class="valueTd" colspan="3">
						<f:textarea path="message" required='true' maxlength='1024' style="width:95%;height:100%;" />
					</td>
				</tr>
			</table>
		</f:form>
		</div>
	</body>
	<script> 
			var serverURL = "${path}/entity/proj/message";
			var formId = "messageForm";
			 
			function doSave(obj){ 
				if($("#"+formId).valid()){
					$("#"+formId).form({
						onSubmit: function(){
							 
						},
						success:function(result){
								 result = jQuery.parseJSON(result);
								 if(result.status == "1"){
								 obj.formSaveSuccessCallback(result);
							}else{
								top.jQuery.messager.alert(constants.alertTitle,result.msg);
							}
						}
					}); 
					$('#'+formId).submit(); 
				}
			} 

			$().ready(function() { 
									var v = $("#"+formId).validate(); 
							});
		</script>
</html>
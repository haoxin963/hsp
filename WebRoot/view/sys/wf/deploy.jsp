<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>发起</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
	</head>
	<body style="margin: 10px;">
		<style>
			*{
				font: 12px;
			}
			input{
				height: 25px;
			}
		</style>
		<script>
			function deploy(){
				var file = document.getElementById("file").value;
				if(file!=""){
					document.getElementById("btn").onclick=function(){};
					document.getElementById("form1").submit();
				}
			}
			
			function back(){
				var api = frameElement.api; 
				var w = api.opener;
				w.dg.datagrid('reload');
				frameElement.api.close();
			}
			
			function info(msg){
				document.getElementById("info").innerHTML = msg;
				document.getElementById("btn").onclick= deploy;
			}
		</script>
		<br/>
		<iframe name='h' id="h" style='display:none'></iframe>
		<form action="${path}/sys/wf/deploy.do" target="h" id="form1" method="post" enctype="multipart/form-data">
			<input type="file" name="file" id="file" />
			<input type="button" id="btn" value="部署" onclick="deploy()"/>
			<br/><br/>
			<span id="info"></span>
		</form>
	</body>
</html>
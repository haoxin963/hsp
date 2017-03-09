<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改密码</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
 
<link rel="stylesheet" type="text/css" href="${path}/main/css/modifypwd.css" />
<script type="text/javascript">
 	var custId= "${sessionScope.CurrentSystemInstance}";
</script> 
<script type="text/javascript" src="${path}/main/script/modifypwd.js"></script>
</head>
<body>
	<div class="formdiv">
		<div class="tr">
			<div class="tit">修改密码</div>
		</div>
		<div class="tr">
			<div class="lbl"> 
				原&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;码：
			</div>
			<div class="txt"> 
				<input type="password" id="oldpwd" name="oldpwd" value="" class="input-text"
					style="width:200px"/>
			</div>
		</div>
		<div class="tr">
			<div class="lbl"> 
				新&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;&nbsp;码：
			</div>
			<div class="txt"> 
				<input type="password" id="newpwd" name="newpwd" value="" class="input-text"
					style="width:200px"/>
			</div>
		</div>
		<div class="tr">
			<div class="lbl"> 
				重复新密码：
			</div>
			<div class="txt"> 
				<input type="password" id="renewpwd" name="renewpwd" value="" class="input-text"
					style="width:200px"/>
			</div>
		</div>
		<div class="tr">
			<div class="formbtn">
				<button type="button" class="btn btn-success radius" id="" name="" onclick="doModifyPwd();">确 认</button> 
			</div>
			 
		</div>		
	</div>
</body>
</html>
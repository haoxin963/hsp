<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/resource/inc/incMeta.jsp" />
		<jsp:include page="/resource/inc/incCssJs.jsp" />
		<title>功能模块列表</title>
	 	<script>
	 		var path="${path}";
	 		var roleId = "${roleId}";	 	
	 	</script>		 		 
		<script type="text/javascript" src="${vpath}/sys/rbac/script/grant_func.js"></script>
	</head>
	<body class="bodyLayout">
		<ul id="tree" class="ztree">
		</ul>
	</body>
</html>

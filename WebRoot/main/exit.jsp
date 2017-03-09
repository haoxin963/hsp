<%@ page language="java"  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%> 
<%@page import="javax.servlet.http.Cookie"%>
<%
String custId = (String)session.getAttribute("CurrentSystemInstance");
session.removeAttribute("userName");
session.removeAttribute("userId");
session.removeAttribute("CurrentSystemInstance");
session.removeAttribute("domain");
if(custId!=null)
{
	response.sendRedirect(request.getContextPath()+"/"+custId+".login");
	return;
}else{
	Cookie[] cs = request.getCookies();
	for (int i = 0; i < cs.length; i++) {
		if("custId".equals(cs[i].getName())){
			custId = cs[i].getValue();
		}
	}
	if(custId!=null)
	{
		response.sendRedirect(request.getContextPath()+"/"+custId+".login");
		return;
	}
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安全退出</title>
<style type="text/css">
#alert1{ width:450px; min-height:150px; _height:150px; padding:50px 20px 10px 100px; margin:auto; font:normal 13px/150% "宋体"; color:#777; background:url(${pageContext.request.contextPath}/images/alert.gif) no-repeat 0 50px;}
#alert1 a{ color:#0080B3; font-size:15px; text-decoration:underline}
.alertGray{ color:#999}
</style>
</head>

<body>
<div id="alert1">
    <br /> 您已经成功退出系统!<br /><br /> 
</div>
</body>
</html>

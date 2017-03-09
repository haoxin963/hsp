<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="net.hsp.service.sys.rbac.FunctionService"%>
<%@page import="net.hsp.web.util.SpringCtx"%>
<%
	String userId = String.valueOf(session.getAttribute("userId"));
	String custId = (String)session.getAttribute("CurrentSystemInstance"); 
	String basePath = request.getContextPath();
	FunctionService funcService =  (FunctionService)SpringCtx.getSpringContext().getBean("functionServiceImpl");
	if(funcService.checkUserMenuFileExist(userId,custId)){
		//包含
%>
	<jsp:include page="/page/${sessionScope.CurrentSystemInstance}/menu/${sessionScope.userId}.html"></jsp:include>
<%			
	}else{
		//生成菜单文件
		funcService.generateMenuHtmlFile(userId,custId,basePath);
%>
	<jsp:include page="/page/${sessionScope.CurrentSystemInstance}/menu/${sessionScope.userId}.html"></jsp:include>
<%
	}
%>
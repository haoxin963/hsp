<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%
	String custId2 = (String)session.getAttribute("CurrentSystemInstance"); 
	String diskPath = net.hsp.common.SystemContext.getProperty("webApp.root");
	String filePath = diskPath + "/page/" + custId2 + "/welcome.jsp";
	File menu = new File(filePath);
	boolean exist=menu.exists();
	
	if(exist){
	//包含
%>
	<jsp:include page="/page/${sessionScope.CurrentSystemInstance}/welcome.jsp"></jsp:include>
<%			
	}else{
%>
	<jsp:include page="/main/welcome.jsp"></jsp:include>
<%
	}
%>
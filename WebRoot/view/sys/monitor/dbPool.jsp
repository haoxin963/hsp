<%@ page language="java" import="java.util.*,net.hsp.web.sys.monitor.pool.*" pageEncoding="UTF8"%>

	<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
    
		<%@include file="/view/sys/monitor/nav.jsp" %>
		
		<%  
		ProxoolServlet p = new ProxoolServlet();
				try {
					request.setCharacterEncoding("utf-8");
					response.setCharacterEncoding("utf-8");
					p.doGet(request, response);
				} catch (Exception e) { 
					e.printStackTrace();
				}
		%>
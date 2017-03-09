<%@ page language="java" import="java.util.*" pageEncoding="UTF8"%> 
   		<jsp:include page="/com/inc/incCssJs.jsp">
		<jsp:param name="com" value="core" />
	</jsp:include>
<div class="toolbarDiv" align="left">
			<jsp:include page="/com/inc/toolbar.jsp">
				
				<jsp:param name="texts" value="容器实时日志"></jsp:param> 
				<jsp:param name="texts" value="容器日志下载"></jsp:param> 
				<jsp:param name="texts" value="作用域"></jsp:param>
				<jsp:param name="texts" value="Bean容器"></jsp:param>
				<jsp:param name="texts" value="连接池"></jsp:param> 
				<jsp:param name="texts" value="JVM"></jsp:param>
				<jsp:param name="texts" value="SQL日志"></jsp:param>
				
				<jsp:param name="funs" value="loadPage('/sys/monitor/log/log.jspx')"></jsp:param>
				
				<jsp:param name="funs" value="loadPage('/sys/monitor/log/container/doList.do')"></jsp:param>
				<jsp:param name="funs" value="loadPage('/sys/monitor/scopeInfo.jspx')"></jsp:param>
				<jsp:param name="funs" value="loadPage('/sys/monitor/beans.jspx')"></jsp:param>
				<jsp:param name="funs" value="loadPage('/sys/monitor/pool/proxoolList.do')"></jsp:param>
				<jsp:param name="funs" value="loadPage('/sys/monitor/serverInfo.jspx')"></jsp:param>
				<jsp:param name="funs" value="loadPage('/sys/monitor/sqlLog.jspx')"></jsp:param>
				
				<jsp:param name="ids" value="id2"></jsp:param>
				<jsp:param name="ids" value="id3"></jsp:param>
				<jsp:param name="ids" value="id5"></jsp:param>
				<jsp:param name="ids" value="id4"></jsp:param> 
				<jsp:param name="ids" value="id6"></jsp:param>
				<jsp:param name="ids" value="id7"></jsp:param>
				<jsp:param name="ids" value="id7"></jsp:param>
				
				<jsp:param name="icons" value="icon-add"></jsp:param>
				 <jsp:param name="icons" value="icon-edit"></jsp:param>
				 <jsp:param name="icons" value="icon-edit"></jsp:param>
				<jsp:param name="icons" value="icon-edit"></jsp:param>
				<jsp:param name="icons" value="icon-add"></jsp:param>
				<jsp:param name="icons" value="icon-add"></jsp:param>  
				<jsp:param name="icons" value="icon-add"></jsp:param> 
			</jsp:include>
		</div>
		
			<script>
			function loadPage(name){
				//var contentFrame = document.getElementById("contentFrame");
				//contentFrame.src = "${path}"+name;
				window.location =  "${path}"+name;
			}
		</script>
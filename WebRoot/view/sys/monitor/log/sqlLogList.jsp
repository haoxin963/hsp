<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.io.*"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:include page="/com/inc/incMeta.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>

	<jsp:include page="/com/easyui/inc.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>

	<body>
		<table class="easyui-datagrid" style="width: auto;border:false,height: auto">
			<thead>
				<tr>
					<th data-options="field:'ID',width:50">
						ID
					</th>
					<th data-options="field:'名称',width:150">
						名称
					</th>
					<th data-options="field:'大小',width:150">
						大小
					</th>
					<th data-options="field:'操作',width:150">
						操作
					</th>
				</tr>
			</thead>
			<tbody> 
				<%
				String defaultPath = config.getServletContext().getRealPath("/page"); 
				String filePath = new File(defaultPath, "logs").getAbsolutePath();
			
				File[] files = new File(filePath).listFiles();
				if (files != null) {
					int k = 0;
					 for (int i = 0; i < files.length; i++) {
					 	if(files[i].isFile()){
					 		k++;
					 		FileInputStream fis = null;
					 		try{
					 	 			fis = new FileInputStream(files[i]) ;
								String name = files[i].getName();
								String link = "sql/downloadLog.do?filename=" + name;
								out.println("<tr><td>"+k+"</td><td>"+name+"</td><td>"+fis.available()/1024+"(KB)</td><td><a href='"+link+"'>下载</a></td></tr>");
								//out.println("<tr><td>"+k+"</td><td>"+name+"</td><td>"+fis.available()/1024+"(KB)</td><td><a href='"+request.getContextPath()+"/page/logs/"+name+"'>下载</a></td></tr>");
							}finally{
								fis.close();
							}
						 }
					 } 
				} 
				
			%>
			</tbody>
		</table>
	</body>
</html>
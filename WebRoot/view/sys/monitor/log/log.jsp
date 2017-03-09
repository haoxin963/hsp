<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.io.File"%>
<%@page import="java.io.RandomAccessFile"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head> 
    		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
    <title>系统日志</title> 
 
  </head>
  <style>body{font-size:11px}</style>

 
<%! 

public String redFile(File f,long gl){
	StringBuffer sb = new StringBuffer();
	long len =0;
	RandomAccessFile raf = null;
       try
		{
			// 使用RandomAccessFile , 从后找最后一行数据
			raf = new RandomAccessFile(f, "r");
			len = raf.length();
			List<String> errList = new ArrayList<String>();
			if (len != 0L)
			{
				long pos = len - 1;
				int i = 0;
				if(gl>0)
				gl=len-gl;
				else gl=10;
				while (pos > 0)
				{
					if (i >500)
						break;
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n')
					{
						String lastLine = new String(raf.readLine().getBytes("iso-8859-1"), "utf8");
						i++;
						if (lastLine.indexOf("ERROR") != -1 ||lastLine.indexOf("Exception") != -1 || lastLine.startsWith("	at"))
				         {
                                if(lastLine.startsWith("	at"))
                                     lastLine="&nbsp;&nbsp;&nbsp;"+lastLine;
                               errList.add(""+lastLine+"");
                              }
                     

				else 	errList.add(lastLine);
					}
				}
			}

			for (int i = errList.size(); i > 0; i--)
			{
				sb.append(errList.get(i - 1) + "\n<br/>");

			} 
		}
		catch (Exception e)
		{     
			return "出现异常了"; 
		}finally{
			try{
				raf.close();
			}catch(Exception e){
			
			}
		}
		return sb.toString()        ;
} 

%>

<%  
String path = request.getParameter("logPath"); 
if(StringUtils.isBlank(path)){
	path = "/opt/tomcat/logs";
} 
pageContext.setAttribute("logPath", path);
String filePath = new File(path).getAbsolutePath();
String logStr = null;
long len = 0l;
File[] files = new File(filePath).listFiles();
if(files != null){  
	String getLength = request.getParameter("getLength");
	int gl=0;
	if("".equals(getLength)){
		gl=Integer.valueOf(getLength); 
	}
	
	for (int i = 0; i < files.length; i++) {
		String name = files[i].getName().toLowerCase();
		if (name.equals("catalina.out")) {
			try {
				logStr = redFile(files[i],  gl); 
				len = files[i].length();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
	}
}
%> 
<body bgcolor="#fbfbfb"> 
	<script>
		function getLog(){
			window.location = "${path}/sys/monitor/log/log.jspx?path="+document.getElementById("url").value; 
		}
		
		function searchFun(){
			$("#searchForm").submit();
		}; 
		
	</script>
	<%@include file="/view/sys/monitor/nav.jsp" %> 
	 		<div id="tb" class="searchTb">
			<form id="searchForm" method="post" class="searchForm"  action="${path}/sys/monitor/log/log.jspx"> 
				日志路径 (catalina.out文件所有目录)${separator}
				<input type='text'  class='filter' name="logPath" value="${logPath}"   style="width:300px">
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>				  
			</form>
		</div>
     <div style="padding:5px;" >
     <%if(logStr!=null){%>
     <span style="color:red">日志文件大小:<%=len/1024 %>KB,当前显示最后500行.</span><br/><br/>
     <%=logStr %>
     <%} %>
     </div> 
  </body>
</html>


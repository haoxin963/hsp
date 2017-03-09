<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />

<title>角色用户</title>
	

	
	</head>
	<body class="bodyLayout"> 
			<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" />
				<input type="text" style="display: none" name="exportTitle"
					id="exportTitle" />
				<input type="text" style="display: none" name="employeeId"
					id="employeeId" value="${employeeId }"/>
				用户名${separator}
				<input type='text' class="input-text" style="width:120px" name="filter[userName]">	
				真实姓名${separator}
				<input type='text' class="input-text"  style="width:120px" name="filter[trueName]">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
			</div>
		</div>
		
			<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="userName"  width="25%;"  report="true">	用户名</th>
					<th w_index="userName" width="25%;" report="true">真实姓名</th>
				
				</tr>

			</table>
		</div>
		
			<script type="text/javascript"
			src="${vpath}/sys/org/script/userSelectList.js"></script>
	</body>
</html>

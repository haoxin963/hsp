<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
		<title>任务列表</title>

		<script type="text/javascript"
			src="${vpath}/sys/schedule/script/taskList.js"></script>

	</head>
	<body class="bodyLayout">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
				
					<jsp:param name="funs" value="toAdd()"></jsp:param>
			
					<jsp:param name="ids" value="id1"></jsp:param>
				
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
				
									 
			</jsp:include>
			</span>
		</div>

	<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				<input type="hidden" name="exportHead" id="exportHead" />
				<input type="hidden" name="exportTitle" id="exportTitle" />
				任务名${separator}
				<input class="input-text"  style="width:120px"name="filter[taskName]">
				站点${separator}
				<input class="input-text"  style="width:120px"name="filter[station]">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
		</div>


			<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="taskName" width="25%" >
						任务名
					</th>
					<th w_index="taskClass" width="25%">
						类
					</th>
					<th w_index="station" width="25%" >
						站点
					</th>
					<th w_index="createDate" width="25%" >
						创建时间
					</th>
					<th w_render="operate" width="15%;">操作</th>
				</tr>
			
		</table>
		</div>
			<script type="text/javascript"
			src="${vpath}/sys/schedule/script/taskList.js"></script>
		
	</body>
</html>

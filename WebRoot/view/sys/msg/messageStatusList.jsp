<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%pageContext.setAttribute("status",request.getParameter("filter[status]")); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	 <jsp:include page="/resource/inc/incMeta.jsp" />
       <jsp:include page="/resource/inc/incCssJs.jsp" />
		<title>ec_message_tbl列表</title>
	
		
	</head>
	<body class="bodyLayout">
		<div class="table-detail">
			<c:if test="${ param.selector ne 'true' }">
					<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> 
					<jsp:include page="/resource/inc/toolbar.jsp">
						<jsp:param name="texts" value="删除"></jsp:param>
						<jsp:param name="funs" value="doDelete('')"></jsp:param>
						<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
					</jsp:include>
				</span>
		</div>
			</c:if>
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead" id="exportHead" />
					<input type="text" style="display: none" name="exportTitle" id="exportTitle" />
					消息标题${separator}
					<input type='text' class="input-text"  style="width:120px" name="filter[m.title]">
					消息内容${separator}
					<input type='text' class="input-text"  style="width:120px" name="filter[m.message]">
					阅读状态${separator}
					<select name="filter[status]" class="input-text"  style="width:120px">
						<option value="">全部</option>
						<option value="1" ${status eq 1 ? 'selected' : ''}>未读</option>
						<option value="2" ${status eq 2 ? 'selected' : ''}>已读</option>
					</select>
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</div>
	<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
				   <th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="title" width="40%;" report=true>
						消息标题
					</th>
					<th w_index="message" width="40%;" report=true>
						消息内容
					</th>
					<th w_index="sendtime" width="40%;"  report=true w_render="rowDateFormater">
						发布时间
					</th>
					<th w_index="status" width="20%;"  report=true w_render="statusFormater">
						状态
					</th>
						<th w_render="operate" width="15%;">操作</th>
				</tr>
			
		</table>
		</div>
		<script type="text/javascript" src="${vpath}/sys/msg/script/messageStatusList.js"></script>
	</body>
</html>

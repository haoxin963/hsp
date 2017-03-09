<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true" session="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
		<title>登录日志列表</title>
	 <jsp:include page="/resource/inc/incMeta.jsp" />
       <jsp:include page="/resource/inc/incCssJs.jsp" />
	

	</head>
<body class="bodyLayout">
		<c:if test="${ param.selector ne 'true' }">
			<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
				
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="report('doAllList')"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
			
					<jsp:param name="icons" value="&#xe6e2"></jsp:param>
					<jsp:param name="icons" value="&#xe644;"></jsp:param>
				</jsp:include>
			</span>
		</div>
		</c:if>
	<c:if test="${searchType ne 'advance' }">
	<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" />
				<input type="text" style="display: none" name="exportTitle"
					id="exportTitle" />
 
				类型${separator} 
				<select class="input-text"  style="width:120px"name="filter[logType]"><option value="">全部</option><option value="0">浏览器</option><option value="1">桌面Agent</option><option value="2">移动Agent</option></select>
				时间${separator}
				<input type='text' class="input-text"  style="width:120px"name="filter[operateTime]">
				IP${separator}
				<input type='text' class="input-text"  style="width:120px"name="filter[ip]">
				登录人${separator}
				<input type='text' class="input-text"  style="width:120px"name="filter[userId]">
				操作系统${separator} 
				<select class="input-text"  style="width:120px"name="filter[osVersion]"><option value="">全部</option><option value="win2000">win2000</option><option value="winxp">winxp</option><option value="win2003">win2003</option><option value="win7">win7</option><option value="win8">win8</option></select>
				终端${separator} 
				<select class="input-text"  style="width:120px"name="filter[agentVersion]"><option value="">全部</option><option value="ios">ios</option><option value="android">android</option><option value="1">ie7</option><option value="ie8">ie8</option><option value="ie9">ie9</option><option value="ie10">ie10</option><option value="ie11">ie11</option><option value="firefox">firefox</option><option value="chrome">chrome</option><option value="360">360</option><option value="qq">qq</option></select>
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
		</div>
	</c:if>
	<c:if test="${searchType eq 'advance' }">
		<jsp:include page="/resource/search/searchBtn.jsp">
			<jsp:param name="baseSearch"
				value="[{'field':'ip','text':'IP'},{'field':'userId','text':'登录人'},{'field':'osVersion','text':'操作系统'},{'field':'agentVersion','text':'终端'}]"></jsp:param>
			<jsp:param name="advanceSearch"
				value="{'items':[{'field':'ip','text':'IP','type':'string'},{'field':'userId','text':'登录人','type':'string'},{'field':'operateTime','text':'操作时间','type':'datetime'},{'field':'logType','text':'类型','type':'multiCbo','selections':[{'value':'0','text':'浏览器'},{'value':'1','text':'桌面Agent'},{'value':'2','text':'移动Agent'}]}]}"></jsp:param>
		</jsp:include>
	</c:if>

	<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_render="rowformater1" w_index="logType"  width="25%"  report=true>
						登录类型
					</th>
					<th w_index="operateTime" width="25%" report=true>
						登录时间
					</th>
					<th w_index="ip" width="25%"  report=true>
						登录ip
					</th>
					<th w_index="userId" width="25%"  report=true>
						登录人
					</th>
					<th w_index="osVersion" width="25%"  report=true>
						操作系统及版本
					</th>
					<th w_index="agentVersion" width="25%"  report=true>
						终端/浏览器类型及版本
					</th>
				     <c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>
			</table>
		</div>
	</body>
	
		<script type="text/javascript" src="${vpath}/sys/monitor/log/script/userLogList.js"></script>
</html>

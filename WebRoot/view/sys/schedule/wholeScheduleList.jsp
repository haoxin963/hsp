<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@page import="net.sf.json.JSONObject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>计划任务列表</title>
		<jsp:include page="/com/easyui/inc.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script type="text/javascript"
			src="${vpath}/sys/schedule/script/scheduleList.js"></script>
	</head>
	<script type="text/javascript">
	var setting = {
		data: {
			simpleData: {
				enable: true,
				idKey:"id",
				pIdKey:"parentId"
			},
			key: { 
				name: "statName" 
			}
		},
		callback: { 
	        onClick: onTreeClickBack
	    }
	};
		 
	function onTreeClickBack(event, treeId, treeNode){ 
		var station  = treeNode.domainAddress;
		$("#station").val(station);
		searchFun();
	};
	$(document).ready(function(){
	 	createTree(); 
	}); 
	</script>
	<body class="easyui-layout" fit="true">
		<div data-options="region:'west',split:true" title="站点列表"
			style="width: 180px;">
			<jsp:include page="/com/inc/incSimpleTree.jsp">
				<jsp:param name="serviceBeanId" value="stationServiceImpl" />
			</jsp:include>
		</div>
		<div data-options="region:'center',border:false"
			style="overflow: hidden;">
			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" style="margin: 0px">
					任务名称:
					<input type="text" name="filter[taskName]">
					<input id="station" type="hidden" name="filter[station]" value="">
					<a href="#" class="easyui-linkbutton" onclick="searchFun()"
						data-options="plain:true,iconCls:'icon-search'">搜索</a>
					<a class="easyui-linkbutton"
						onclick="document.getElementById('searchForm').reset()"
						data-options="plain:true,iconCls:'icon-undo'">清空条件</a>
				</form>
			</div>
			<table id="dg" width="100%" height="100%">
				<thead>
					<tr>
						<th field="taskName" width=180>
							任务名称
						</th>
						<th field="taskClass" width=180>
							类
						</th>
						<th field="description" width=540>
							描述
						</th>
						<th
							data-options="width:50,field:'openFlag',formatter:flagformater">
							状态
						</th>
						<th field="createDate" width=180>
							创建时间
						</th>
						<th data-options="width:120,field:'a',formatter:rowformater">
							操作
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</body>
</html>

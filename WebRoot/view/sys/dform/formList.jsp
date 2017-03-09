<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"  trimDirectiveWhitespaces="true"%>
<%@page import="net.sf.json.JSONObject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include> 
		<title>自定义表单列表</title>
		<jsp:include page="/com/easyui/inc.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script type="text/javascript" src="${vpath}/sys/dform/script/formList.js"></script>
	</head>
	<body  class="bodyLayout">
		<div class="toolbarDiv" align="left">
			<jsp:include page="/com/inc/toolbar.jsp">
				<jsp:param name="texts" value="开启"></jsp:param>
				<jsp:param name="texts" value="停止"></jsp:param>
				<jsp:param name="texts" value="新增"></jsp:param>
				<jsp:param name="texts" value="编辑"></jsp:param>
				<jsp:param name="texts" value="查看"></jsp:param>
				<jsp:param name="funs" value="start()"></jsp:param>
				<jsp:param name="funs" value="stop()"></jsp:param>
				<jsp:param name="funs" value="toAdd()"></jsp:param>
				<jsp:param name="funs" value="toEdit()"></jsp:param>
				<jsp:param name="funs" value="view()"></jsp:param>
				<jsp:param name="ids" value="id1"></jsp:param>
				<jsp:param name="ids" value="id2"></jsp:param>
				<jsp:param name="ids" value="id3"></jsp:param>
				<jsp:param name="ids" value="id4"></jsp:param>
				<jsp:param name="ids" value="id5"></jsp:param>
				<jsp:param name="icons" value="icon-ok"></jsp:param>
				<jsp:param name="icons" value="icon-cancel"></jsp:param>
				<jsp:param name="icons" value="icon-add"></jsp:param>
				<jsp:param name="icons" value="icon-edit"></jsp:param>
				<jsp:param name="icons" value="icon-redo"></jsp:param>
			</jsp:include>
		</div>
		<div data-options="region:'center',border:false"
			style="overflow: hidden;">
			<table id="dg" width="100%" height="100%" >
				<thead>
					<tr>
						<th data-options="field:'formId',checkbox:true"></th>
						<th field="formName" width=180>
							表单名称
						</th>
						<th field="status" width=180>
							状态
						</th>
						<th field="createId" width=540>
							创建者
						</th>
						<th field="createTime" width=180>
							创建者
						</th>
						<th field="updateId" width=180>
							最后更新者
						</th>
						<th field="updateTime" width=180>
							最后更新时间
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</body>
</html>

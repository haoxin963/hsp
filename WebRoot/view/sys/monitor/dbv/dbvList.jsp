<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="/com/inc/incMeta.jsp">
	<jsp:param name="com" value="core"></jsp:param>
</jsp:include>

<title>数据库浏览器</title>
<jsp:include page="/com/inc/incCssJs.jsp">
	<jsp:param name="com" value="core" />
</jsp:include>
<jsp:include page="/com/inc/jsonList.jsp">
	<jsp:param name="command" value="command"></jsp:param>
</jsp:include>

<body>

	<div id="tb" class="searchTb">
		<form id="searchForm" method="post" class="searchForm">
			表名${separator} <input type='text' class='filter'  name="filter[tablename]">
			<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
		</form>
	</div>

	<table id="dg" width="100%" height="100%">
		<thead>
			<tr id="gridThead">
				<th data-options="field:'tname', width:100">表名</th>
				<th data-options="field:'tcomment', width:100">表注释</th>
				<th data-options="field:'trows', width:50">表行数</th>
				<c:choose>
					<c:when test="${dbtype eq 'oracle'}">
						<th data-options="field:'ttime', width:100">最后分析时间</th>
					</c:when>
					<c:when test="${dbtype eq 'mysql'}">
						<th data-options="field:'ttime', width:100">创建时间</th>
					</c:when>
					<c:otherwise>
						<th data-options="field:'ttime', width:100">记录时间</th>
					</c:otherwise>
				</c:choose>
				<th data-options="field:'tinfo', width:100">相关信息</th>
				<th data-options="width:50,field:'a',formatter:rowformater">操作</th> 
			</tr>
		</thead>
	</table>

	<script type="text/javascript">
		function searchFun() {
			var data = $("#searchForm").serializeJson();
			$('#dg').datagrid('load', data);
		};

		function rowformater(value, row, index) {
			return "<a  href='javascript:void(0);'  title='结构' name='editBtn' onclick=showTableStruct('"+row.tname+"')>查看结构</a>&nbsp;&nbsp;"
					+ "<a href='javascript:void(0);' title='数据' name='delBtn'  onclick=showTableData('"+row.tname+"')>查看数据</a>";
		};

		var listPath = path + "/sys/monitor/dbv/doList.json";
		var dg = $('#dg').datagrid({
			data : dataList,
			url : listPath,
			loadFilter : function(r) {
				if (r.command) {
					return r.command;
				} else {
					return r;
				}
			},
			singleSelect : true,
			pagination : true,
			fitColumns : true,
			striped : true,
			rownumbers : true
		});
 
		function showTableStruct(tname) {
			var actionURL = path + "/sys/monitor/dbv/showStruct.do?tname=" + tname;
			top.ymPrompt.win({
				title : "表结构：" + tname,
				autoClose : false,
				width : 1000,
				height : 580,
				fixPosition : true,
				maxBtn : true,
				minBtn : false,
				handler : tableStructHandler,
				btn : [ [ constants.save, 'ok' ], [ constants.cancel, 'cancel' ] ],
				iframe : {
					id : 'dbview',
					name : 'dbview',
					src : actionURL
				}
			});
		}
		function tableStructHandler(tp) {
			top.ymPrompt.close();
		}

		function showTableData(tname) { 
			var actionURL = path + "/sys/monitor/dbv/showData.do?tname=" + tname;
			top.ymPrompt.win({
				title : "表数据：" + tname,
				autoClose : false,
				width :1000,
				height : 580,
				fixPosition : true,
				maxBtn : true,
				minBtn : false,
				handler : tableDataHandler,
				btn : [ [ constants.save, 'ok' ], [ constants.cancel, 'cancel' ] ],
				iframe : {
					id : 'dbview',
					name : 'dbview',
					src : actionURL
				}
			});
		}
		function tableDataHandler(tp) {
			top.ymPrompt.close();
		}
	</script>


</body>
</html>

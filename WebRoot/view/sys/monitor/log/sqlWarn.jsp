<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.io.File"%>
<%@page import="java.io.RandomAccessFile"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<jsp:include page="/com/inc/incMeta.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>

	<jsp:include page="/com/easyui/inc.jsp">
		<jsp:param name="com" value="core"></jsp:param>
	</jsp:include>
	
	<jsp:include page="/com/inc/jsonList.jsp">
		<jsp:param name="command" value="command"></jsp:param>
	</jsp:include>

	<body>

		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'sql'">
						SQL记录
					</th>
				</tr>
			</thead>
		</table>
		
<script type="text/javascript">

var listPath = path+"/sys/monitor/log/sql/warnLogList.json";
//alert("listPath="+listPath);
var dg = $('#dg').datagrid({
	data:dataList,
    url:listPath,
	loadFilter: function(r){
		if (r.command){ 
			return r.command;
		} else {
			return r;
		}
	}, 
    pagination:true, 
    fitColumns:constants.fitColumns,
    striped:true,
    rownumbers:true,
   // pageSize:3, 
   // pageList:[3,5,10,20]
}); 

</script>
		
	</body>
</html>
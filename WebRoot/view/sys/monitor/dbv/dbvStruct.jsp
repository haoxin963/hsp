<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
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
				<th data-options="field:'index', width:30">列序号</th>
				<th data-options="field:'colname', width:180">列名</th>
				<th data-options="field:'datatype', width:110">数据类型</th>
				<th data-options="field:'nullable', width:50">是否可为空</th>
				<th data-options="field:'comments', width:120">注释</th>
				<th data-options="field:'note', width:200">其他信息</th>
			</tr>
		</thead>
	</table>
	
<script type="text/javascript">
//var dataList = {"page":12,"rows":[{"tname":"t1","tinfo":"inf1"}]}
var listPath = path+"/sys/monitor/dbv/showStruct.json?tname=${tname}";
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
	singleSelect:true,
	nowrap:false,
    pagination:false, 
    fitColumns:constants.fitColumns,
    striped:true,
    rownumbers:true,
   // pageSize:3, 
   // pageList:[3,5,10,20]
}); 
</script>
  </body>
</html>

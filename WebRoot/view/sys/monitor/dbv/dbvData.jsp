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
	</table>
<script type="text/javascript">
//var dataList = {"page":12,"rows":[{"tname":"t1","tinfo":"inf1"}]}
var listPath = path+"/sys/monitor/dbv/showData.json?tname=${tname}";
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
	columns:[dataList.columns] ,
	singleSelect:true,
    pagination:true, 
    fitColumns:false,
    striped:true,
    rownumbers:true,
   // pageSize:3, 
   // pageList:[3,5,10,20]
}); 
</script>
  </body>
</html>

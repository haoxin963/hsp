<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>日志列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include> 
	</head>
	<body class="bodyLayout"> 
		<%@include file="/view/sys/monitor/nav.jsp" %>
		<div id="tb" class="searchTb">
			<form id="searchForm" method="post" class="searchForm"> 
				日志路径 (日志文件所在目录)${separator} 
				<input type='text'  class='filter' name="path" value="${logPath}"  style="width:300px">
				<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>				  
			</form>
		</div>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="field:'id',checkbox:true"></th>					
					<th field="name" width=70 report=true>
							文件名
					</th>
					<th field="length" width=70 report=true>
							文件大小(KB)
					</th>	
					<th field="modified" width=70 report=true>
							最后更新时间
					</th>					 
					<th data-options="field:'xxx',formatter:rowformater">
						操作
					</th>
				</tr>
			</thead>
		</table>
		<script>
		var basePath = path+'/sys/monitor/log/container';
		var dg;
		$(function(){
			var i=0;
			var listPath = basePath+"/doList.json";
			if(typeof(dataList)!="undefined" ){
				dg = $('#dg').datagrid({
				    data:dataList,
				    pagination:true, 
				    fitColumns:constants.fitColumns,
				    striped:true,
				    rownumbers:true,
					loadFilter: function(r){
						if (r.command){ 
							return r.command;
						} else {
							return r;
						}
					}, 
				    onBeforeLoad:function(){
				    	if(i==1){
					    	var opts = $(this).datagrid('options');
					    	opts.url= listPath; 
				    	}
				    	i++;
				    }
				}); 
			}else{
				dg = $('#dg').datagrid({
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
				    rownumbers:true
				}); 
			}
			
		}); 
 

		function rowformater(value,row,index)
		{
			return "<a href='"+basePath+"/downloadLog.do?filename="+row.name+"&path=${logPath}'>下载</a>";
		};

		function searchFun(){
			var data = $("#searchForm").serializeJson(); 
		  	$('#dg').datagrid('load',data); 
		}; 
		</script>
	</body>
</html>

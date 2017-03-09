<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>所有流程</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<body class="bodyLayout"> 
			<table id="dg" width="100%" height="100%"  border=false>
				<thead>
					<tr>
						<th field="name"  width="80%">
							流程名称
						</th>
						 
						<th data-options="field:'opr',formatter:rowformater" >
							操作
						</th>
					</tr>
				</thead>
			</table> 
	</body>
	<script>
				var basePath = '${path}/sys/wf';
				var dg;
				$(function(){
					var i=0;
					var listPath = basePath+"/processFilterList.json";
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
					    fit:true,
					    rownumbers:true
					});  
				}); 
				 
				
				function showImg(id){  
					$.dialog({cancel:true,title:'流程示意图',content: 'url:'+basePath+'/resourceRead.do?resourceType=image&processDefinitionId='+id,name:'a',id:'dialogPic',lock: true,parent:this,width: 1000, height: 500});
				};
				
				function start(startPage,key){  
					window.location = basePath+"/toStart.do?wfKey="+key+"&formUrl="+startPage;
				};
				
				function startByAutoFirstTask(startPage,key){ 
					top.jQuery.messager.confirm("提示","确认启动流程?",function(r){
						if (r){ 
							window.location = basePath+"/startByAutoFirstTask.do?wfKey="+key+"&formUrl="+startPage;
						}
					});
				};
				
				function rowformater(value,row,index)
				{  
					var str = "";
					//区分两种情况：1、start 表单提交时启动流程，2、startByAutoFirstTask启动流程后初始化表单
					//str +="<a href='javascript:void(0);' name='startBtn' onclick=start('"+row.description+"','"+row.key+"')>启动</a>&nbsp;&nbsp;";
					str +="<a href='javascript:void(0);' name='startBtn' onclick=startByAutoFirstTask('"+row.description+"','"+row.key+"')>启动</a>&nbsp;&nbsp;";
					str +="<a href='javascript:void(0);' name='imgBtn' onclick=showImg('"+row.id+"')>流程图</a>";
					return str;
				};
	</script>
</html>
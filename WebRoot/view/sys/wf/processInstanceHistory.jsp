<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp"></jsp:include>
		<title>流程历史</title>
		<jsp:include page="/com/inc/incCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${path}/com/window/lhgdialog.js?skin=iblue"></script>
	</head>
	<div>
	<div class="easyui-tabs" style="width: auto; height:auto;">
			<div title="历史步骤列表" style="padding: 1px;">
				 <table id="dg" border="false">
				<thead>
					<tr>
						<!-- <th field="name">
							名称
						</th>
						<th field="processInstanceId" width="50">
							流程实例ID
						</th> -->
						<th field="taskname" width="50">
							步骤名称
						</th>
						
						<th field="starttime" width="50">
							开始时间
						</th>
						<th field="endtime" width="50">
							结束时间
						</th>
						<th field="assignee" width="50">
							处理人
						</th>
						<th field="comments" width="50">
							备注
						</th> 
						<th data-options="field:'opr',formatter:rowformater" width="30">
							处理结果
						</th>
					</tr>
				</thead>
			</table> 
			</div>
			<div title="流程追踪图"  
				style="padding: 1px">
				 <img src="${path}/sys/wf/traceImage.do?processInstanceId=${param.processInstanceId}&resourceType=image" >
			</div>
		</div>
		 
		<script>
var basePath = '${path}/sys/wf';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/processInstanceHistory.json?processInstanceId=${param.processInstanceId}";
	dg = $('#dg').datagrid({
	    url:listPath,
			loadFilter: function(r){ 
				if (r.command){ 
					return r.command;
				} else {
				return r;
			}
		},  
	    fitColumns:constants.fitColumns,
	    striped:true,
	    rownumbers:true
	});  
}); 
  
function rowformater(value,row,index){ 
	var v = row.deletereason;
	if(v == "completed"){
		v = "完成";
	}else if(v=="" || v ==null){
		v = "处理中";
	}
	return v;
}
</script>
</body>
</html>

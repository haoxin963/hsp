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
		<title>工作流</title>
		<jsp:include page="/com/easyui/inc.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<jsp:include page="/com/inc/incTreeCssJs.jsp"></jsp:include>
		<script type="text/javascript" src="${vpath}/sys/workflow/script/excanvas.js"></script>
		<script type="text/javascript" src="${vpath}/sys/workflow/script/wf.js"></script>
	</head>
	<script type="text/javascript">
	var currentWorkflow = null;
	var setting = {
		data: {
			simpleData: {
				enable: true,
				idKey:"workflowName",
			},
			key: { 
				name: "workflowName" 
			}
		},
		callback: { 
	        onClick: onTreeClickBack
	    }
	};
		 
	function onTreeClickBack(event, treeId, treeNode){ 
		var workflowName = treeNode.workflowName;
		
		$.get(path+'/sys/workflow/viewWorkflow.json',{workflowName:workflowName},function(result){
			if(1==result.status){
				var description;
				if(null != result.workflow.meta.description){
					description = result.workflow.meta.description;
				}else{
					description = '该工作流没有描述...';
				} 
				$('#description').text(description);
				currentWorkflow = result.workflow;
				var currentSteps = eval("([])");
				var historySteps = eval("([])");
				var canvas = $("#canvas");
				drawWf(canvas[0], result.workflow.xml, result.workflow.layout, currentSteps, historySteps);
			}else{
				$.messager.alert(constants.alertTitle, result.msg, 'error');
			}
		},'json');
	};
	
	function formHandler(tp){ 
		if(tp=='ok'){
			top.ymPrompt.getPage().contentWindow.doSave($('#dg'))
		}else if(tp=='cancel'){
			top.ymPrompt.close();
		}else if(tp=='close'){
			top.ymPrompt.close();
		}
	}

	function add(){
		if(null == currentWorkflow){
			jQuery.messager.alert(constants.alertTitle, '请先选择一个工作流', 'error');
		}else{
			jQuery.messager.confirm('Confirm','确认启动工作流?',function(r){
				if (r){
					//var url = path + '/sys/workflow/toInitialize.do?workflowName='+currentWorkflow.workflowName+'&actionId='+currentWorkflow.initActionId;
					var url = path + '/sys/workflow/toInitialize.do?workflowName='+currentWorkflow.workflowName;
					
					url+="&t="+new Date().getTime();
					titleTxt = '启动流程';
					var v = top.ymPrompt.win({title:titleTxt,autoClose:false,width:500,height:300,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'projectOnline',name:'projectOnline',src:url}});
									
				}
			});
		}
	}
	var zNodes = ${workflowJson};
	$(document).ready(function(){
	 	$.fn.zTree.init($("#tree"), setting, zNodes);
	}); 
	</script>
	<body class="easyui-layout" fit="true">
		<div data-options="region:'west',split:true" title="工作流列表"
			style="width: 180px;">
			<div class="zTreeDemoBackground left">
				<ul id="tree" class="ztree"></ul>
			</div>
		</div>
		<div data-options="region:'center',border:false"
			style="overflow: hidden;">
			<div class="toolbarDiv">
				<jsp:include page="/com/inc/toolbar.jsp">
					<jsp:param name="texts" value="启动流程"></jsp:param>											
					<jsp:param name="funs" value="add()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="icons" value="icon-add"></jsp:param>
				</jsp:include>
			</div>
			<div>
			<div id="description" style="padding:5px"></div>
			<canvas id="canvas" width="100%" height="100%">
			</canvas>
			</div>
		</div>
	</body>
</html>

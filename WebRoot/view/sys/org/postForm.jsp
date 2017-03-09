<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		
		<title>岗位信息</title>
	    <jsp:include page="/resource/inc/incMeta.jsp" />
        <jsp:include page="/resource/inc/incCssJs.jsp" />
        <jsp:include page="/resource/inc/incSimpleTree.jsp"/>
        <style type="text/css">
			.treediv{
				position:absolute;
				z-index:9px;				 
				width:200px;
				height:200px;
				overflow-y:auto;
				background-color:#eeeeee;		
			}
		</style>
	</head>

	<body class="bodyLayout">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
		</div>
		<f:form class="layui-form" action="${path}/sys/org/post/doSave.json"
			id="postForm" modelAttribute="command" method="POST">
		<table class="table_one">
				<tr>
					<f:hidden path="id" required='true' integer='true' maxlength='255' />
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							岗位代号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="postNo" maxlength='50' autocomplete="off"
								placeholder="请输入岗位代号" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				</tr>
				
				<tr>
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							职位 ${separator}</label>
						<div class="layui-input-block">
							<f:select path="positionId">
							<f:option value=""></f:option>
							<c:forEach items="${positionList}" var="v" >
								<option ${v.id == positionObj.id?'selected':''} value="${v.id}">${v.positionName}</option>							
							</c:forEach>
						</f:select>
						</div>
					</div>
				</td>
				</tr>
				
				
				<tr>
					<td >
						<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span> 所属部门 ${separator}</label>
			
						<f:hidden path="deptId" value="${deptObj.departmentId}" required='true' maxlength='255' />
					
			            <div class="layui-input-block">
							<input type="text" id="deptName" name="deptName" value="${deptObj.departmentName}" readonly  onclick="showDeptTree();" autocomplete="off"
							class="layui-input arrow_down " lay-verify="required" />
						</div>
						</div>
						</td>
				</tr>
				<tr>
					<td >
						<div class="layui-form-item">
						<label class="layui-form-label"> 上级岗位 ${separator}</label>
			
							<f:hidden path="parentId" value="${parentPost.id}" maxlength='255' />
					
			            <div class="layui-input-block">
							<input type="text" id="parentName" name="parentName" value="${parentPost.postName}" readonly   onclick="showPostTree(${not empty parentPost.id?'false':'true'});"  autocomplete="off"
							class="layui-input arrow_down " />
						</div>
						</div>
						</td>
				</tr>
		         
		         <tr>
					
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							岗位名称  ${separator}</label>
						<div class="layui-input-block">
							<f:input path="postName" maxlength='255' autocomplete="off"
								placeholder="请输入岗位名称" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				</tr>
				<tr>
					
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							岗位简称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="shortName" maxlength='255' autocomplete="off"
								class="layui-input" />
						</div>
					</div>
				</td>
				</tr>
		         <tr>
					
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							否是部门负责人 ${separator}</label>
						<div class="layui-input-block">
							<f:select path="isDeptHead">
							<f:option value="0" >否</f:option>
							<f:option value="1">是</f:option>							 
						</f:select>
						(一个部门只能有一个负责岗位)
						</div>
					</div>
				</td>
				</tr>
			
			   	<tr>
					
					<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							备注 ${separator}</label>
						<div class="layui-input-block">
							<f:textarea path="remark" maxlength='255'   class="layui-textarea"/>
						</div>
					</div>
				</td>
				</tr>
				<f:hidden path="hasChild" required='true' maxlength='1' />
				<f:hidden path="delTag" required='true' maxlength='1' />
				<f:hidden path="sortNo" integer='true' maxlength='255' />
			</table>
		</f:form>
	</body>

	<script> 
			
			var formId = "postForm";
			 
			function doSave(obj) {
				if (valid()) {
					$("#" + formId).ajaxSubmit({
						success : function(result) {
							result = jQuery.parseJSON(result);
							if (result.status == "1") {
								obj.closeAndRef();
							} else {
								alertError(result.msg);
							}
						}
					});

				}
			}
			
			//界面加载完毕
			$(function() { 
				
				
				//检查岗位代号是否存在
				$("#postNo").blur(function(){
					//checkPosNoExist
					var $pno= $("#postNo");
					if($pno.val()==""||$.trim($pno.val())==""){
						return;
					}
					$.ajax({
						url:"${path}/sys/org/post/checkPosNoExist.json?postNo="+$pno.val(),
						method:"post",
						dataType:"json",
						success:function(result){
							if(result.status=="1"){
								top.jQuery.messager.alert(constants.alertTitle,"岗位代号不能重复！");
								$pno.val("");
							}
						}					
					});
				});
				
				$("#postName").blur(function(){
					if($(this).val()!=""&&$.trim($(this).val())!=""){
						$("#shortName").val($(this).val());
					}		
				});				
				 
			});
			
			
			
			//显示岗位树结构
			var postTree;			
			function initPostTree(){
				//配置
				var setting = {
					data: {
						simpleData: {
							enable: true,
							idKey:"id",
							pIdKey:"parentId"
						}		
					},					 
					callback: { 
						//点击事件
				        onClick:function(){
							var nodes = postTree.getSelectedNodes();
							if(nodes.length>0){
								var id = $("#id").val();
								var nid = nodes[0].id;
								var nname = nodes[0].name;
								//不可以自己选自己
								if(id==nid){
									top.jQuery.messager.alert("提示","不可以选择自己作为上级岗位！","error");
									return;
								}					
								$("#parentId").val(nid);
								$("#parentName").val(nname);
								closePostTree();
							}
						}
				    }  
				};
			
				$.ajax({
		            url: '${path}/system/pubmodule/doTree.do',
		            data: {
		            	"filter[serviceBeanId]": "postServiceImpl",
		            	"filter[showType]":"1"
		            },
		            type: 'POST',
		            dataType: "json",
		            success: function(result) {  
		               	postTree = $.fn.zTree.init($("#ptree"), setting, result);
		            }
		        });
			}
			
			/**
			*展示岗位树结构
			*/		
			function showPostTree(isAdd){
				if(!isAdd){
					return false;
				}
				if($("#treediv").length>0){
					$("#treediv").remove();					
				}else{			
					var $postTreeDiv = $('<div id="treediv" class="treediv"><div style="text-align:right;height:15px;overflow:hidden;padding-right:5px;"><a style="font-size:15px;" href="javascript:void(0)" onclick="closetree(\'parentId\',\'parentName\')">×</a></div><ul id="ptree" class="ztree"></ul></div>');
					$(document.body).append($postTreeDiv);
					var l = $("#parentName").offset().left;
					var t = $("#parentName").offset().top;
					t += $("#parentName").height()+5;
					$postTreeDiv.css({"left":l+"px","top":t+"px"});
					initPostTree();					
					 
					$(document).click(function(e){
						destoryPostTree(e);
					});
				}
			}
			
			function closetree(valId,nameId){
				$("#"+valId).val("");
				$("#"+nameId).val("");
				closePostTree();
				closeDeptTree();				
			}
			
			 
			var destoryPostTree = function(e){
				var $treediv = $("#treediv");
				if($treediv.length==0){
					return;
				}
				var x = e.clientX;
				var y = e.clientY;
				var treeX = $treediv.offset().left;
				var treeY = $treediv.offset().top;
				var treeW = $treediv.width();
				var treeH = $treediv.height();
				if(!((x>treeX&&x<(treeX+treeW))&&(y>treeY&&y<(treeY+treeH)))&&e.target.id!="parentName"){
					closePostTree();
				}
			}
			
			function closePostTree(){
				if($("#treediv").length>0){
					$("#treediv").remove();
					//$(document).unbind("click",destoryPostTree);
				}
			}			
			
			//显示部门树结构
			var deptTree;			
			function initDeptTree(){
				//配置
				var setting = {
					data: {
						simpleData: {
							enable: true,
							idKey:"id",
							pIdKey:"parentId"
						}
					},		 
					callback: { 
						//点击事件
				        onClick:function(){
							var nodes = deptTree.getSelectedNodes();
							if(nodes.length>0){
								var nid = nodes[0].id;
								var nname = nodes[0].name;								 				
								$("#deptId").val(nid);
								$("#deptName").val(nname);
								closeDeptTree();
							}
						}
				    }  
				};
			
				$.ajax({
		            url: '${path}/system/pubmodule/doTree.do',
		            data: {
		            	"filter[serviceBeanId]": "departmentServiceImpl"		            	 
		            },
		            type: 'POST',
		            dataType: "json",
		            success: function(result) {  
		               	deptTree = $.fn.zTree.init($("#depttree"), setting, result);
		            }
		        });
			}
			
			/**
			*展示岗位树结构
			*/		
			function showDeptTree(){										 		 
				if($("#depttreediv").length>0){
					$("#depttreediv").remove();					
				}else{			
					var $deptTreeDiv = $('<div id="depttreediv" class="treediv"><div style="text-align:right;height:15px;overflow:hidden;padding-right:5px;"><a style="font-size:15px;" href="javascript:void(0)" onclick="closetree(\'deptId\',\'deptName\')">×</a></div><ul id="depttree" class="ztree"></ul></div>');
					$(document.body).append($deptTreeDiv);
					var l = $("#deptName").offset().left;
					var t = $("#deptName").offset().top;
					t += $("#deptName").height()+5;
					$deptTreeDiv.css({"left":l+"px","top":t+"px"});
					initDeptTree();					
					 
					$(document).click(function(e){
						destoryDeptTree(e);
					});
				}
			}
			 
			var destoryDeptTree = function(e){
				var $treediv = $("#depttreediv");
				if($treediv.length==0){
					return;
				}
				var x = e.clientX;
				var y = e.clientY;
				var treeX = $treediv.offset().left;
				var treeY = $treediv.offset().top;
				var treeW = $treediv.width();
				var treeH = $treediv.height();
				if(!((x>treeX&&x<(treeX+treeW))&&(y>treeY&&y<(treeY+treeH)))&&e.target.id!="deptName"){
					closeDeptTree();
				}
			}
			
			function closeDeptTree(){
				if($("#depttreediv").length>0){
					$("#depttreediv").remove();
					//$(document).unbind("click",destoryDeptTree);
				}
			}								 
		</script>
</html>
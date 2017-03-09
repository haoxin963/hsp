<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>		
		<title>部门新增</title>
			<jsp:include page="/resource/inc/incMeta.jsp"/>
		<jsp:include page="/resource/inc/incCssJs.jsp"/>
	
	</head>

	<body class="bodyLayout">
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
		<f:form class="layui-form"
			action="${path}/sys/org/department/doSave.json" id="departmentForm"
			modelAttribute="command" method="POST">
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd">
					     <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							上级部门  ${separator}</label>
						<div class="layui-input-block">							
							<c:if test="${empty parentId}">						
							<input type="text" name="parentName" value="${parentObj.departmentName}" readonly  placeholder="请输入上级部门" class="layui-input" lay-verify="required" />
							<input type="hidden" name="parentId" value="${command.parentId}"/>
						</c:if>
						<c:if test="${!empty parentId}">
							<input type="text" name="parentName" value="${parentName}" readonly  placeholder="请输入上级部门" class="layui-input" lay-verify="required" />
							<input type="hidden" name="parentId" value="${parentId}"/>
						</c:if>	
						</div>
						</div>
					</td>
				
				</tr>				
				<tr>
					<f:hidden path="departmentId" required='true' integer='true' maxlength='255' />
					<td class="lableTd">
					     <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							部门编号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="departmentNumber" maxlength='16' autocomplete="off"
								readonly="${command.departmentId!=null?'true':'false'}"
								placeholder="请输入部门编号" class="layui-input" lay-verify="required" />
						</div>
						</div>
						
					</td>
				</tr>				
				<tr>					
					<td class="lableTd">
					        <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="departmentName" maxlength='64' autocomplete="off"
								placeholder="请输入名称" class="layui-input" lay-verify="required" />
						</div>
						</div>
					
					</td>
				
				</tr>				
				<tr>
					<td class="lableTd">
					       <div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							简称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="shortName" maxlength='50' autocomplete="off"
								placeholder="请输入简称" class="layui-input" lay-verify="required" />
						</div>
						</div>
						
				</tr>	
				
				
				<tr>
				  
					<td class="lableTd">
						   <div class="layui-form-item">
						<label class="layui-form-label">描述 ${separator}</label>
						<div class="layui-input-block">
							<f:textarea path="departmentIntroduction" class="layui-textarea"  maxlength='255' />
						</div>
					</div>
					</td>
								 
				</tr>	
				
				<tr>
				
					<td>
					           <div class="layui-form-item">
						<label class="layui-form-label">
							</label>
						<div class="layui-input-block">
							<input type="checkbox" id="displayorg" ${command.isdisplay eq '0'?'':'checked'} title="显示在组织机构中"/>
						</div>
						</div>
						<f:hidden path="isdisplay" required='true' value="${command.isdisplay==null?'1':command.isdisplay}" />
					</td>					 
				</tr>			
				<tr>
					
						<f:hidden path="sortNo" required='true' maxlength='255' />
						<f:hidden path="child" required='true' maxlength='1' />
						<f:hidden path="domain" required='true' maxlength='20' />				
						<f:hidden path="delTag" required='true' maxlength='1' />
				
				</tr>
			</table>
		</f:form>
	</body>

	<script> 
			var serverURL = "${path}/sys/org/department";
			var formId = "departmentForm";
			 
			function doSave(win){ 
				if (valid()) {
						$("#" + formId).ajaxSubmit({
							success : function(result) {
								result = jQuery.parseJSON(result);
								if (result.status == "1") {
									win.closeAndRef();
									 win.zTree.destroy();
									 win.createTree();
								} else {
									alertError(result.msg);
								}
							}
						});
					
				} 
			} 
			 

			

			$(function() { 
			
				
			
				
				$("#displayorg").click(function(){
					var isdisplay = $(this).attr("checked")?'1':'0';
					$("#isdisplay").val(isdisplay);
				}); 
				
				//简称自动默认成名称
				$("#name").blur(function(){				 
					var regexp =/^[\u4E00-\u9FA5\w]+$/;
					if(!regexp.test($(this).val())){
						top.jQuery.messager.alert("提示","名称不能含中文、字母、数字以及下划线以外的其他特殊字符","info");
						$(this).val("");
						return;
					}
					$("#shortName").val($(this).val());
				});
			});
		</script>
</html>
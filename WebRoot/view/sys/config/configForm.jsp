<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
		<title>TaskEntity</title>
		<jsp:include page="/resource/inc/incMeta.jsp" />
         <jsp:include page="/resource/inc/incCssJs.jsp" />
	</head>
	<body class="bodyLayout">
		<c:if test="${1 eq devTag}">
			<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					

					<jsp:param name="texts" value="新增参数"></jsp:param>
					<jsp:param name="funs" value="toAddConfigItem()"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="texts" value="保存"></jsp:param>
					<jsp:param name="funs" value="doSave()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="texts" value="应用更新"></jsp:param>
					<jsp:param name="funs" value="doReload()"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="icons" value="&#xe68f;"></jsp:param> 
				</jsp:include>
				</span>
		</div>
		</c:if>
		<c:if test="${1 ne devTag}">
				<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="保存"></jsp:param>
					<jsp:param name="funs" value="doSave()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
 					<jsp:param name="texts" value="应用更新"></jsp:param>
					<jsp:param name="funs" value="doReload()"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="icons" value="&#xe68f;"></jsp:param> 
				</jsp:include>
				</span>
		</div>
		</c:if>
		<f:form class="layui-form" action="${path}/sys/config/editConfig.json?custId=${param.custId}" id="taskForm" modelAttribute="command"
			method="POST">
			<input type="hidden" name="itemGroup" value="${itemGroup}" />
			<table class="table_one">
				<c:forEach var="config" items="${configs}">
					<tr>
						<td  colspan=2>
                            <div class="layui-form-item">
						<label class="layui-form-label">
								${config.configItem.itemName}&nbsp;(${config.configItem.itemKey})${separator}</label>
						<div class="layui-input-block">
						    <c:if test="${'text' == config.configItem.itemType}">
							<input type="text" name="${config.configItem.itemKey}" maxlength='50' value="${config.value}"  autocomplete="off"
								class="layui-input" />
							 </c:if>
							 <c:if test="${'single' == config.configItem.itemType}">
								<c:if test="${fn:length(config.configItem.itemValues)<=5}">
									<c:forEach var="itemValue" items="${config.configItem.itemValues}">
									    <input name="${config.configItem.itemKey}" type="radio" value="${itemValue.value}"  title="${itemValue.valueName}"
									          <c:if test="${itemValue.value == config.value}">checked="checked"</c:if>>
										</c:forEach>
								</c:if>
								<c:if test="${fn:length(config.configItem.itemValues)>5}">
									<select name="${config.configItem.itemKey}">
										<c:forEach var="itemValue" items="${config.configItem.itemValues}">
											<option value="${itemValue.value}" <c:if test="${itemValue.value == config.value}">selected="selected"</c:if>>
												${itemValue.valueName}
											</option>
										</c:forEach>
									</select>
								</c:if>
							</c:if>
							<c:if test="${'multiple' == config.configItem.itemType}">
								<c:forEach var="itemValue" items="${config.configItem.itemValues}">
									<input name="${config.configItem.itemKey}" type="checkbox" value="${itemValue.value}" title="${itemValue.valueName}"
										<c:if test="${fn:contains(config.valueList,itemValue.value)}">checked="checked"</c:if>>
									</c:forEach>
							</c:if>
						</div>
					</div>						
 						
						
						</td>
					</tr>
				</c:forEach>
			</table>
		</f:form>
		
	</body>
	<script> 
			function doReload(){
				var vurl = '${path}/sys/config/reloadConfigs.json';
				jQuery.ajax({
					type: 'POST',
					url:vurl,
					data: {},
					dataType: "json",
					success: function(result){
						if(result.status==1){
							top.jQuery.messager.alert("提示","数据刷新成功", 'info');
							//top.ymPrompt.close(); 
						}else{
							top.jQuery.messager.alert("提示","操作失败，请稍后再试！", 'error');
						}
					}
					
				});
			}
			
			function toAddConfigItem(){
				var actionURL = "${path}/sys/config/toAddConfigItem.do?itemGroup=${itemGroup}&custId=${param.custId}";
				var bt = [{name: constants.save,callback:buttonHandler,focus: true},{name: constants.cancel}];
				dialogW = jQuery.dialog({
					title: constants.newForm,
					width: 850,
					height:300,
				    id: 'configDialog', 
				    content: 'url:'+actionURL,
				    lock:true,
				    button: bt
				});
				//var v = top.ymPrompt.win({title:constants.newForm,autoClose:false,width:600,height:380,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'task',name:'task',src:actionURL}});
			}
			
			function buttonHandler(tp){ 
				dialogW.content.doSave(window);
				return false;
			}

			function formSaveSuccessCallback(r){
				top.jQuery.messager.alert(constants.alertTitle,constants.success);
				window.location.reload(true);
				dialogW.close();
			}
			
			var serverURL = "${path}/sys/config/editConfig";
			var formId = "taskForm";
			 
			function doSave(obj){ 
				if($("#"+formId).valid()){
					$("#"+formId).form({
						onSubmit: function(){
							 
						},
						success:function(result){
							result = jQuery.parseJSON(result);
							if(result.status == 1){
								obj.formSaveSuccessCallback(result);
							}else{
								top.jQuery.messager.alert(constants.alertTitle,result.msg);
							}
						}
					}); 
					$('#'+formId).submit(); 
				}
			} 
			$().ready(function() { 
				var v = $("#"+formId).validate(); 
			});
		</script>
</html>
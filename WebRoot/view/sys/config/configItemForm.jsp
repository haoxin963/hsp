<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>参数项</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>
	<body class="bodyLayout">
		<f:form class="inputForm"
			action="${path}/sys/config/addConfigItem.json" id="configItemForm"
			modelAttribute="command" method="POST">
			<input type="hidden" name="custId" maxlength='50' value="${param.custId }" />
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd">
						参数标志 ${separator}
					</td>
					<td class="valueTd">
						<input type="text" name="itemKey" maxlength='50' />
					</td>
					<td class="lableTd">
						参数名 ${separator}
					</td>
					<td class="valueTd">
						<input type="text" name="itemName" maxlength='50' />
					</td>
				</tr>
				<tr>
					<td class="lableTd">
						参数类型 ${separator}
					</td>
					<td class="valueTd">
						<select id="itemType" name="itemType">
							<option value="text">
								文本
							</option>
							<option value="single">
								单选
							</option>
							<option value="multiple">
								多选
							</option>
						</select>
					</td>
					<td class="lableTd">
						参数组 ${separator}
					</td>
					<td class="valueTd">
						${itemGroup.itemGroupName}
						<input type="hidden" name="itemGroup" value="${itemGroup.itemGroup}">
					</td>
				</tr>
			</table>
			<br />
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%" id="itemValueTbl">
				<tr>
					<td class="lableTd" style="width: 15%">
						参数值${separator}
					</td>
					<td class="valueTd" colspan="3" style="width: 85%">
						<a href="#" onclick="addItemValue()" class="easyui-linkbutton"
							data-options="plain:true,iconCls:'icon-add'">新增</a>
					</td>
				</tr>
				<tr>
					<td class="lableTd" style="width: 15%; text-align: center;">
						值名称
					</td>
					<td class="lableTd" style="width: 15%; text-align: center;">
						值
					</td>
					<td class="lableTd" colspan="2"
						style="width: 70%; text-align: center;">
						操作
					</td>
				</tr>
			</table>
		</f:form>
	</body>
	<script> 
			var i = 0;
			function addItemValue(){
				var html ='';
				html += '<tr id="itemValueTr'+i+'">';
				html += '<td class="valueTd" style="width: 15%;text-align: center;">';
				html += '<input style="width:80px;" type="text" name="itemValues['+i+'].valueName">';
				html += '</td>';
				html += '<td class="valueTd" style="width: 15%;text-align: center;">';
				html += '<input style="width:80px;" type="text" name="itemValues['+i+'].value">';
				html += '</td>';
				html += '<td class="valueTd" colspan="2"  style="width: 70%;text-align: center;">';
				html += '<a href="#" onclick="removeItemValue('+i+')">删除</a>';
				html += '</td>';
				html += '</tr>';
				$('#itemValueTbl').append(html);
				i++;
			}
			
			function removeItemValue(index){
				$('#itemValueTr'+index).remove();
			}
			var serverURL = "${path}/sys/config/addConfigItem";
			var formId = "configItemForm";
			 
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
			
			function changeType(){
				var type = $('#itemType').val();
				if('text' == type){
					$('#itemValueTbl').css('display','none');
					$('tr[id*=itemValueTr]').remove();
				}else{
					$('#itemValueTbl').css('display','');
				}
			}
			$().ready(function() { 
				var v = $("#"+formId).validate(); 
				$('#itemType').change(function(){
					changeType();
				});
				changeType();
			});
		</script>
</html>
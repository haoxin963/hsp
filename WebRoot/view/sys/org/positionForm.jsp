<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>职位信息</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<style type="text/css">
.inputForm fieldset {
	font-family: "微软雅黑";
	border: 1px solid #E6E6E6;
	background-color: #FFFFFF;
	margin-top: 15px;
}

.inputForm fieldset legend {
	font-size: 15px;
}

.lableTd {
	width: 18%;
}
legend{
    font-size:16px;
}
</style>
</head>

<body class="bodyLayout">
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
	<f:form class="layui-form"
		action="${path}/sys/org/position/doSave.json" id="positionForm"
		modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<f:hidden path="id" required='true' integer='true' maxlength='255' />
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							职位代号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="positionNo" maxlength='50' autocomplete="off"
								readonly="${command.id!=null?'true':'false'}"
								placeholder="请输入职位代号" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>

				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							职位名称 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="positionName" maxlength='255' autocomplete="off"
								placeholder="请输入职位名称" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
			<tr>

				<td colspan=2>
					<div class="layui-form-item">
						<label class="layui-form-label">备注 ${separator}</label>
						<div class="layui-input-block">
							<f:textarea path="remark" class="layui-textarea" />
						</div>
					</div>
				</td>


			</tr>
			<f:hidden path="delTag" required='true' maxlength='1' />
			<f:hidden path="refrole" maxlength='1' />
		</table>

		<%--职位新增--%>
		<c:if test="${empty command.id}">
			<fieldset>
				<legend>对应角色</legend>
			<table class="table_one">
			<tr>
				<td colspan="4">
			<input type="radio" id="temprole" name="rfrole" value="1"
						checked title="无对应角色" />
					</td>
					</tr>
					<tr>
				<td  colspan="4">
					<input type="radio" id="newrole" name="rfrole" value="2"
						title="自动生成角色（角色代号为职位代号，角色名称为职位名称）" />	</td>
					</tr>
					<tr>
				<td><input type="radio" id="selrole" name="rfrole" value="3"
						title="选择角色" />
				</td>
				<td>		
						 <select id="rolebox" name="selrole"
						disabled="true">
							<option value=""></option>
							<c:forEach items="${roleList}" var="r">
								<option value="${r.id}">${r.roleName}</option>
							</c:forEach>
					</select> 
					</td><td>
					<input type="text" id="rolekeyword" class="layui-input" />
					</td><td>
					 <input type="button"
						id="filterrole" value="筛选" onclick="filterRole()" class="layui-btn" />	</td>
					</tr>
				
			</fieldset>

		</c:if>

		<%--职位修改--%>
		<c:if test="${not empty command.id}">
			<fieldset>
				<legend>对应角色</legend>
				<table class="table_one">
			<tr>
				<td colspan="4"><input type="radio" id="temprole" name="rfrole" value="1"
						${empty command.refrole?'checked':''}   title="无对应角色" /> 
					</td></tr>
					
			<tr>
				<td colspan="4"><input type="radio" id="newrole" name="rfrole" value="2"
						${not empty command.refrole?'disabled':''} 	title="自动生成角色（角色代号为职位代号，角色名称为职位名称）"  /> </td></tr>
					<tr><td><input type="radio" id="selrole" name="rfrole" value="3"
						${not empty command.refrole?'checked':''}  title="选择角色" /> 
						</td>
						<td>
						<select id="rolebox" name="selrole"
						${empty command.refrole?'disabled':''}>
							<option value=""></option>
							<c:forEach items="${roleList}" var="r">
								<option ${command.refrole==r.id?'selected':''} value="${r.id}">${r.roleName}</option>
							</c:forEach>
					</select> 
					</td>
					<td>
					<input type="text" id="rolekeyword"  class="layui-input"/> 
					</td>
					<td>
					<input type="button"
						id="filterrole" value="筛选" onclick="filterRole()" class="layui-btn"/></td></tr>
			</table>
			</fieldset>
		</c:if>
	</f:form>
</body>

<script>
	var serverURL = "${path}/sys/org/position";
	var formId = "positionForm";
	var basePath = "${path}";
	
	function doSave(obj) {
		if (valid()) {
			//判断选择角色的时候是否有选中
			var $ckrd = $("input:radio[name='rfrole']:checked");
			if ($ckrd.val() == "3") {
				var $rbox = $("#rolebox");
				if ($rbox.val() == "") {
					alertError("请选择相应的角色！");
					return;
				}
			}

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

	/**
	 *筛选角色列表
	 */
	function filterRole() {
		var $keyword = $("#rolekeyword");
		$.ajax({
			url : "${path}/sys/org/position/doGetRolesByKeyword.json?keyword="
					+ $keyword.val(),
			method : "post",
			dataType : "json",
			success : function(result) {
				if (result.roleList && result.roleList.length > 0) {
					$("#rolebox option").remove();
					$("#rolebox").append("<option value=''></option>");
					$(result.roleList).each(
							function(i, n) {
								$("#rolebox").append(
										"<option value=\""+n.id+"\">"
												+ n.roleName + "</option>")
							});
				} else {
					$("#rolebox option").remove();
				}
			}
		});
	}

	$(function() {

		$("input:radio[name='rfrole']").click(function() {
			var radio = $(this);
			$("#rolebox").attr("disabled", "true");
			if ($(this).val() == "1") {
				$("#refrole").val("");
			} else if ($(this).val() == "2") {
				$("#refrole").val("@CREATEROLE");
			} else if ($(this).val() == "3") {
				$("#rolebox").removeAttr("disabled");
				$("#refrole").val($("#rolebox").val());
			}
		});

		$("#rolebox").change(function() {
			$("#refrole").val($(this).val());
		});

		//职位编码验证重复
		$("#positionNo")
				.blur(
						function() {
							var $psid = $("#id");
							var $pno = $("#positionNo");
							if ($pno.val() == "" || $.trim($pno.val()) == "") {
								//parent.jQuery.messager.alert(constants.alertTitle,"职位代码必填");
								return;
							}
							//新增时候验证编码
							if ($psid.val() == "") {
								$
										.ajax({
											url : "${path}/sys/org/position/doCheckPositionNo.json?positionNo="
													+ $pno.val(),
											method : "post",
											dataType : "json",
											success : function(result) {
												if (result.status == '1') {
													//编码已经存在
													alertError(result.msg);
													$pno.val("");
													return;
												}
											}
										});

							}
						});
	});
</script>
</html>
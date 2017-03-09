<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>pubmodule_notice_tbl</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>
<body class="bodyLayout">
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" >
	
			<jsp:param name="texts" value="直接发布"></jsp:param>
			<jsp:param name="texts" value="保存"></jsp:param>
			<jsp:param name="texts" value="保存并继续"></jsp:param>
			<jsp:param name="texts" value="清空"></jsp:param>
			<jsp:param name="texts" value="返回"></jsp:param>
			<jsp:param name="funs" value="doRelease();"></jsp:param>
			<jsp:param name="funs" value="doSave();"></jsp:param>
			<jsp:param name="funs" value="doSaveNew();"></jsp:param>
			<jsp:param name="funs" value="doNew();"></jsp:param>
			<jsp:param name="funs" value="doBack();"></jsp:param>
			<jsp:param name="ids" value="id1"></jsp:param>
			<jsp:param name="ids" value="id2"></jsp:param>
			<jsp:param name="ids" value="id3"></jsp:param>
			<jsp:param name="ids" value="id4"></jsp:param>
			<jsp:param name="ids" value="id5"></jsp:param>
			<jsp:param name="icons" value="&#xe6df;"></jsp:param>
			<jsp:param name="icons" value="&#xe600;"></jsp:param>
			<jsp:param name="icons" value="&#xe600;"></jsp:param>
			<jsp:param name="icons" value="&#xe6e2"></jsp:param>
			<jsp:param name="icons" value="&#xe6e2"></jsp:param>
		</jsp:include>
	</span>
	</div>
	<f:form class="layui-form"
		action="${path}/sys/notice/notice/doSave.json" id="noticeForm"
		modelAttribute="command" method="POST">
		<table class="table_one">
			<tr>
				<f:hidden path="id" required='true' integer='true' maxlength='255' />
				<f:hidden path="delTag" required='true' maxlength='255' />
				<f:hidden path="sortNo" required='true' maxlength='255' />
				<f:hidden path="createTime" required='true' maxlength='255' />
				<f:hidden path="createUserId" required='true' integer='true'
					maxlength='255' />
				<f:hidden path="alluser" required='true' integer='true'
					maxlength='255' />
				<f:hidden path="hits" required='true' integer='true' maxlength='255' />
				<f:hidden path="status" required='true' integer='true'
					maxlength='255' />
					<td colspan=2>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							 标题 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="title" maxlength='200' autocomplete="off"
								placeholder="请输入标题" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
			     	<td >
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							 信息类型 ${separator}</label>
						<div class="layui-input-block">
						<f:hidden path="categoryId" required='true'
						maxlength='20' />
							<input type="text"  maxlength='200' autocomplete="off"
								class="layui-input" lay-verify="required refTree"    id="categoryName" 	readOnly="readOnly" />
						</div>
					</div>
				</td>
					<td>
					<div class="layui-form-item">
						<label class="layui-form-label">
							 特殊标签 ${separator}</label>
						<div class="layui-input-block">
							<input type="checkbox" name="ismajor" title="重要公告"
					id="ismajor" value="1"
					<c:if test="${ command.ismajor == 1 }">						
							checked="true"
						</c:if> />

					&nbsp;&nbsp;&nbsp; <input type="checkbox" name="iscomment"  title="允许评论"
					id="iscomment" value="1"
					<c:if test="${ command.iscomment == 1 }">						
							checked="true"
						</c:if> />
					<c:if test="${ command.alluser == 0 }"> 
							 &nbsp;&nbsp;&nbsp;
							<input type="checkbox" name="isback" id="isback" value="1" title="允许评论"
							<c:if test="${ command.isback == 1 }">						
								checked="true"
							</c:if> />
						 </c:if>
						</div>
					</div>
				</td>
			
			</tr>
			<tr>
			     <td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							 生效日期 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="taskopen" maxlength='19' autocomplete="off"
								placeholder="请输入生效日期" class="layui-input"  onclick="layui.laydate({elem: this, istime: true, format: 'YYYY-MM-DD '})" />
						</div>
					</div>
				</td>
				 <td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							 关闭日期 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="taskclose" maxlength='19' autocomplete="off"
								placeholder="请输入关闭日期" class="layui-input"  onclick="layui.laydate({elem: this, istime: true, format: 'YYYY-MM-DD '})" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
			<td colspan=2>
			<div class="layui-form-item">
						<label class="layui-form-label">
							上传 ${separator}</label>
					
							<f:input type="hidden"
						path="attachpath" maxlength='50' /> <jsp:include
						page="/resource/swfupload/swfUpload.jsp">
						<jsp:param name="filePath" value="${command.attachpath}" />
						<jsp:param name="type"
							value="*.jpg;*.doc;*.png;*.docx;*.xls;*.xlsx;*.zip;*.rar;*.bmp;*.ppt;*.gif;*.txt;" />
						<jsp:param name="isDB" value="true" />
						<jsp:param name="single" value="false" />
						<jsp:param name="rename" value="false" />
						<jsp:param name="fileSize" value="20000" />
						<jsp:param name="btnText" value="上传文件" />
						<jsp:param name="mode" value="auto" />
						<jsp:param name="isListExisting" value="true" />
						<jsp:param name="isListConsole" value="true" />
						<jsp:param name="compressPic" value="1,0,0,1,0.8" />
						<jsp:param name="seqId" value="1" />
					</jsp:include>
					</div>
			</tr>
			<tr>
			<td  colspan=2>
			<div class="layui-form-item">
						<label class="layui-form-label">
							 信息内容 ${separator}</label>
						<div class="layui-input-block">
						<f:textarea path="content" />
						<jsp:include page="/resource/inc/incEditor.jsp">
						<jsp:param name="inputId" value="content"></jsp:param>
					</jsp:include>
						</div>
					</div>
			</td>		
			
			</tr>
		</table>
	</f:form>
</body>
<script>
	var baseURL = "${path}/sys/notice/notice/";
	//列表
	var allListURL = "doAllList.do";
	var userListURL = "doUserList.do";
	//保存
	var saveURL = "doSave.do";
	//加载
	var loadURL = "doLoad.do?alluser=" + ${command.alluser};
	//发布
	var releaseURL = "doRelease.json";
	var formId = "noticeForm";

	var alluser = ${command.alluser};
	
	layui.use(['laydate'], function() {
		var laydate = layui.laydate;
});
	function checkForm() {
		return valid();
	}

	function doNew() {
		self.location = baseURL + loadURL;
	}

	function doBack() {
		if (alluser == 1) {
			self.location = baseURL + allListURL;
		} else {
			self.location = baseURL + userListURL;
		}
	}

	function doRelease(obj) {
		if (valid()) {
			parent.layer.confirm('确认提交?', function(index) {
				$('#' + formId).attr("action", baseURL + releaseURL);
				$("#" + formId).ajaxSubmit({
					success : function(result) {
						result = jQuery.parseJSON(result);
						if (result.status == "1") {
							obj.closeAndRef();
							doBack();
						} else {
							alertError(result.msg);
						}
					}
				});
			});
		}
	}

	function doSaveNew(obj) {
		if (valid()) {
			parent.layer.confirm('确认提交?', function(index) {
				$("#" + formId).ajaxSubmit({
					success : function(result) {
						result = jQuery.parseJSON(result);
						if (result.status == "1") {
							obj.closeAndRef();
							doNew();
						} else {
							alertError(result.msg);
						}
					}
				});
		});
		}
	}

	function doSave(obj) {
		if (valid()) {
			parent.layer.confirm('确认提交?', function(index) {
				$("#" + formId).ajaxSubmit({
					success : function(result) {
						result = jQuery.parseJSON(result);
						if (result.status == "1") {
							obj.closeAndRef();
							doBack();
						} else {
							alertError(result.msg);
						}
					}
				});
		});
		}
	}
</script>
</html>
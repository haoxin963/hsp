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
	<title>pubmodule_noticecomment_tbl列表</title>
	<jsp:include page="/com/inc/incCssJs.jsp">
		<jsp:param name="com" value="core" />
	</jsp:include>
	<jsp:include page="/com/inc/jsonList.jsp">
		<jsp:param name="command" value="command"></jsp:param>
	</jsp:include>
	<style type="text/css">
	.title {
		font-family: "黑体";
		font-size: 14px;
		height:30px;
	}
	.commTitle {
		font-family: "黑体";
	}
</style>
		<script type="text/javascript"
			src="${vpath}/sys/notice/script/noticeView.js"></script>
			
	</head>
	<body class="bodyLayout">
		<div class="toolbarDiv">
			<jsp:include page="/com/inc/toolbar.jsp">
				<jsp:param name="texts" value="返回"></jsp:param>
				<jsp:param name="funs" value="doBack();"></jsp:param>
				<jsp:param name="ids" value="id1"></jsp:param>
				<jsp:param name="icons" value="icon-back"></jsp:param>
			</jsp:include>
		</div>
		<table cellspacing="1" cellpadding="1" border="0" align="center" width="99%">
			<tr>
				<td class="title" align="center"> 
					<b>${command.notice.title}</b> 
				</td>
			</tr>
			<tr>
				<td class="valueTd" align="center"> 
					发布人：${command.notice.releaseUserName}&nbsp;&nbsp;发布时间：${command.notice.taskopen}
				</td>
			</tr>
			<tr>
				<td>
					${command.notice.content}
				</td>
			</tr>
			 
			<tr>
				<td class="valueTd"><br/>
					<jsp:include page="/com/swfupload/viewUpload.jsp">
						<jsp:param name="filePath" value="${command.notice.attachpath}" />
						<jsp:param name="showIcon" value="true" />
					</jsp:include>
				</td>
			</tr>
			 
			<c:if test="${ not empty command.notice.updateUserId }">	
			<tr>
				<td class="valueTd" align="right"><br/>
				${command.notice.updateUserName} 于 ${command.notice.updateTime} 最终编辑。
					
				</td>
			</tr>
			</c:if>	
		</table>
		<c:if test="${ command.notice.iscomment == 1 }">
		<c:if test="${ command.total > 0}">
		<br/><br/>
		<table id="dg" width="100%" height="100%">
			<thead>
				<tr id="gridThead">
					<th data-options="width:600,field:'id',formatter:rowComment">
						<span class="commTitle" style="font-size:14px;">已有评论</span>
					</th>
				</tr>
			</thead>
		</table>
		</c:if>
		<br/>
		<table cellspacing="1" cellpadding="1" border="0" width="60%"  align="center">
			<tr>
				<td class="valueTd">
					<b>发表评论</b>
				</td>
			</tr>
			<tr>
				<td class="valueTd" align="center">
					<textarea name="commentContent" cols="80" rows="10" id="commentContent"></textarea>
					<br/><br/>
  					<input type="button" name="button" id="button" value="发表评论" onClick="commentSubmit(${command.notice.id})" />
				</td>
			</tr>
		</c:if>
		</table>
	</body>
</html>

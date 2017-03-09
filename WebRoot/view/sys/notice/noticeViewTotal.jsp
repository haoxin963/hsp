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
		<title>pubmodule_notice_tbl</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
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
		<table cellspacing="1" cellpadding="1" border="0"
			class="inputFormTable" width="100%">
			<tr>
				<td class="lableTd">
					标题 ${separator}
				</td>
				<td class="valueTd">
					${command.notice.title}
				</td>
			</tr>
			<tr>
				<td class="lableTd">
					点击量 ${separator}
				</td>
				<td class="valueTd">
					${command.notice.hits}
					
				</td>
			</tr>
<!-- 
			<tr>
				<td class="lableTd">
					阅读人数 ${separator}
				</td>
				<td class="valueTd">
					
				</td>
			</tr>
			<tr>
				<td class="lableTd">
					阅读人 ${separator}
				</td>
				<td class="valueTd">
					
				</td>
			</tr>
 -->
			<tr>
				<td class="lableTd">
					评论次数 ${separator}
				</td>
				<td class="valueTd">
					<a href="../noticeComment/doList.do?noticeId=${command.notice.id}" target="_self">${command.commentCount}</a>
				</td>
			</tr>
			<tr>
				<td class="lableTd">
					最新评论 ${separator}
				</td>
				<td class="valueTd">
					<c:if test="${ not empty command.comment }">						
						${command.comment.userName}&nbsp;(${command.comment.dateTime})</br>
						${command.comment.content}
					</c:if>
				</td>
			</tr>
			<c:if test="${ command.notice.alluser == 0 }">
			<tr>
				<td class="lableTd">
					未阅读人 ${separator}
				</td>
				<td class="valueTd">.
					
				</td>
			</tr>
			</c:if>
		</table>
	</body>
		<script>
		var baseURL = "${path}/sys/notice/notice/";
		//列表
		var allListURL = "doAllList.do";
		var userListURL = "doUserList.do";
		
		var alluser=${command.notice.alluser};
		

		function doBack(){
			if(alluser==1){
				self.location=baseURL + allListURL;
			}else{
				self.location=baseURL + userListURL;
			}
		}
	</script>
</html>
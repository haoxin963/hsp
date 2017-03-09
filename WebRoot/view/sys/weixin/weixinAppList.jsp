<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>微信列表</title>
	<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />


	</head>
	<body class="bodyLayout">
	<c:if test="${ param.selector ne 'true' }">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="编辑"></jsp:param>
					
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
				
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
				
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
					
									 
			</jsp:include>
			</span>
		</div>
	</c:if>
		<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportTitle" id="exportTitle" />
					<input type="text" style="display: none" name="exportHead" id="exportHead" />
					<input type="text" style="display: none" name="exportField" id="exportField" />
					<table class='searchTable'>
						<tr>

							<td>
								appId${separator}
							</td>
							<td>
								<input type='text' class="input-text"  style="width:120px"name="filter[appId]">
							</td>
							 
							<td>
								微信用户名${separator}
							</td>
							<td>
								<input type='text' class="input-text"  style="width:120px"name="filter[userName]">
							</td>
							<td><jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
	
		
				
		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="appId" width=20% report=true>
						appId
					</th>
					<th w_index="secret" width=25% report=true>
						secret
					</th>
					<th w_index="custId" width=15% report=true>
						所属站点
					</th>
					<th w_index="userName" width=20% report=true>
						微信用户名
					</th> 
					<th w_index="barcode" width=25% report=true>
						二维码
					</th>

					<c:if test="${ param.selector ne 'true' }">
							<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>
		
		</table>
		
				<script type="text/javascript" src="${vpath}/sys/weixin/script/weixinAppList.js"></script>
	</body>
</html>

<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<title>自定义皮肤修改项</title>
</head>

<body class="bodyLayout">
	<c:if test="${ param.selector ne 'true' }">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="批量删除"></jsp:param>
					
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete()"></jsp:param>
		
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
				
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2;"></jsp:param>
				
				</jsp:include>
			</span>
		</div>
	</c:if>




	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="id" style="width:30px"></th> 
				<th w_index="stylename" width="25%;" report="true">属性名</th>
				<th w_index="stylefunction" width="25%;" report="true">属性规则</th>
				<th w_index="stylevalue" width="25%;" report="true">属性初始值</th>
				<th  w_render="styletype" width="25%;"  report="true" >属性类型</th>
				<th w_render="operate" width="15%;">操作</th>
			</tr>
		</table>
	</div>
<script type="text/javascript"
	src="${vpath}/sys/tool/script/toolstyleList.js"></script>
</body>
</html>

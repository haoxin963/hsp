<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<title>${command.dicType.dicTypeName}</title>
	<script type="text/javascript">
		var dicTypeId = ${command.dicType.dicTypeId};
		</script>
</head>

<body class="bodyLayout">
	<c:if test="${ param.selector ne 'true' }">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新增"></jsp:param>
					<jsp:param name="texts" value="启用"></jsp:param>
					<jsp:param name="texts" value="禁用"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="funs" value="doLoad('view','')"></jsp:param>
				    <jsp:param name="funs" value="enabled()"></jsp:param>
				    <jsp:param name="funs" value="disableddic()"></jsp:param>
				<jsp:param name="funs" value="doDelete()"></jsp:param>
				<jsp:param name="ids" value="id1"></jsp:param>
				<jsp:param name="ids" value="id2"></jsp:param>
				<jsp:param name="ids" value="id3"></jsp:param>
				<jsp:param name="ids" value="id4"></jsp:param>
				<jsp:param name="icons" value="&#xe600;"></jsp:param>
				<jsp:param name="icons" value="&#xe61d;"></jsp:param>
				<jsp:param name="icons" value="&#xe61d;"></jsp:param>
				<jsp:param name="icons" value="&#xe6e2;"></jsp:param>
				</jsp:include>
			</span>
		</div>
	</c:if>




	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="dicId" style="width:30px"></th> 
				<th w_index="dicCode" width="25%;" report="true">字典代号</th>
				<th w_index="dictName" width="25%;" report="true">字典名称</th>
				<c:forEach var="extField" items="${command.dicType.extFields}">
							<c:if test="${extField.display}">
								<th w_index="${extField.extField}" width="25%">
									${extField.fieldName}
								</th>
							</c:if>
						</c:forEach>
				<th w_render="state" width="25%;" report="true">是否启用</th>
				<th  w_render="delTag" width="25%;"  report="true" >是否删除</th>
				<th w_render="operate" width="15%;">操作</th>
			</tr>
		</table>
	</div>
<script type="text/javascript"
	src="${vpath}/sys/dic/script/dicList.js"></script>
</body>
</html>

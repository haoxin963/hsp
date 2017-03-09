<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<title>参数配置</title>
	<jsp:include page="/resource/inc/incMeta.jsp" />
     <jsp:include page="/resource/inc/incCssJs.jsp" />
	</head>
	<body class="bodyLayout"> 
	    <div class="layui-tab" lay-filter="role" >
		<ul class="layui-tab-title">
		<c:forEach var="itemGroup" items="${itemGroups}"   varStatus="status">
		       	<li     <c:if test="${status.count == 1}">class="layui-this" </c:if>>${itemGroup.value.itemGroupName}</li>
		</c:forEach>
		</ul>
		<div class="layui-tab-content" >
		     <c:forEach var="itemGroup" items="${itemGroups}"   varStatus="status">
		     	<c:if test="${status.count == 1}"> <div class="layui-tab-item   layui-show "   ></c:if>
		     	<c:if test="${status.count != 1}"> <div class="layui-tab-item "  ></c:if>
				<iframe id="viewFrame"  scrolling="yes" frameborder="0"  style="width:100%;min-height:1000px;"
					src="${path}/sys/config/toEditConfig.do?devTag=${devTag}&itemGroup=${itemGroup.key}&custId=${param.custId}"  ></iframe>
			</div>
		    
		     </c:forEach>
		
		</div>
		<script>
		layui.use(
				'element',
				function() {
					var element = layui.element();
				});
		
		</script>
	</body>
</html>

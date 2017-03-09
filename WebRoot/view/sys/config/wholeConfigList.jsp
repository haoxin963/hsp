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
		<title>配置列表</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>


	</head>
	<script type="text/javascript">
	var setting = {
		data: {
			simpleData: {
				enable: true,
				idKey:"id",
				pIdKey:"parentId"
			},
			key: { 
				name: "statName" 
			}
		},
		callback: { 
	        onClick: onTreeClickBack
	    }
	};
		 
	function onTreeClickBack(event, treeId, treeNode){ 
		var station  = treeNode.domainAddress;
		$('#configIfm').attr('src','${path}/sys/config/listConfig.do?devTag=${devTag}&custId='+station);
		
	};
	$(document).ready(function(){
	 	createTree(); 
	}); 
	</script>
	<body class="easyui-layout" fit="true">
		<div data-options="region:'west',split:true" title="站点列表"
			style="width: 180px;">
			<jsp:include page="/com/inc/incSimpleTree.jsp">
				<jsp:param name="serviceBeanId" value="stationServiceImpl" />
			</jsp:include>
		</div>
		<div data-options="region:'center',border:false"
			style="overflow: hidden;">
			<iframe id="configIfm" src="" width="100%" frameborder="0" scrolling="auto"
				height="100%"></iframe>
		</div>
	</body>
</html>

<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>pubmodule_noticecategory_tbl列表</title>
		<jsp:include page="/resource/inc/incMeta.jsp" />
        <jsp:include page="/resource/inc/incCssJs.jsp" />
</head>
<body>
       <div >
				<script>
	$(function(){
		 createTree();
	}); 
	var setting = {
			check: {
				enable: true,
				chkStyle:"radio",
				radioType: "all"
			},
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
					pIdKey:"parentId"
				}
			}
	};
	 
	
	function getChecked(){ 
		var obj = [];
		var nodes = zTree.getCheckedNodes(true);  
		for(var i=0;i<nodes.length;i++){ 
			 if(nodes[i].id !="0"){
				 var item = {"id":nodes[i].id,"name":nodes[i].name};
				 obj.push(item);
			 }
		} 
		return obj;
	}
	
	function expendFirstLevel(zTree){
		zTree.expandNode(zTree.getNodes()[0], true);
	}
</script>
<jsp:include page="/resource/inc/incSimpleTree.jsp">
			<jsp:param name="serviceBeanId" value="noticeCategoryServiceImpl" /> 
	       <jsp:param name="callBack" value="expendFirstLevel" />	
</jsp:include>
		 </div>
		 <script>
			function doSave(obj) {
				obj.closeAndRef();
				obj.selectorCategoryBack(getChecked());
			}
		 </script>
</body>
</html>
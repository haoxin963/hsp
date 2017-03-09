<%@page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="zTreeDemoBackground left">
	<ul id="tree" class="ztree"></ul>
</div>
<SCRIPT type="text/javascript"> 
		var zTree;
		function createTree() {
		  var param = {};
			 if(${not empty param.treeParamForm}){
			  	param = $("#${param.treeParamForm}").serializeJson(); 
			 } 
			 param["filter[serviceBeanId]"] = "${param.serviceBeanId}";
			 param["filter[serviceMethod]"] = "${param.serviceMethod}";
	        var zNodes;
	        $.ajax({
	            url: '${path}/system/pubmodule/doTree.do',
	            data: param,
	            type: 'POST',
	            async:false,
	            dataType: "json",
	            success: function(result) {  
	               	zTree = $.fn.zTree.init($("#tree"), setting, result);
	               	<c:if test="${not empty param.callBack}">
	               		${param.callBack}(zTree);
	               	</c:if>
	               	return zTree;
	            },
	            error: function(msg) {
	              	return null;
	            }
	        });
	    }; 
	  
</SCRIPT>


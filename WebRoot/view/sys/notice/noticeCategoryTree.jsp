<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	
	
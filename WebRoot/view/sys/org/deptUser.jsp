<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<script type="text/javascript" src="${path}/com/jquery.min.js"></script>
	<jsp:include page="/com/inc/incTreeCssJs.jsp"></jsp:include>
	<SCRIPT type="text/javascript">
		<!--
		var setting = {
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};

		var code;
		function setCheck() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"), 
			type = { "Y" : "s", "N" : "ps" }; 
			zTree.setting.check.chkboxType = type; 
			showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');
		}
		function showCode(str) {
			if (!code) code = $("#code");
			code.empty();
			code.append("<li>"+str+"</li>");
		}
		
		var tree;
		$(document).ready(function(){ 
			$.ajax({
			   type: "POST",
			   dataType:"json",
			   url: "${path}/system/pubmodule/deptUser.json",
			   success: function(r){ 
				 tree = $.fn.zTree.init($("#treeDemo"), setting, r.command);
				 setCheck(); 
			   }
			});
		});
		//-->
	</SCRIPT>
	</head> 
	<body> 
		<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	 
	</body>
	<script>
		function getChecked(){ 
			var obj = [];
			var nodes = tree.getCheckedNodes(true); 
			for(var i=0;i<nodes.length;i++){
				 if(nodes[i].cls == "u"){
					 var item = {"id":nodes[i].id,"name":nodes[i].name,"key":"value"};
					 obj.push(item);
				 }
			} 
			return obj;
		}	
	</script>
</html>
